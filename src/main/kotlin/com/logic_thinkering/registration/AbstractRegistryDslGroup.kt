package com.logic_thinkering.registration

import com.logic_thinkering.MOD_GROUP
import com.logic_thinkering.MOD_ID
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

typealias Group = RegistryKey<ItemGroup>

interface IDSLConfig<Settings> {
    var itemGroup: Group?
    var settings: Settings?
}

@RegistryDsl
abstract class AbstractRegistryDslGroup<Type, Settings, Config : IDSLConfig<Settings>> {
    private val _instances: MutableList<RegistryEntry<Type>> = mutableListOf()
    var itemGroup: Group = MOD_GROUP

    abstract var settings: Settings

    val instances: List<RegistryEntry<Type>>
    get() = _instances

    fun ((Settings) -> Type).with(name: String, init: Config.() -> Unit) {
        val id = Identifier.of(MOD_ID, name)
        val config = createConfig().apply(init)
        addInstance(instantiate(this, id, config.settings ?: settings), id, config.itemGroup)
    }

    infix fun ((Settings) -> Type).with(name: String) {
        val id = Identifier.of(MOD_ID, name)
        addInstance(instantiate(this, id, settings), id)
    }

    infix fun Type.with(name: String) {
        addInstance(this, Identifier.of(MOD_ID, name))
    }

    private fun addInstance(instance: Type, id: Identifier, group: RegistryKey<ItemGroup>? = null) {
        _instances += RegistryEntry(instance, id, group ?: itemGroup)
    }

    abstract fun createConfig(): Config
    abstract fun instantiate(factory: (Settings) -> Type, id: Identifier, settings: Settings): Type
}