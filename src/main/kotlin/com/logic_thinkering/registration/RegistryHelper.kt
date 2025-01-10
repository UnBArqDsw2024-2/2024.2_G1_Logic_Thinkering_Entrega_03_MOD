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

fun registerItem(id: Identifier, item: Item, group: RegistryKey<ItemGroup>) {
    logger.info("Registering item {}", id)
    Registry.register(Registries.ITEM, id, item)
    ItemGroupEvents.modifyEntriesEvent(group).register {it.add(item)}
}

private fun registerBlock(id: Identifier, block: Block, group: RegistryKey<ItemGroup>?) {
    logger.info("Registering block {}", id)
    Registry.register(Registries.BLOCK, id, block)
    group?.let {
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, id)
        val itemSettings = Item.Settings()
            .useBlockPrefixedTranslationKey()
            .registryKey(itemKey)
        val item = BlockItem(block, itemSettings)
        registerItem(id, item, group)
    }
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
        items.forEach { (item, id, group) -> registerItem(id, item, group) }
        blocks.forEach { (block, id, group) -> registerBlock(id, block, group) }
    }
}