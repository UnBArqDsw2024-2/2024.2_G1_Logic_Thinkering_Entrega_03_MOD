package com.logic_thinkering

import com.logic_thinkering.digitalcircuits.*
import com.logic_thinkering.items.ReinforcedCopperShield
import com.logic_thinkering.items.ReinforcedCopperSword
import net.fabricmc.api.ModInitializer
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Blocks
import net.minecraft.item.ItemGroups
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Object responsible for initializing the mod.
 * Registering blocks, items and componentes and setting the mod's identifier.
 */
object LogicThinkeringKotlin : ModInitializer {
    const val MOD_ID = "logic_thinkering"
    val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    fun initialize() {
        logger.info("Initializing Logic Thinkering mod, Kotlin side!")

        registerBlocks {
            group(ItemGroups.REDSTONE)
            settings(Settings.copy(Blocks.REPEATER))
            ::ORGate with "or_gate_block"
            ::ANDGate with "and_gate_block"
            ::XORGate with "xor_gate_block"
            ::NOTGate with "not_gate_block"
            ::NORGate with "nor_gate_block"
            ::NANDGate with "nand_gate_block"
            ::XNORGate with "xnor_gate_block"
        }

        registerItems {
            group(ItemGroups.COMBAT)
            ReinforcedCopperShield() with "reinforced_copper_shield"
            ReinforcedCopperSword() with "reinforced_sword"
        }

        ModComponents.initialize()
    }

    override fun onInitialize() {
        logger.info("Initializing Logic Thinkering mod, Kotlin side!")

        registerBlocks {
            group(ItemGroups.REDSTONE)
            settings(Settings.copy(Blocks.REPEATER))
            ::ORGate with "or_gate_block"
            ::ANDGate with "and_gate_block"
            ::XORGate with "xor_gate_block"
            ::NOTGate with "not_gate_block"
            ::NORGate with "nor_gate_block"
            ::NANDGate with "nand_gate_block"
            ::XNORGate with "xnor_gate_block"
        }

        registerItems {
            group(ItemGroups.COMBAT)
            ReinforcedCopperShield() with "reinforced_copper_shield"
            ReinforcedCopperSword() with "reinforced_sword"
        }

        ModComponents.initialize()
    }
}
