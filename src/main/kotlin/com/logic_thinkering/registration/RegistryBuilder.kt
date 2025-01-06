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
    var init: ItemFactory,
    var itemGroup: RegistryKey<ItemGroup>,
    var settings: Item.Settings,
    var name: String? = null,
)

@RegistryDsl
class ItemRegistryGroup {
    private val _itemsFactories: MutableList<Pair<ItemFactory, ItemBuilderConfig>> = mutableListOf()
    private val _items: MutableList<Triple<Item, String, RegistryKey<ItemGroup>>> = mutableListOf()
    var itemGroup: RegistryKey<ItemGroup>? = null
    var settings: Item.Settings = Item.Settings()

    val itemFactories: List<Pair<ItemFactory, ItemBuilderConfig>>
        get() = _itemsFactories

    val items: List<Triple<Item, String, RegistryKey<ItemGroup>>>
        get() = _items

    infix fun ItemFactory.with(config: ItemBuilderConfig.() -> Unit) {
        checkNotNull(itemGroup) { "Item group must be specified for item" }
        val configObject = ItemBuilderConfig(this, itemGroup!!, settings)
        configObject.config()
        checkNotNull(configObject.name) { "Name must be specified for block." }
        _itemsFactories += this to configObject
    }

    infix fun ItemFactory.with(name: String) {
        checkNotNull(itemGroup) { "Item group must be specified for item" }
        _itemsFactories += this to ItemBuilderConfig(this, itemGroup!!, settings,name)
    }
    
    infix fun Item.with(name: String) {
        checkNotNull(itemGroup) { "Item group must be specified for item" }
        _items += Triple(this, name, itemGroup!!)
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
    private val _blockFactories : MutableMap<BlockInit, BlockBuilderConfig> = HashMap()
    private val _blocks : MutableList<Triple<Block, String, RegistryKey<ItemGroup>?>> = mutableListOf()
    var itemGroup: RegistryKey<ItemGroup>? = null
    var settings: AbstractBlock.Settings = AbstractBlock.Settings.create()

    val blocks: MutableList<Triple<Block, String, RegistryKey<ItemGroup>?>>
        get() = _blocks

    val blockFactories: Map<BlockInit, BlockBuilderConfig>
        get() = _blockFactories

    infix fun BlockInit.with(config: BlockBuilderConfig.() -> Unit) {
        val configObject = BlockBuilderConfig(this, itemGroup!!, settings)
        configObject.config()
        checkNotNull(configObject.name) { "Name must be specified for block." }
        _blockFactories[this] = configObject
    }

    infix fun BlockInit.with(name: String) {
        _blockFactories[this] = BlockBuilderConfig(this, itemGroup, settings, name)
    }

    infix fun Block.with(name: String) {
        _blocks += Triple(this, name, itemGroup)
    }
}

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
        checkNotNull(itemGroup.settings) { "settings must not be null when registering items." }
        checkNotNull(itemGroup.itemGroup) { "Item group must not be null when registering items." }
        itemFactories += itemGroup.itemFactories.map { (init, config)  -> ItemConfig(init, config.settings, Identifier.of(MOD_ID, config.name!!), config.itemGroup) }
        items += itemGroup.items
    }

    fun blocks(init: BlockRegistryGroup.() -> Unit) {
        val blockGroup = BlockRegistryGroup()
        blockGroup.init()
        checkNotNull(blockGroup.settings) { "settings must not be null when registering items." }
        blockFactories += blockGroup.blockFactories.map { (init, config)  -> BlockConfig(init, config.settings!!, Identifier.of(MOD_ID, config.name!!), config.itemGroup!!) }
        blocks += blockGroup.blocks
    }

    fun build(): RegistryHelper {
        return RegistryHelper(blockFactories, itemFactories, items, blocks)
    }
}