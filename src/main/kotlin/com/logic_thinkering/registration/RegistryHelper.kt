package com.logic_thinkering.registration

import com.logic_thinkering.logger
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

typealias BlockFactory = (Settings) -> Block
typealias ItemFactory = (Item.Settings) -> Item

@DslMarker
annotation class RegistryDsl

fun register(init: RegistryDslInitializer.() -> Unit) {
    val registry = RegistryDslInitializer()
    registry.init()
    registry.register()
}


private fun registerItem(id: Identifier, item: Item, group: RegistryKey<ItemGroup>) {
    logger.info("Registering item {}", id)
    Registry.register(Registries.ITEM, id, item)
    ItemGroupEvents.modifyEntriesEvent(group).register {it.add(item)}
}

private fun registerBlock(id: Identifier, block: Block, group: RegistryKey<ItemGroup>?) {
    Registry.register(Registries.BLOCK, id, block)
    if (group == null) return
    val itemKey = RegistryKey.of(RegistryKeys.ITEM, id)
    val itemSettings = Item.Settings()
        .useBlockPrefixedTranslationKey()
        .registryKey(itemKey)
    val item = BlockItem(block, itemSettings)
    registerItem(id, item, group)
}

@RegistryDsl
class RegistryDslInitializer {
    private val items: MutableList<Triple<Item, Identifier, RegistryKey<ItemGroup>>> = mutableListOf()
    private val blocks: MutableList<Triple<Block, Identifier, RegistryKey<ItemGroup>>> = mutableListOf()

    fun items(init: ItemRegistryDslGroup.() -> Unit) {
        val itemGroup = ItemRegistryDslGroup()
        itemGroup.init()
        items += itemGroup.instances
    }

    fun blocks(init: BlockRegistryDslGroup.() -> Unit) {
        val blockGroup = BlockRegistryDslGroup()
        blockGroup.init()
        blocks += blockGroup.instances
    }

    fun register() {
        items.forEach { (item, id, group) -> registerItem(id, item, group) }
        blocks.forEach { (block, id, group) -> registerBlock(id, block, group) }
    }
}