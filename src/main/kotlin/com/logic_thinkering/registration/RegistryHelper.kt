package com.logic_thinkering.registration

import com.logic_thinkering.MOD_ID
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
    val blockFactory: BlockFactory,
    val settings: Settings,
    val id: Identifier,
    val itemGroup: RegistryKey<ItemGroup>?,
)

data class ItemConfig (
    val itemFactory: ItemFactory,
    val settings: Item.Settings,
    val id: Identifier,
    val itemGroup: RegistryKey<ItemGroup>,
)

class RegistryHelper (
    private val blockConfigs: List<BlockConfig>,
    private val itemConfigs: List<ItemConfig>,
    private val items: MutableList<Triple<Item, String, RegistryKey<ItemGroup>>>,
    private val blocks: MutableList<Triple<Block, String, RegistryKey<ItemGroup>?>> = mutableListOf()
) {

    private fun registerBlockConfig(blockConfig: BlockConfig) {
        val key = RegistryKey.of(RegistryKeys.BLOCK, blockConfig.id)
        blockConfig.settings.registryKey(key)

        val block = blockConfig.blockFactory(blockConfig.settings)
        logger.info("Registering {} as {}", blockConfig.id, block.name)
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
        val item = itemConfig.itemFactory(itemSettings)
        Registry.register(Registries.ITEM, itemConfig.id, item)
        ItemGroupEvents.modifyEntriesEvent(itemConfig.itemGroup).register {it.add(item)}
    }

    fun register() {
        blockConfigs.forEach(::registerBlockConfig)
        itemConfigs.forEach(::registerItemConfig)
        items.forEach { (item, name, group) ->
            Registry.register(Registries.ITEM, Identifier.of(name), item)
            ItemGroupEvents.modifyEntriesEvent(group).register {it.add(item)}
        }
        blocks.forEach { (block, name, group) ->
            val id = Identifier.of(MOD_ID,name)
            Registry.register(Registries.BLOCK,  id, block)
            if (group == null) return
            val itemKey = RegistryKey.of(RegistryKeys.ITEM, id)
            val itemSettings = Item.Settings()
                .useBlockPrefixedTranslationKey()
                .registryKey(itemKey)
            val item = BlockItem(block, itemSettings)
            Registry.register(Registries.ITEM, id, item)
            ItemGroupEvents.modifyEntriesEvent(group).register {it.add(item)}
        }
    }
}