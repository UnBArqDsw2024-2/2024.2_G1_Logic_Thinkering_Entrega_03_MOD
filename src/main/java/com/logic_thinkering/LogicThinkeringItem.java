package com.logic_thinkering;

import com.logic_thinkering.itens.*;
import net.minecraft.item.equipment.EquipmentModels;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class LogicThinkeringItem {
    public List<ConcreteItem> items = new ArrayList<>();
    public List<ConcreteArmor> armors = new ArrayList<>();
    public List<ConcreteTool> tools = new ArrayList<>();

    public List<LogicThinkeringArmorMaterial> armorMaterials = new ArrayList<>();
    public List<LogicThinkeringToolMaterial> toolMaterials = new ArrayList<>();

    public LogicThinkeringItem() {
        items.add(new ConcreteItem("reinforced_copper_ingot"));
        insertCommonItem("reinforced_emerald_shard");
        insertCommonItem("reinforced_amethyst_shard");

        armorMaterials.add(new LogicThinkeringArmorMaterial("REINFORCED_COPPER", 37, new int[]{3, 6, 8, 3, 11}, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ItemTags.REPAIRS_GOLD_ARMOR, EquipmentModels.NETHERITE));
        armorMaterials.add(new LogicThinkeringArmorMaterial("REINFORCED_EMERALD", 37, new int[]{3, 6, 8, 3, 11}, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ItemTags.REPAIRS_GOLD_ARMOR, EquipmentModels.NETHERITE));
        armorMaterials.add(new LogicThinkeringArmorMaterial("REINFORCED_AMETHYST", 37, new int[]{3, 6, 8, 3, 11}, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ItemTags.REPAIRS_GOLD_ARMOR, EquipmentModels.NETHERITE));

        toolMaterials.add(new LogicThinkeringToolMaterial("REINFORCED_COPPER", BlockTags.INCORRECT_FOR_STONE_TOOL, 200, 6.5F, 2.0F, 14, ItemTags.STONE_TOOL_MATERIALS));
        toolMaterials.add(new LogicThinkeringToolMaterial("REINFORCED_EMERALD", BlockTags.INCORRECT_FOR_STONE_TOOL, 250, 6.5F, 3.0F, 14, ItemTags.STONE_TOOL_MATERIALS));
        toolMaterials.add(new LogicThinkeringToolMaterial("REINFORCED_AMETHYST", BlockTags.INCORRECT_FOR_STONE_TOOL, 500, 6.5F, 5.0F, 14, ItemTags.STONE_TOOL_MATERIALS));

        insertAllArmors();
        insertAllTools();
    }

    private void insertCommonItem(String id){
        ConcreteItem clone = items.getFirst().clone();
        clone.register(id);
        items.add(clone);
    }

    private void insertAllArmors() {
        for (ArmorType armorType : ArmorType.values()) {
            ConcreteArmor prototype = new ConcreteArmor(armorMaterials.getFirst().getName().toLowerCase() + "_" + armorType.name().toLowerCase(), armorType, armorMaterials.getFirst());
            armors.add(prototype);

            for (int i = 1; i < armorMaterials.size(); i++) {
                ConcreteArmor clone = prototype.clone();
                clone.updateMaterial(armorMaterials.get(i));
                clone.register(armorMaterials.get(i).getName().toLowerCase() + "_" + armorType.name().toLowerCase());
                armors.add(clone);
            }
        }
    }

    private void insertAllTools() {
        for (ToolType toolType : ToolType.values()) {
            ConcreteTool prototype = new ConcreteTool(toolMaterials.getFirst().getName().toLowerCase() + "_" + toolType.name().toLowerCase(), toolType, toolMaterials.getFirst());
            tools.add(prototype);

            for (int i = 1; i < toolMaterials.size(); i++) {
                ConcreteTool clone = prototype.clone();
                clone.updateMaterial(toolMaterials.get(i));
                clone.register(toolMaterials.get(i).getName().toLowerCase() + "_" + toolType.name().toLowerCase());
                tools.add(clone);
            }
        }
    }

}
