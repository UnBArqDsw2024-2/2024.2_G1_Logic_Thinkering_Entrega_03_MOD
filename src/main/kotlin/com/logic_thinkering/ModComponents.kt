package com.logic_thinkering

import com.logic_thinkering.LogicThinkeringKotlin.MOD_ID
import com.mojang.serialization.Codec
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModComponents {
    val CHARGE_COMPONENT: ComponentType<Int> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(MOD_ID, "charge_level"),
        ComponentType.builder<Int>().codec(Codec.INT).build()
    )

    internal fun initialize() {
        LogicThinkeringKotlin.logger.info("Registering {} components", MOD_ID)
    }
}
