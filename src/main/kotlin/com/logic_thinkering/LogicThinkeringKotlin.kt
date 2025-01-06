package com.logic_thinkering

import com.logic_thinkering.digitalcircuits.*
import com.logic_thinkering.items.ReinforcedCopperShield
import com.logic_thinkering.items.ReinforcedCopperSword
import com.logic_thinkering.registration.register
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val MOD_ID = "logic_thinkering"
val logger: Logger = LoggerFactory.getLogger(MOD_ID)

private val group = LogicThinkeringItemGroup.LOGICTHINKERING_GROUP
fun initialize() {
    logger.info("Initializing Logic Thinkering mod, Kotlin side!")

    register {
        items {
            itemGroup = group
            ReinforcedCopperSword() with "reinforced_copper_sword"
            ReinforcedCopperShield() with "reinforced_copper_shield"
        }
        blocks {
            settings = Settings.copy(Blocks.REPEATER)
            itemGroup = group
            ::ORGate with "or_gate"
            ::ANDGate with "and_gate"
            ::XORGate with "xor_gate"
            ::NOTGate with "not_gate"
            ::NORGate with "nor_gate"
            ::NANDGate with "nand_gate"
            ::XNORGate with "xnor_gate"
        }
    }
}