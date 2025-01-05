package com.logic_thinkering

import com.logic_thinkering.LogicThinkeringKotlin.MOD_ID
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

/**
 * Registers a list of blocks in the Minecraft registry with a specified group and settings.
 *
 * @param init A lambda function used to initialize the `BlockRegistryBuilder` and register blocks.
 */
fun registerBlocks(init: BlockRegistryBuilder.() -> Unit) {
    val builder = BlockRegistryBuilder()
    builder.init()
    builder.register()
}

/**
 * Type alias for a function that initializes a block with settings and returns a `Block` instance.
 *
 * @param settings Block settings (used in block registration).
 * @return A newly created `Block` instance.
 */
typealias BlockInit = (Settings) -> Block

@DslMarker
annotation class BlockRegistryDsl

/**
 * A builder class for registering blocks with specific settings.
 * It supports configuring settings, item group, and whether items should be registered for blocks.
 */
@BlockRegistryDsl
class BlockRegistryBuilder {
    private val blocks = mutableListOf<Pair<BlockInit, String>>()
    private var itemGroup: RegistryKey<ItemGroup>? = null
    private var baseSettings: Settings? = null
    private var registerItems = true

    /**
     * Sets the settings for the blocks being registered.
     *
     * @param settings The settings to be applied to the blocks.
     * @return The current instance of `BlockRegistryBuilder`.
     */
    fun settings(settings: Settings) = apply { this.baseSettings = settings }

    /**
     * Sets the item group that the block items will be added to.
     *
     * @param group The item group to add the block items to.
     * @return The current instance of `BlockRegistryBuilder`.
     */
    fun group(group: RegistryKey<ItemGroup>) = apply { itemGroup = group }

    /**
     * Sets whether to register the items for each block.
     * defaults to True.
     *
     * @param group The item group to add the block items to.
     * @return The current instance of `BlockRegistryBuilder`.
     */
    fun registerItems(registerItems: Boolean) = apply { this.registerItems = registerItems }

    /**
     * Registers a block initialization function with a name.
     *
     * @param name The name for the block being registered.
     */
    infix fun BlockInit.with(name: String) {
        blocks += this to name
    }

    /**
     * Registers a block initialization function with a name.
     *
     * Created for compatability with java
     *
     * @param name The name for the block being registered.
     * @return this instance of BlockRegistryBuilder
     */
    fun with(init: BlockInit, name: String) = apply { blocks += init to name }

    /**
     * Registers all blocks in the registry, using the provided settings and item group.
     * This method also ensures that the all the items will be registered for each block
     * if registerItems is set
     *
     * @throws IllegalStateException If settings are not defined before block registration.
     */
    fun register() {
        if (registerItems && itemGroup == null)
            throw IllegalStateException("Item group must be set before block item registration")
        if (baseSettings == null)
            throw IllegalStateException("Settings must be set before block registration")

        val constructedBlocks = blocks.map { (blockInit, name) ->
            val id = Identifier.of(MOD_ID, name)
            val key = RegistryKey.of(RegistryKeys.BLOCK, id)
            val settings = baseSettings!!.registryKey(key)
            blockInit(settings) to name
        }

        val registeredBlocks = constructedBlocks.map { (block, name) ->
            val id = Identifier.of(MOD_ID, name)
            Registry.register(Registries.BLOCK, id, block).also {
                if (registerItems) {
                    val itemKey = RegistryKey.of(RegistryKeys.ITEM, id)
                    val itemSettings = Item.Settings().registryKey(itemKey)
                    LogicThinkeringItemGroup.addItem(Registry.register(Registries.ITEM, id, BlockItem(it, itemSettings)))
                }
            }
        }

        ItemGroupEvents.modifyEntriesEvent(itemGroup).register { content ->
            registeredBlocks.forEach { block -> content.add(block) }
        }
    }
}