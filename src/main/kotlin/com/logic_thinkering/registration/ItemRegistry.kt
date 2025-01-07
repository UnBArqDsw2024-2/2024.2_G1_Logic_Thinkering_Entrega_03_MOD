package com.logic_thinkering.registration

import com.logic_thinkering.MOD_GROUP
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey

data class ItemBuilderConfig (
    override var factory: ItemFactory,
    override var itemGroup: RegistryKey<ItemGroup>,
    override var settings: Item.Settings,
    override var name: String? = null,
) : AbstractRegistryConfig<Item, Item.Settings, RegistryKey<ItemGroup>>

@RegistryDsl
class ItemRegistryGroup : AbstractRegistryGroup<Item, Item.Settings, ItemBuilderConfig, RegistryKey<ItemGroup>>() {
    override var settings: Item.Settings = Item.Settings()
    override var itemGroup: RegistryKey<ItemGroup> = MOD_GROUP

    override fun createConfig(factory: (Item.Settings) -> Item, name: String?) =
        ItemBuilderConfig(factory, itemGroup, settings, name)
}