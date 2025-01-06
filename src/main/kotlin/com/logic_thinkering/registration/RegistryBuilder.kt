package com.logic_thinkering.registration

import com.logic_thinkering.MOD_ID
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

@DslMarker
annotation class RegistryDsl

data class ItemBuilderConfig (
    var init: ItemInit,
    var itemGroup: RegistryKey<ItemGroup>,
    var settings: Item.Settings,
    var name: String? = null,
)

@RegistryDsl
class ItemRegistryGroup {
    private val _items: MutableMap<ItemInit, ItemBuilderConfig> = HashMap()
    var itemGroup: RegistryKey<ItemGroup>? = null
    var settings: Item.Settings = Item.Settings()

    val items: Map<ItemInit, ItemBuilderConfig>
        get() = _items

    infix fun ItemInit.with(config: ItemBuilderConfig.() -> Unit) {
        checkNotNull(itemGroup) { "Item group must be specified for item" }
        val configObject = ItemBuilderConfig(this, itemGroup!!, settings)
        configObject.config()
        checkNotNull(configObject.name) { "Name must be specified for block." }
        _items[this] = configObject
    }

    infix fun ItemInit.with(name: String) {
        checkNotNull(itemGroup) { "Item group must be specified for item" }
        _items[this] = ItemBuilderConfig(this, itemGroup!!, settings,name)
    }
}


data class BlockBuilderConfig (
    var init: BlockInit,
    var itemGroup: RegistryKey<ItemGroup>?,
    var settings: AbstractBlock.Settings,
    var name: String? = null,
)

@RegistryDsl
class BlockRegistryGroup {
    private val _blocks : MutableMap<BlockInit, BlockBuilderConfig> = HashMap()
    var itemGroup: RegistryKey<ItemGroup>? = null
    var settings: AbstractBlock.Settings = AbstractBlock.Settings.create()

    val blocks: Map<BlockInit, BlockBuilderConfig>
        get() = _blocks

    infix fun BlockInit.with(config: BlockBuilderConfig.() -> Unit) {
        val configObject = BlockBuilderConfig(this, itemGroup!!, settings)
        configObject.config()
        checkNotNull(configObject.name) { "Name must be specified for block." }
        _blocks[this] = configObject
    }

    infix fun BlockInit.with(name: String) {
        checkNotNull(itemGroup) { "Item group must be specified for block" }
        _blocks[this] = BlockBuilderConfig(this, itemGroup!!, settings, name)
    }
}

fun register(init: RegistryInitializer.() -> Unit): RegistryHelper {
    val registry = RegistryInitializer()
    registry.init()
    return registry.build()
}

@RegistryDsl
class RegistryInitializer {
    private val blocks: MutableList<BlockConfig> = mutableListOf()
    private val items: MutableList<ItemConfig> = mutableListOf()

    fun items(init: ItemRegistryGroup.() -> Unit) {
        val itemGroup = ItemRegistryGroup()
        itemGroup.init()
        checkNotNull(itemGroup.settings) { "settings must not be null when registering items." }
        checkNotNull(itemGroup.itemGroup) { "Item group must not be null when registering items." }
        items += itemGroup.items.map {(init, config)  -> ItemConfig(init, config.settings, Identifier.of(MOD_ID, config.name!!), config.itemGroup) }
    }

    fun blocks(init: BlockRegistryGroup.() -> Unit) {
        val blockGroup = BlockRegistryGroup()
        blockGroup.init()
        checkNotNull(blockGroup.settings) { "settings must not be null when registering items." }
        blocks += blockGroup.blocks.map {(init, config)  -> BlockConfig(init, config.settings!!, Identifier.of(MOD_ID, config.name!!), config.itemGroup!!) }
    }

    fun build(): RegistryHelper {
        return RegistryHelper(blocks, items)
    }
}