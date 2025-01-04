package com.logic_thinkering;

import com.logic_thinkering.screen.MiningMachineScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.fabricmc.api.ClientModInitializer;

public class LogicThinkeringClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        HandledScreens.register(com.logic_thinkering.screenhandler.ModScreenHandlers.MINING_MACHINE_SCREEN_HANDLER, MiningMachineScreen::new);
    }
}
