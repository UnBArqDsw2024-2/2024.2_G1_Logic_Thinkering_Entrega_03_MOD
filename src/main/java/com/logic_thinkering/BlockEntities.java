package com.logic_thinkering;

import com.logic_thinkering.block.entity.MiningMachineBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntities {

    public static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Main.MOD_ID, id), type);
    }

    public static final BlockEntityType<MiningMachineBlockEntity> ENTIDADE_BLOCO_MINERACAO = register(
            "bloco_mineracao",
            FabricBlockEntityTypeBuilder.create(MiningMachineBlockEntity::new, Blocos.BLOCO_MINERACAO).build()
    );

    public static void inicializa() {}
}
