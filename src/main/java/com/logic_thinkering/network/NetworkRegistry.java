package com.logic_thinkering.network;

import com.logic_thinkering.block.entity.MiningMachineBlockEntity;
import com.logic_thinkering.block.miningmachine.LineMiningStrategy;
import com.logic_thinkering.block.miningmachine.MiningStrategy;
import com.logic_thinkering.block.miningmachine.RadiusMiningStrategy;
import com.logic_thinkering.screenhandler.MiningMachineScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class NetworkRegistry {
    public static void registerClientToServerPackets() {
        PayloadTypeRegistry.playC2S().register(MiningStrategyPayload.ID, MiningStrategyPayload.CODEC);
    }

    public static void registerServerToClientPackets() {
        ServerPlayNetworking.registerGlobalReceiver(MiningStrategyPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();

            BlockPos pos = ((MiningMachineScreenHandler) player.currentScreenHandler).getBlockEntity().getPos();
            MiningMachineBlockEntity blockEntity = (MiningMachineBlockEntity) player.getWorld().getBlockEntity(pos);

            if (blockEntity != null) {
                MiningStrategy newStrategy = createMiningStrategyFromName(payload.strategy());
                blockEntity.setMiningStrategy(newStrategy, player.getServerWorld(), pos);
                player.sendMessage(Text.of("Você agora está minerando usando a estratégia: " + payload.strategy()),
                        false);
            }
        });
    }

    private static MiningStrategy createMiningStrategyFromName(String strategy) {
        if (strategy.equals("radius")) {
            return new RadiusMiningStrategy(5);
        }
        else if (strategy.equals("line")) {
            return new LineMiningStrategy(60);
        }
        return new RadiusMiningStrategy(5);
    }

    public static void registerPackets() {
        registerClientToServerPackets();
        registerServerToClientPackets();
    }
}
