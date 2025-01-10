package com.logic_thinkering.registration

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

data class ItemDSLConfig (
    override var itemGroup: Group,
    override var settings: Item.Settings,
) : IDSLConfig<Item, Item.Settings>

@RegistryDsl
class ItemRegistryDslGroup : AbstractRegistryDslGroup<Item, Item.Settings, ItemDSLConfig>() {
    override var settings: Item.Settings = Item.Settings()
    override fun instantiate(factory: (Item.Settings) -> Item, id: Identifier, settings: Item.Settings): Item {
        val key = RegistryKey.of(RegistryKeys.ITEM, id)
        settings.registryKey(key)
        return factory(settings)
    }

    override fun createConfig() = ItemDSLConfig(itemGroup, settings)
}