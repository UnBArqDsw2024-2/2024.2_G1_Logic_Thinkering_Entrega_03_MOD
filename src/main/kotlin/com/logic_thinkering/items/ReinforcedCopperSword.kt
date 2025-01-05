package com.logic_thinkering.items

import com.logic_thinkering.LogicThinkeringKotlin
import com.logic_thinkering.ModComponents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import kotlin.math.max

class ReinforcedCopperSword(
    private val material: ToolMaterial = ToolMaterial(
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        200,
        6.5f,
        2.0f,
        14,
        ItemTags.STONE_TOOL_MATERIALS),
    name: String = "reinforced_copper_sword",
    settings: Settings = Settings().registryKey(
        RegistryKey.of(
            RegistryKeys.ITEM,
            Identifier.of(LogicThinkeringKotlin.MOD_ID, name)
        )
    )
) : SwordItem(material, 4.0F, 5.0F, settings) {

    companion object {
        private const val DEFAULT_CHARGE = 0
        private const val SOUND_VOLUME = 1.0f
        private const val SOUND_PITCH = 1.0f
        private const val MAX_CHARGE = 2
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        if (attacker !is PlayerEntity) {
            return super.postHit(stack, target, attacker)
        }

        processChargedAttack(stack, target, attacker)
        return super.postHit(stack, target, attacker)
    }

    private fun processChargedAttack(stack: ItemStack, target: LivingEntity, attacker: PlayerEntity) {
        val currentCharge = getCurrentCharge(stack)

        if (currentCharge <= 0) return

        dealChargedDamage(target, attacker, currentCharge)
        resetCharge(stack)
        playAttackSound(target, attacker)
    }

    private fun getCurrentCharge(stack: ItemStack): Int =
        stack.get(ModComponents.CHARGE_COMPONENT) ?: DEFAULT_CHARGE

    private fun dealChargedDamage(target: LivingEntity, attacker: PlayerEntity, chargeLevel: Int) {
        val damageDealt = 4.0F * (max(chargeLevel, MAX_CHARGE) + 1)
        val world = target.world
        target.damage(world as ServerWorld?, target.damageSources.playerAttack(attacker), damageDealt)
    }

    private fun resetCharge(stack: ItemStack) {
        stack.set(ModComponents.CHARGE_COMPONENT, DEFAULT_CHARGE)
    }

    private fun playAttackSound(target: LivingEntity, attacker: PlayerEntity) {
        attacker.world.playSound(
            null,
            target.x,
            target.y,
            target.z,
            SoundEvents.BLOCK_COPPER_BREAK,
            SoundCategory.PLAYERS,
            SOUND_VOLUME,
            SOUND_PITCH
        )
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: TooltipContext?,
        tooltip: MutableList<Text>,
        context: TooltipType
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        addChargeTooltip(stack, tooltip)
    }

    private fun addChargeTooltip(stack: ItemStack, tooltip: MutableList<Text>) {
        val chargeLevel = getCurrentCharge(stack)

        if (chargeLevel > 0) {
            tooltip.add(
                Text.translatable("item.logic_thinkering.counter.info", chargeLevel)
                    .formatted(Formatting.GOLD)
            )
        }
    }
}