package com.logic_thinkering

import com.logic_thinkering.LogicThinkeringKotlin.MOD_ID
import com.logic_thinkering.items.ReinforcedCopperShield
import com.logic_thinkering.items.ReinforcedCopperSword
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModItems {

    private val reinforcedCopperShield = ReinforcedCopperShield()
    private val reinforcedCopperSword = ReinforcedCopperSword()

    fun registerItems() {
        registerItem(reinforcedCopperShield, "reinforced_copper_shield")
        registerItem(reinforcedCopperSword, "reinforced_copper_sword")

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register { entries ->
            entries.add(reinforcedCopperSword)
            entries.add(reinforcedCopperShield)
        }
    }

    private fun registerItem(item: Item, id: String): Item {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, id), item)
    }
}