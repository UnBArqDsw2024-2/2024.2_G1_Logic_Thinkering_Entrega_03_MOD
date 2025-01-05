package com.logic_thinkering

import com.logic_thinkering.LogicThinkeringKotlin.MOD_ID
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import kotlin.collections.plusAssign


/**
 * Registers a list of items in the Minecraft registry with a specified group and settings.
 *
 * @param init A lambda function used to initialize the `ItemRegistryBuilder` and register items.
 */
fun registerItems(init: ItemRegistryBuilder.() -> Unit) {
    val builder = ItemRegistryBuilder()
    builder.init()
    builder.register()
}

@DslMarker
annotation class ItemRegistryDsl

/**
 * A builder class for registering Items with specific settings and item groups.
 */
@ItemRegistryDsl
class ItemRegistryBuilder {
    private val items = mutableListOf<Pair<Item, String>>()
    private var itemGroup: RegistryKey<ItemGroup>? = null


    /**
     * Sets the item group that the items will be added to.
     *
     * @param group The group to add the items to.
     * @return The current instance of `ItemRegistryBuilder`.
     */
    fun group(group: RegistryKey<ItemGroup>) = apply { itemGroup = group }

    /**
     * Adds an item with a specific identifier to be registered.
     *
     * @param name The name for the item being registered.
     */
    infix fun Item.with(name: String) {
        items += this to name
    }

    /**
     * Registers an item with a name. This function is used to build and register an item.
     *
     * Created for compatability with java
     *
     * @param name The name for the block being registered.
     * @return this instance of BlockRegistryBuilder
     */
    fun with(item: Item, name: String) = apply { items += item to name }

    fun register() {
        val group = checkNotNull(itemGroup) { "Item group must be set before registration" }
        if (itemGroup == null)
            throw IllegalStateException("Item group must be set before block item registration")
        items.forEach { (item, name) ->
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item)
        }
        ItemGroupEvents.modifyEntriesEvent(group).register {
            items.forEach { (item, _) ->  it.add(item) }
        }
    }

}