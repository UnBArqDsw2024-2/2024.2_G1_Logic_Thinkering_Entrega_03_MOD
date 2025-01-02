package com.example;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ConcreteRegisterItem implements StrategyRegister {

    @Override
    public Item register(String id) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ExampleMod.MOD_ID, id));
        Function<Item.Settings, Item> factory = Item::new;
        Item item = factory.apply(new Item.Settings().registryKey(key));
        if (item instanceof BlockItem blockItem) blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        return(Registry.register(Registries.ITEM, key, item));
    }

}