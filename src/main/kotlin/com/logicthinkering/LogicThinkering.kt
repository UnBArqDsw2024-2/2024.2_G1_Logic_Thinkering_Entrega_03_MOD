package com.logicthinkering

import com.logicthinkering.digitalcircuits.*
import com.logicthinkering.reinforceditems.ReinforcedCopperShield
import com.logicthinkering.reinforceditems.ReinforcedCopperSword
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
object LogicThinkering : ModInitializer {
    const val MOD_ID = "logic-thinkering"
    val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Initializing Logic Thinkering mod!")

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