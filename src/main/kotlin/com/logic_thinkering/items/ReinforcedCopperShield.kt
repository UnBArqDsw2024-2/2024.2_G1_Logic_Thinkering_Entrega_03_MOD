package com.logic_thinkering.items

import com.logic_thinkering.LogicThinkeringKotlin
import com.logic_thinkering.ModComponents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ShieldItem
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ReinforcedCopperShield(name: String = "reinforced_copper_shield"): ShieldItem(
    Settings().maxDamage(336).registryKey(
        RegistryKey.of(
            RegistryKeys.ITEM,
            Identifier.of(LogicThinkeringKotlin.MOD_ID, name)
        )
    )
) {
    companion object {
        private const val MIN_DAMAGE_THRESHOLD = 1.0f
        private const val MAX_CHARGE = 3
        private const val SOUND_VOLUME = 50.0f
        private const val BASE_PITCH = 1.0f
        private const val PITCH_INCREMENT = 0.1f
        private const val PARTICLE_SPREAD = 4.0
        private const val PARTICLE_HEIGHT = 1.0
        private const val PARTICLE_VELOCITY_Y = 0.1
        private const val DURABILITY_DAMAGE_PER_HIT = 1
    }

    fun handleBlockedDamage(blocker: PlayerEntity, amount: Float) {
        if (amount < MIN_DAMAGE_THRESHOLD) return

        val shieldStack = blocker.activeItem

        if (!blocker.abilities.creativeMode) {
            val currentDurability = shieldStack.maxDamage - shieldStack.damage
            if (currentDurability > 0) {
                shieldStack.damage += DURABILITY_DAMAGE_PER_HIT

                if (shieldStack.damage >= shieldStack.maxDamage) {
                    shieldStack.count = 0
                    blocker.world.playSound(
                        null,
                        blocker.x,
                        blocker.y,
                        blocker.z,
                        SoundEvents.ITEM_SHIELD_BREAK,
                        SoundCategory.PLAYERS,
                        0.8f,
                        0.8f + blocker.world.random.nextFloat() * 0.4f
                    )
                }
            }
        }

        playBlockSound(blocker.world, blocker.pos)

        findCopperSword(blocker)?.let { sword ->
            handleSwordCharge(sword, blocker)
        }
    }

    private fun findCopperSword(player: PlayerEntity) =
        player.mainHandStack.takeIf { it.item is ReinforcedCopperSword }
            ?: player.offHandStack.takeIf { it.item is ReinforcedCopperSword }

    private fun handleSwordCharge(sword: ItemStack, player: PlayerEntity) {
        val currentCharge = sword.get(ModComponents.CHARGE_COMPONENT) ?: 0
        if (currentCharge >= MAX_CHARGE) return

        val newCharge = currentCharge + 1
        sword.set(ModComponents.CHARGE_COMPONENT, newCharge)

        playSwordChargeSound(player.world, player.pos, currentCharge)
        spawnChargeParticles(player.world, player.pos, newCharge)
    }

    private fun playBlockSound(world: World, pos: Vec3d) {
        world.playSound(
            null,
            pos.x,
            pos.y,
            pos.z,
            SoundEvents.BLOCK_COPPER_STEP,
            SoundCategory.PLAYERS,
            SOUND_VOLUME,
            BASE_PITCH
        )
    }

    private fun playSwordChargeSound(world: World, pos: Vec3d, currentCharge: Int) {
        world.playSound(
            null,
            pos.x,
            pos.y,
            pos.z,
            SoundEvents.BLOCK_COPPER_STEP,
            SoundCategory.PLAYERS,
            SOUND_VOLUME,
            BASE_PITCH + (currentCharge * PITCH_INCREMENT)
        )
    }

    private fun spawnChargeParticles(world: World, pos: Vec3d, chargeLevel: Int) {
        val particleCount = chargeLevel * 10
        repeat(particleCount) {
            val randomX = pos.x + (Math.random() - 0.5) * PARTICLE_SPREAD
            val randomZ = pos.z + (Math.random() - 0.5) * PARTICLE_SPREAD

            world.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                randomX,
                pos.y + PARTICLE_HEIGHT,
                randomZ,
                0.0,
                PARTICLE_VELOCITY_Y,
                0.0
            )
        }
    }
}