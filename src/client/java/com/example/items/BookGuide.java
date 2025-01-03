package com.example.items;

import com.example.gui.Gui;
import com.example.gui.ScreenBook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.util.ActionResult;


public class BookGuide extends Item {

    public BookGuide(Item.Settings settings) {
        super(settings);
    }

   @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

       MinecraftClient.getInstance().setScreen(new ScreenBook(new Gui()));

        return super.use(world, user, hand);
    }
}
