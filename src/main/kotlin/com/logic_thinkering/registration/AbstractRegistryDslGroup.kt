package com.logic_thinkering.registration

import com.logic_thinkering.MOD_GROUP
import com.logic_thinkering.MOD_ID
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

typealias Group = RegistryKey<ItemGroup>

interface IDSLConfig<Type, Settings> {
    var factory: (Settings) -> Type
    var itemGroup: Group
    var settings: Settings
    var id: Identifier?

    fun name(name: String) {
        id = Identifier.of(MOD_ID, name)
    }
}

abstract class AbstractRegistryDslGroup<Type, Settings, Config : IDSLConfig<Type, Settings>> {
    private val _instances: MutableList<Triple<Type, Identifier, Group>> = mutableListOf()
    var itemGroup: Group = MOD_GROUP

    abstract var settings: Settings


    val instances: List<Triple<Type, Identifier, Group>>
    get() = _instances

    infix fun ((Settings) -> Type).with(init: Config.() -> Unit) {
        val configObject = createConfig(this)
        configObject.init()
        checkNotNull(configObject.id) { "Name must be specified." }
        _instances += Triple(createInstance(this, configObject.id!!, configObject.settings), configObject.id!!, itemGroup)
    }

    infix fun ((Settings) -> Type).with(name: String) {
        val id = Identifier.of(MOD_ID, name)
        _instances += Triple(createInstance(this, id, settings), id,  itemGroup)
    }

    infix fun Type.with(name: String) {
        _instances += Triple(this, Identifier.of(MOD_ID, name), itemGroup)
    }

    abstract fun createConfig(factory: (Settings) -> Type, name: String? = null): Config
    abstract fun createInstance(factory: (Settings) -> Type, id: Identifier, settings: Settings): Type
}