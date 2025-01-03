package com.logicthinkering.weapon

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Item.TooltipContext
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

interface WeaponBehavior {
    fun onHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean
    fun appendTooltip(stack: ItemStack, world: TooltipContext?, tooltip: MutableList<Text>, context: TooltipType)
}