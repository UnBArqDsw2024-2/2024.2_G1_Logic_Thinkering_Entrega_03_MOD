package com.logic_thinkering

import com.logic_thinkering.digitalcircuits.*
import com.logic_thinkering.items.ReinforcedCopperShield
import com.logic_thinkering.items.ReinforcedCopperSword
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Blocks
import net.minecraft.item.ItemGroups
import org.slf4j.Logger
import org.slf4j.LoggerFactory


const val MOD_ID = "logic_thinkering"
val logger: Logger = LoggerFactory.getLogger(MOD_ID)

fun initialize() {
    logger.info("Initializing Logic Thinkering mod, Kotlin side!")

    registerBlocks {
        group(LogicThinkeringItemGroup.LOGICTHINKERING_GROUP)
        settings(Settings.copy(Blocks.REPEATER))
        decorator {
            if (it is AbstractLogicGate)
                LogicLoggerDecorator(it)
            else
                it
        }
        ::ORGate with "or_gate"
        ::ANDGate with "and_gate"
        ::XORGate with "xor_gate"
        ::NOTGate with "not_gate"
        ::NORGate with "nor_gate"
        ::NANDGate with "nand_gate"
        ::XNORGate with "xnor_gate"
    }

    registerItems {
        group(ItemGroups.COMBAT)
        ReinforcedCopperShield() with "reinforced_copper_shield"
        ReinforcedCopperSword() with "reinforced_sword"
    }

    ModComponents.initialize()
}
