package com.logic_thinkering.block.miningmachine;

import com.logic_thinkering.block.entity.MiningMachineBlockEntity;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MiningMachineBlock extends BlockWithEntity {

    public MiningMachineBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends MiningMachineBlock> getCodec() {
        return createCodec(MiningMachineBlock::new);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MiningMachineBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            startMining((ServerWorld) world, pos);
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MiningMachineBlockEntity miningMachineBlockEntity) {
                ItemScatterer.spawn(world, pos, miningMachineBlockEntity);
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof MiningMachineBlockEntity miningMachineBlockEntity) {
                player.openHandledScreen(miningMachineBlockEntity);
            }
        }
        return ActionResult.SUCCESS;
    }

    public void startMining(ServerWorld world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MiningMachineBlockEntity miningMachineBlockEntity) {
            miningMachineBlockEntity.getMiningStrategy().mine(world, pos, miningMachineBlockEntity.getBlocksToMine());
            if (!miningMachineBlockEntity.getBlocksToMine().isEmpty()) {
                world.scheduleBlockTick(pos, this, 20);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof MiningMachineBlockEntity miningMachineEntity) {
            List<BlockPos> blocksToMine = miningMachineEntity.getBlocksToMine();
            if (!blocksToMine.isEmpty()) {
                BlockPos targetPos = blocksToMine.getFirst();
                BlockState targetState = world.getBlockState(targetPos);

                List<ItemStack> drops = Block.getDroppedStacks(
                        targetState,
                        world,
                        targetPos,
                        world.getBlockEntity(targetPos),
                         null,
                        ItemStack.EMPTY
                );

                boolean canStoreAllDrops = true;
                for (ItemStack drop : drops) {
                    if (!miningMachineEntity.canStoreItem(drop)) {
                        canStoreAllDrops = false;
                        break;
                    }
                }

                // Move o bloco para final da lista, uma vez que não é possível armazenar
                // todos os seus drops, e segue com a mineração dos demais blocos.
                if (!canStoreAllDrops) {
                    blocksToMine.addLast(blocksToMine.removeFirst());
                    world.scheduleBlockTick(pos, this, 20);
                    return;
                }

                drops.forEach(miningMachineEntity::addItemToInventory);
                blocksToMine.removeFirst();
                world.breakBlock(targetPos, false);
            }

            // Agendar o próximo tick de mineração, se houver blocos restantes
            if (!blocksToMine.isEmpty()) {
                world.scheduleBlockTick(pos, this, 20);
            }
        }
    }
}
