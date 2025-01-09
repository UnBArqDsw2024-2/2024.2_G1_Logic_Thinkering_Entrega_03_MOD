package com.logic_thinkering.registration

import com.logic_thinkering.MOD_ID
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier


data class BlockDSLConfig(
    override var factory: BlockFactory,
    override var itemGroup: RegistryKey<ItemGroup>,
    override var settings: AbstractBlock.Settings,
    override var id: Identifier? = null
) : IDSLConfig<Block, AbstractBlock.Settings>

@RegistryDsl
class BlockRegistryDslGroup : AbstractRegistryDslGroup<Block, AbstractBlock.Settings, BlockDSLConfig>() {
    override var settings: AbstractBlock.Settings = AbstractBlock.Settings.create()
    override fun createInstance(
        factory: (AbstractBlock.Settings) -> Block,
        id: Identifier,
        settings: AbstractBlock.Settings
    ): Block {
        val registryKey = RegistryKey.of(RegistryKeys.BLOCK, id)
        settings.registryKey(registryKey)
        return factory(settings)
    }

    override fun createConfig(factory: (AbstractBlock.Settings) -> Block, name: String?) =
        BlockDSLConfig(factory, itemGroup, settings, Identifier.of(MOD_ID, name))
}