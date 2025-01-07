package com.logic_thinkering.registration



interface AbstractRegistryConfig<Type, Settings, Group> {
    var factory: (Settings) -> Type
    var itemGroup: Group
    var settings: Settings
    var name: String?
}

abstract class AbstractRegistryGroup<Type, Settings, Config : AbstractRegistryConfig<Type, Settings, Group>, Group> {
    protected val _factories: MutableList<Pair<(Settings) -> Type, Config>> = mutableListOf()
    protected val _instances: MutableList<Triple<Type, String, Group>> = mutableListOf()

    abstract var itemGroup: Group
    abstract var settings: Settings

    val factories: List<Pair<(Settings) -> Type, Config>>
    get() = _factories

    val instances: List<Triple<Type, String, Group>>
    get() = _instances

    infix fun ((Settings) -> Type).with(init: Config.() -> Unit) {
        val configObject = createConfig(this)
        configObject.init()
        checkNotNull(configObject.name) { "Name must be specified." }
        _factories += this to configObject
    }

    infix fun ((Settings) -> Type).with(name: String) {
        _factories += this to createConfig(this, name)
    }

    infix fun Type.with(name: String) {
        _instances += Triple(this, name, itemGroup)
    }

    abstract fun createConfig(factory: (Settings) -> Type, name: String? = null): Config
}