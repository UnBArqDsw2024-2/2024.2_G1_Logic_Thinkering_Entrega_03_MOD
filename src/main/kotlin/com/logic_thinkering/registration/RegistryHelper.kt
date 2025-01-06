package com.logic_thinkering.registration

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

typealias BlockInit = (Settings) -> Block
typealias ItemInit = (Item.Settings) -> Item

data class BlockConfig (
    val blockInit: BlockInit,
    val settings: Settings,
    val id: Identifier,
    val itemGroup: RegistryKey<ItemGroup>?,
)

data class ItemConfig (
    val itemInit: ItemInit,
    val settings: Item.Settings,
    val id: Identifier,
    val itemGroup: RegistryKey<ItemGroup>,
)

class RegistryHelper (
    private val blocks: List<BlockConfig>,
    private val items: List<ItemConfig>,
) {
    private fun registerItem(item: Item, id: Identifier) {}

    private fun registerBlockConfig(blockConfig: BlockConfig) {
        val key = RegistryKey.of(RegistryKeys.BLOCK, blockConfig.id)
        blockConfig.settings.registryKey(key)

        val block = blockConfig.blockInit(blockConfig.settings)
        Registry.register(Registries.BLOCK, key, block)
        if (blockConfig.itemGroup == null)
            return
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, blockConfig.id)
        val itemSettings = Item.Settings()
            .useBlockPrefixedTranslationKey()
            .registryKey(itemKey)
        val item = BlockItem(block, itemSettings)
        Registry.register(Registries.ITEM, blockConfig.id, item)
        ItemGroupEvents.modifyEntriesEvent(blockConfig.itemGroup).register {it.add(item)}
    }

    private fun registerItemConfig(itemConfig: ItemConfig) {
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, itemConfig.id)
        val itemSettings = Item.Settings()
            .useBlockPrefixedTranslationKey()
            .registryKey(itemKey)
        val item = itemConfig.itemInit(itemSettings)
        Registry.register(Registries.ITEM, itemConfig.id, item)
        ItemGroupEvents.modifyEntriesEvent(itemConfig.itemGroup).register {it.add(item)}
    }

    fun register() {
        blocks.forEach(::registerBlockConfig)
        items.forEach(::registerItemConfig)
    }
}