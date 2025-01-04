package com.logic_thinkering.screenhandler;

import com.logic_thinkering.Main;
import com.logic_thinkering.network.BlockPosPayload;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static <T extends ScreenHandler, D extends CustomPayload> ExtendedScreenHandlerType<T, D>
    register(String name,
             ExtendedScreenHandlerType.ExtendedFactory<T, D> factory,
             PacketCodec<? super RegistryByteBuf, D> codec) {
        return Registry.register(Registries.SCREEN_HANDLER,
                Identifier.of(Main.MOD_ID, name),
                new ExtendedScreenHandlerType<>(factory, codec));
    }

    public static final ScreenHandlerType<MiningMachineScreenHandler> MINING_MACHINE_SCREEN_HANDLER =
            register("bloco_mineracao", MiningMachineScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    public static void inicializa() {}
}
