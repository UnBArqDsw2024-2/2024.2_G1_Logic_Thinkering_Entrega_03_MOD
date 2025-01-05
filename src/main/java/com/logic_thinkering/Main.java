package com.logic_thinkering;

import com.logic_thinkering.network.NetworkRegistry;
import com.logic_thinkering.screenhandler.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "logic_thinkering";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Blocos.inicializa();
		BlockEntities.inicializa();
		ModScreenHandlers.inicializa();
		NetworkRegistry.registerPackets();
		Blocos.inicializa();
		new LogicThinkeringItem();
		LogicThinkeringKotlinKt.initialize();
		LOGGER.info("OK");
	}
}