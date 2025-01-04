package com.logic_thinkering.block.miningmachine;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class LineMiningStrategy implements MiningStrategy {

    private final int length;

    public LineMiningStrategy(int length) {
        this.length = length;
    }

    @Override
    public void mine(ServerWorld world, BlockPos pos, List<BlockPos> blocksToMine) {
        for (int i = 1; i < length; i++) {
            // A cada iteração, a posição se move para frente ao longo do eixo X
            BlockPos targetPos = pos.add(0, 0, i);

            // Minerando uma área 3x3 ao redor de targetPos, incluindo o eixo Y
            for (int x = -1; x <= 1; x++) {
                for (int y = 0; y <= 2; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos areaPos = targetPos.add(x, y, z);
                        BlockState targetState = world.getBlockState(areaPos);

                        if (areaPos.equals(pos) || targetState.getBlock() == Blocks.BEDROCK) {
                            continue;
                        }

                        if (!targetState.isAir()) {
                            blocksToMine.add(areaPos);
                        }
                    }
                }
            }
        }
    }
}
