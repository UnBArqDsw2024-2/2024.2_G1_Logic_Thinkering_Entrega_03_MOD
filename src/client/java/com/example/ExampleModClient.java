package com.example;

import com.nimbusds.oauth2.sdk.id.Identifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import com.example.items.BookGuide;

public class ExampleModClient implements ClientModInitializer {

	public static final Item GUIDE_BOOK = new BookGuide(new Item.Settings());

	@Override
	public void onInitializeClient() {
		Registry.register(Registries.ITEM, new Identifier("logicthinkering", "book_guide"), GUIDE_BOOK);
		ItemGroupEvents.modifyEntriesEvent(ModItemGroup.LOGICTHINKERING_GROUP).register(entries -> {
			entries.add(GUIDE_BOOK);
		});
	}
}
