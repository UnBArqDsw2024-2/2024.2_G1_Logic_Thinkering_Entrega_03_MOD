package com.logic_thinkering.block.entity;

import com.logic_thinkering.BlockEntities;
import com.logic_thinkering.block.miningmachine.LineMiningStrategy;
import com.logic_thinkering.block.miningmachine.MiningStrategy;
import com.logic_thinkering.network.BlockPosPayload;
import com.logic_thinkering.screenhandler.MiningMachineScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MiningMachineBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private final List<BlockPos> blocksToMine = new ArrayList<>();
    private MiningStrategy miningStrategy;

    public MiningMachineBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.ENTIDADE_BLOCO_MINERACAO, pos, state);
        this.miningStrategy = new LineMiningStrategy(60);
    }

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public boolean canStoreItem(ItemStack item) {
        for (ItemStack slotStack : inventory) {
            // Se houver slot vazio, o item pode ser armazenado
            if (slotStack.isEmpty()) {
                return true;
            }

            // Se o item for do mesmo tipo e houver espaço para empilhar
            if (ItemStack.areItemsEqual(slotStack, item) && slotStack.getCount() < slotStack.getMaxCount() && slotStack.getCount() + item.getCount() <= slotStack.getMaxCount()) {
                return true;
            }
        }

        return false;
    }

    public void addItemToInventory(ItemStack itemStack) {
        // Primeiro, tenta agrupar o item nos slots existentes
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack currentStack = inventory.get(i);

            // Verifica se o item no slot atual é o mesmo tipo
            if (!currentStack.isEmpty() && ItemStack.areItemsEqual(currentStack, itemStack)) {
                int newCount = currentStack.getCount() + itemStack.getCount();
                if (newCount <= currentStack.getMaxCount()) {
                    currentStack.setCount(newCount); // Agrupa os itens
                    markDirty();
                    return; // Item agrupado, não precisa adicionar mais nada
                }
            }
        }

        // Se não conseguiu agrupar (ou seja, não encontrou nenhum item igual ou o empilhamento foi ultrapassado), adiciona o item em um slot vazio
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, itemStack); // Coloca o item no primeiro slot vazio
                markDirty();
                break;
            }
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Mining Machine Block");
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new BlockPosPayload(this.pos);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MiningMachineScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, inventory, registryLookup);
        super.writeNbt(nbt, registryLookup);
    }

    public List<BlockPos> getBlocksToMine() {
        return blocksToMine;
    }

    public MiningStrategy getMiningStrategy() {
        return miningStrategy;
    }

    public void setMiningStrategy(MiningStrategy newStrategy, ServerWorld world, BlockPos pos) {
        this.miningStrategy = newStrategy;
        this.blocksToMine.clear();
        this.miningStrategy.mine(world, pos, this.blocksToMine);
    }
}
