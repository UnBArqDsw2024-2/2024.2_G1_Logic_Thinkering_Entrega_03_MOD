package com.logic_thinkering.block.miningmachine;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.List;

public class RadiusMiningStrategy implements MiningStrategy {
    private final int radius;

    public RadiusMiningStrategy(int radius) {
        this.radius = radius;
    }

    @Override
    public void mine(ServerWorld world, BlockPos pos, List<BlockPos> blocksToMine) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = pos.add(new Vec3i(x, y, z));
                    BlockState targetState = world.getBlockState(targetPos);

                    // Verificar se não é o próprio bloco nem Bedrock
                    if (targetPos.equals(pos) || targetState.getBlock() == Blocks.BEDROCK) {
                        continue;
                    }

                    // Adicionar o bloco à lista de blocos a serem minerados
                    if (!targetState.isAir()) {
                        blocksToMine.add(targetPos);
                    }
                }
            }
        }
    }
}
