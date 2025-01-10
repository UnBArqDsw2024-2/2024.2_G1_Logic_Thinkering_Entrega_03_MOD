package com.logic_thinkering.registration

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier


data class BlockDSLConfig(
    override var itemGroup: RegistryKey<ItemGroup>,
    override var settings: AbstractBlock.Settings,
) : IDSLConfig<Block, AbstractBlock.Settings>

@RegistryDsl
class BlockRegistryDslGroup : AbstractRegistryDslGroup<Block, AbstractBlock.Settings, BlockDSLConfig>() {
    override var settings: AbstractBlock.Settings = AbstractBlock.Settings.create()
    override fun instantiate(
        factory: (AbstractBlock.Settings) -> Block,
        id: Identifier,
        settings: AbstractBlock.Settings
    ): Block {
        // TODO: talvez tenha que copiar as configurações para não afetar a mesma instância o tempo todo
        val registryKey = RegistryKey.of(RegistryKeys.BLOCK, id)
        settings.registryKey(registryKey)
        return factory(settings)
    }

    override fun createConfig() = BlockDSLConfig(itemGroup, settings)
}