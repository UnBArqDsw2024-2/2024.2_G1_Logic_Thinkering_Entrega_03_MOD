package com.logic_thinkering.registration

import com.logic_thinkering.MOD_ID
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

data class ItemDSLConfig (
    override var factory: ItemFactory,
    override var itemGroup: Group,
    override var settings: Item.Settings,
    override var id: Identifier? = null
) : IDSLConfig<Item, Item.Settings>

@RegistryDsl
class ItemRegistryDslGroup : AbstractRegistryDslGroup<Item, Item.Settings, ItemDSLConfig>() {
    override var settings: Item.Settings = Item.Settings()
    override fun createInstance(factory: (Item.Settings) -> Item, id: Identifier, settings: Item.Settings): Item {
        val key = RegistryKey.of(RegistryKeys.ITEM, id)
        settings.registryKey(key)
        return factory(settings)
    }

    override fun createConfig(factory: (Item.Settings) -> Item, name: String?) =
        ItemDSLConfig(factory, itemGroup, settings, Identifier.of(MOD_ID, name))
}