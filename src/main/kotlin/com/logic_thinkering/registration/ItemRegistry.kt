package com.logic_thinkering.registration

import net.minecraft.item.Item

data class ItemDSLConfig (
    override var factory: ItemFactory,
    override var itemGroup: Group,
    override var settings: Item.Settings,
    override var name: String? = null,
) : IDSLConfig<Item, Item.Settings>

@RegistryDsl
class ItemRegistryDslGroup : AbstractRegistryDslGroup<Item, Item.Settings, ItemDSLConfig>() {
    override var settings: Item.Settings = Item.Settings()

    override fun createConfig(factory: (Item.Settings) -> Item, name: String?) =
        ItemDSLConfig(factory, itemGroup, settings, name)
}