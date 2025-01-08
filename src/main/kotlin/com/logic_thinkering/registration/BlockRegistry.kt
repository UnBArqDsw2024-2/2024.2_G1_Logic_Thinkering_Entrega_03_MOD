package com.logic_thinkering.registration

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey


data class BlockBuilderConfig(
    override var factory: BlockFactory,
    override var itemGroup: RegistryKey<ItemGroup>?,
    override var settings: AbstractBlock.Settings,
    override var name: String? = null
) : AbstractRegistryConfig<Block, AbstractBlock.Settings, RegistryKey<ItemGroup>?>

@RegistryDsl
class BlockRegistryGroup : AbstractRegistryGroup<Block, AbstractBlock.Settings, BlockBuilderConfig, RegistryKey<ItemGroup>?>() {
    override var settings: AbstractBlock.Settings = AbstractBlock.Settings.create()
    override var itemGroup: RegistryKey<ItemGroup>? = null

    override fun createConfig(factory: (AbstractBlock.Settings) -> Block, name: String?) =
        BlockBuilderConfig(factory, itemGroup, settings, name)
}