package com.logic_thinkering.registration

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

class ItemDSLConfig : IDSLConfig<Item.Settings> {
    override var itemGroup: Group? = null
    override var settings: Item.Settings? = null
}

class ItemRegistryDslGroup : AbstractRegistryDslGroup<Item, Item.Settings, ItemDSLConfig>() {
    override var settings: Item.Settings = Item.Settings()
    override fun instantiate(factory: (Item.Settings) -> Item, id: Identifier, settings: Item.Settings): Item {
        val key = RegistryKey.of(RegistryKeys.ITEM, id)
        settings.registryKey(key)
        return factory(settings)
    }

    override fun createConfig() = ItemDSLConfig()
}