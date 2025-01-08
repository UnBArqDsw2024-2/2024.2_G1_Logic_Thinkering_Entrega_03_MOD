package com.logic_thinkering.registration

import com.logic_thinkering.MOD_ID
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

@DslMarker
annotation class RegistryDsl

fun register(init: RegistryInitializer.() -> Unit) {
    val registry = RegistryInitializer()
    registry.init()
    registry.build().register()
}

@RegistryDsl
class RegistryInitializer {
    private val blockFactories: MutableList<BlockConfig> = mutableListOf()
    private val itemFactories: MutableList<ItemConfig> = mutableListOf()
    private val items: MutableList<Triple<Item, String, RegistryKey<ItemGroup>>> = mutableListOf()
    private val blocks: MutableList<Triple<Block, String, RegistryKey<ItemGroup>?>> = mutableListOf()

    fun items(init: ItemRegistryGroup.() -> Unit) {
        val itemGroup = ItemRegistryGroup()
        itemGroup.init()
        itemFactories += itemGroup.factories.map { (init, config) ->
            val id = Identifier.of(MOD_ID, config.name) ?: throw NullPointerException("Item name must be set")
            ItemConfig(init, config.settings, id, config.itemGroup)
        }
        items += itemGroup.instances
    }

    fun blocks(init: BlockRegistryGroup.() -> Unit) {
        val blockGroup = BlockRegistryGroup()
        blockGroup.init()
        blockFactories += blockGroup.factories.map { (init, config)  -> BlockConfig(init, config.settings, Identifier.of(MOD_ID, config.name ?: "error"), config.itemGroup) }
        blocks += blockGroup.instances
    }

    fun build() = RegistryHelper(blockFactories, itemFactories, items, blocks)
}