package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import com.example.items.BookGuide;
import org.intellij.lang.annotations.Identifier;


public class ExampleModClient implements ClientModInitializer {

	public static final Item GUIDE_BOOK = new BookGuide(new Item.Settings());

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		Registry.register(Registries.ITEM, "book_guide", GUIDE_BOOK);
	}
}