package com.logic_thinkering;

import com.logic_thinkering.block.miningmachine.MiningMachineBlock;
import com.logic_thinkering.block.ClockEnergy;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

public class Blocos {

    public static final Block CLOCK = RegisterBlock.registrarBloco(
            "clock",
            ClockEnergy::new,
            AbstractBlock.Settings.create().strength(4f).requiresTool()
    );

    public static final Block BLOCO_MINERACAO = RegisterBlock.registrarBloco(
            "bloco_mineracao",
            MiningMachineBlock::new,
            AbstractBlock.Settings.create().strength(4f).requiresTool()
    );

    public static final Block MATRIZ_FIOS = RegisterBlock.registrarBloco(
            "matriz_fios",
            Block::new,
            AbstractBlock.Settings.create().strength(4f).requiresTool()
    );

    public static void inicializa() {
        ItemGroupEvents.modifyEntriesEvent(LogicThinkeringItemGroup.LOGICTHINKERING_GROUP).register(entries -> {
            entries.add(Blocos.CLOCK);
            entries.add(Blocos.BLOCO_MINERACAO);
            entries.add(Blocos.MATRIZ_FIOS);
        });
    }
}