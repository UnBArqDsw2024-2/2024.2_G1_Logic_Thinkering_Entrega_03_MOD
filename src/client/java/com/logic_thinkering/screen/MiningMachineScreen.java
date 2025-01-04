package com.logic_thinkering.screen;

import com.logic_thinkering.network.MiningStrategyPayload;
import com.logic_thinkering.screenhandler.MiningMachineScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class MiningMachineScreen extends HandledScreen<MiningMachineScreenHandler> {
    // A path to the gui texture. In this example we use the texture from the dispenser
    private String currentStrategy;
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/dispenser.png");
    // For versions before 1.21:
    // private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");

    public MiningMachineScreen(MiningMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        // TODO: Encontrar alguma forma de inicializar com a estratégia de fato sendo usada pela BlockEntity
        this.currentStrategy = "line";
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTexturedOverlay, TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;

        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonX = (width - buttonWidth) / 2;

        int buttonY = (height - backgroundHeight) / 2  - 30;

        this.addDrawableChild(
                ButtonWidget.builder(Text.of("Alterar estratégia de mineração"), (button) -> {
                            sendStrategyPacket();
                        })
                        .dimensions(buttonX, buttonY, buttonWidth, buttonHeight)
                        .build()
        );
    }

    private void sendStrategyPacket() {
        if (currentStrategy.equals("line")) {
            setCurrentStrategy("radius");
        } else if (currentStrategy.equals("radius")) {
            setCurrentStrategy("line");
        }
        ClientPlayNetworking.send(new MiningStrategyPayload(currentStrategy));
    }

    public void setCurrentStrategy(String currentStrategy) {
        this.currentStrategy = currentStrategy;
    }
}