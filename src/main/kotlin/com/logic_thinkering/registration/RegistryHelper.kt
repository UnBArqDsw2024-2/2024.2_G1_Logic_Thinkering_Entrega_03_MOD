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

data class BlockConfig (
    val factory: BlockFactory,
    val settings: Settings,
    val id: Identifier,
    val itemGroup: RegistryKey<ItemGroup>,
)

data class ItemConfig (
    val factory: ItemFactory,
    val settings: Item.Settings,
    val id: Identifier,
    val itemGroup: RegistryKey<ItemGroup>,
)

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

class RegistryHelper (
    private val blockConfigs: List<BlockConfig>,
    private val itemConfigs: List<ItemConfig>,
    // TODO: change string to id
    private val items: MutableList<Triple<Item, Identifier, RegistryKey<ItemGroup>>> = mutableListOf(),
    private val blocks: MutableList<Triple<Block, Identifier, RegistryKey<ItemGroup>>> = mutableListOf()
) {

    private fun registerBlockConfig(blockConfig: BlockConfig) {
        val block = blockConfig.factory(blockConfig.settings)
        registerBlock(blockConfig.id, block, blockConfig.itemGroup)
    }

    private fun registerItemConfig(itemConfig: ItemConfig) {
        val item = itemConfig.factory(itemConfig.settings)
        registerItem(itemConfig.id, item, itemConfig.itemGroup)
    }

    fun register() {
        blockConfigs.forEach(::registerBlockConfig)
        itemConfigs.forEach(::registerItemConfig)
        items.forEach { (item, id, group) -> registerItem(id, item, group) }
        blocks.forEach { (block, id, group) -> registerBlock(id, block, group) }
    }
}