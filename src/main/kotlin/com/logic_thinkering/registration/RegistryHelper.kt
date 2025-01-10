package com.logic_thinkering.registration

import com.logic_thinkering.logger
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

@DslMarker
annotation class RegistryDsl

fun register(init: RegistryDslInitializer.() -> Unit) {
    RegistryDslInitializer().apply(init).register()
}

private fun registerItem(entry: RegistryEntry<Item>) {
    logger.info("Registering item {}", entry.id)
    Registry.register(Registries.ITEM, entry.id, entry.element)
    ItemGroupEvents.modifyEntriesEvent(entry.group).register {it.add(entry.element)}
}

private fun registerBlock(entry: RegistryEntry<Block>) {
    logger.info("Registering block {}", entry.id)
    Registry.register(Registries.BLOCK, entry.id, entry.element)
    val itemKey = RegistryKey.of(RegistryKeys.ITEM, entry.id)
    val itemSettings = Item.Settings()
        .useBlockPrefixedTranslationKey()
        .registryKey(itemKey)
    val item = BlockItem(entry.element, itemSettings)
    Registry.register(Registries.ITEM, entry.id, item)
    ItemGroupEvents.modifyEntriesEvent(entry.group).register {it.add(item)}
}

data class RegistryEntry<T>(
    val element: T,
    val id: Identifier,
    val group: RegistryKey<ItemGroup>
)

@RegistryDsl
class RegistryDslInitializer {
    private val items: MutableList<RegistryEntry<Item>> = mutableListOf()
    private val blocks: MutableList<RegistryEntry<Block>> = mutableListOf()

    fun items(init: ItemRegistryDslGroup.() -> Unit) {
        items += ItemRegistryDslGroup().apply(init).instances
    }

    fun blocks(init: BlockRegistryDslGroup.() -> Unit) {
        blocks += BlockRegistryDslGroup().apply(init).instances
    }

    fun register() {
        items.forEach(::registerItem)
        blocks.forEach(::registerBlock)
    }
}