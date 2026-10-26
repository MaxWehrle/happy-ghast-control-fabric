package de.maxwehrle.happyghastcontrol.client;

import de.maxwehrle.happyghastcontrol.ParkingRequestPayload;
import de.maxwehrle.happyghastcontrol.ParkingStatusPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** A small settings screen for the particular Ghast the player is riding. */
public final class GhastControlScreen extends Screen {
    private final int ghastId;
    private Button parkingButton;

    public GhastControlScreen(int ghastId) {
        super(Component.translatable("screen.happyghastcontrol.title"));
        this.ghastId = ghastId;
    }

    public int ghastId() {
        return ghastId;
    }

    @Override
    protected void init() {
        int center = width / 2;
        int top = height / 2 - 30;
        parkingButton = addRenderableWidget(Button.builder(
                Component.translatable("screen.happyghastcontrol.loading"), button -> {
                    button.active = false;
                    ClientPlayNetworking.send(new ParkingRequestPayload(ghastId, true));
                }).bounds(center - 105, top + 20, 210, 20).build());
        parkingButton.active = false;

        addRenderableWidget(Button.builder(
                Component.translatable("screen.happyghastcontrol.done"), button ->
                        minecraft.gui.setScreen(null))
                .bounds(center - 105, top + 48, 210, 20).build());

        ClientPlayNetworking.send(new ParkingRequestPayload(ghastId, false));
    }

    public void receiveStatus(ParkingStatusPayload status) {
        if (status.entityId() != ghastId || parkingButton == null) {
            return;
        }
        parkingButton.setMessage(Component.translatable(status.enabled()
                ? "screen.happyghastcontrol.parking_on"
                : "screen.happyghastcontrol.parking_off"));
        parkingButton.active = true;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
            float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(font, title, width / 2, height / 2 - 72, 0xFFFFFFFF);
        graphics.centeredText(font,
                Component.translatable("screen.happyghastcontrol.description"),
                width / 2, height / 2 - 49, 0xFFBBBBBB);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
