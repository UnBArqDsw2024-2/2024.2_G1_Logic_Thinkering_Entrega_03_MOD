package com.logic_thinkering.block.miningmachine;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface MiningStrategy {
    void mine(ServerWorld world, BlockPos pos, List<BlockPos> blocksToMine);
}
