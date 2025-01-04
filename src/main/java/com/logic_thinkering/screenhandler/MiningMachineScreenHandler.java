package com.logic_thinkering.screenhandler;

import com.logic_thinkering.block.entity.MiningMachineBlockEntity;
import com.logic_thinkering.network.BlockPosPayload;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class MiningMachineScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final MiningMachineBlockEntity blockEntity;

    public MiningMachineScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId,
                playerInventory,
                (MiningMachineBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }

    public MiningMachineScreenHandler(int syncId, PlayerInventory playerInventory, MiningMachineBlockEntity blockEntity) {
        super(ModScreenHandlers.MINING_MACHINE_SCREEN_HANDLER, syncId);
        this.inventory = blockEntity;
        this.blockEntity = blockEntity;
        inventory.onOpen(playerInventory.player);

        addInventoryGridSlots();
        addPlayerInventorySlots(playerInventory);
        addPlayerHotbarSlots(playerInventory);
    }

    private void addInventoryGridSlots() {
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 3; ++l) {
                this.addSlot(new Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18));
            }
        }
    }

    private void addPlayerInventorySlots(PlayerInventory playerInventory) {
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
    }

    private void addPlayerHotbarSlots(PlayerInventory playerInventory) {
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public MiningMachineBlockEntity getBlockEntity() {
        return this.blockEntity;
    }
}
