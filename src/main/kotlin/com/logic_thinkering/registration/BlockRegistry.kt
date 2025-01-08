package com.logic_thinkering.registration

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey


data class BlockDSLConfig(
    override var factory: BlockFactory,
    override var itemGroup: RegistryKey<ItemGroup>,
    override var settings: AbstractBlock.Settings,
    override var name: String? = null
) : IDSLConfig<Block, AbstractBlock.Settings>

@RegistryDsl
class BlockRegistryDslGroup : AbstractRegistryDslGroup<Block, AbstractBlock.Settings, BlockDSLConfig>() {
    override var settings: AbstractBlock.Settings = AbstractBlock.Settings.create()

    override fun createConfig(factory: (AbstractBlock.Settings) -> Block, name: String?) =
        BlockDSLConfig(factory, itemGroup, settings, name)
}