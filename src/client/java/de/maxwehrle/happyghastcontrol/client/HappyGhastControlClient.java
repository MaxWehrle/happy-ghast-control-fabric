package de.maxwehrle.happyghastcontrol.client;

import com.mojang.blaze3d.platform.InputConstants;
import de.maxwehrle.happyghastcontrol.DescendPayload;
import de.maxwehrle.happyghastcontrol.DescentState;
import de.maxwehrle.happyghastcontrol.HappyGhastControl;
import de.maxwehrle.happyghastcontrol.ParkingRequestPayload;
import de.maxwehrle.happyghastcontrol.ParkingStatusPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;

public final class HappyGhastControlClient implements ClientModInitializer {
    private static boolean lastSent;
    private static Player trackedPlayer;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(HappyGhastControl.MOD_ID, "flight"));
        KeyMapping descendKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.happyghastcontrol.descend",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_G,
                category));
        KeyMapping menuKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.happyghastcontrol.menu",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_H,
                category));

        ClientPlayNetworking.registerGlobalReceiver(ParkingStatusPayload.TYPE, (status, context) -> {
            if (context.client().gui.screen() instanceof GhastControlScreen screen) {
                screen.receiveStatus(status);
            }
        });

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(HappyGhastControl.MOD_ID, "menu_hint"),
                (graphics, delta) -> {
                    Minecraft client = Minecraft.getInstance();
                    if (client.player == null || client.gui.screen() != null
                            || client.gui.hud.isHidden()
                            || !ClientPlayNetworking.canSend(ParkingRequestPayload.TYPE)
                            || !(client.player.getVehicle() instanceof HappyGhast ghast)
                            || ghast.getFirstPassenger() != client.player) {
                        return;
                    }

                    Component hint = Component.translatable(
                            "hud.happyghastcontrol.menu_hint", menuKey.getTranslatedKeyMessage());
                    int x = (graphics.guiWidth() - client.font.width(hint)) / 2;
                    int y = graphics.guiHeight() - 67;
                    graphics.fill(x - 5, y - 4, x + client.font.width(hint) + 5,
                            y + client.font.lineHeight + 4, 0x88000000);
                    graphics.text(client.font, hint, x, y, 0xFFFFFFFF, true);
                });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != trackedPlayer) {
                if (trackedPlayer != null) {
                    DescentState.set(trackedPlayer, false);
                }
                trackedPlayer = client.player;
                lastSent = false;
            }
            if (client.player == null) {
                return;
            }

            if (client.gui.screen() instanceof GhastControlScreen screen
                    && (!(client.player.getVehicle() instanceof HappyGhast ghast)
                            || ghast.getId() != screen.ghastId()
                            || ghast.getFirstPassenger() != client.player)) {
                client.gui.setScreen(null);
            }

            boolean openMenu = menuKey.consumeClick();
            if (openMenu && client.gui.screen() == null
                    && client.player.getVehicle() instanceof HappyGhast ghast
                    && ghast.getFirstPassenger() == client.player
                    && ClientPlayNetworking.canSend(ParkingRequestPayload.TYPE)) {
                client.gui.setScreen(new GhastControlScreen(ghast.getId()));
            }

            boolean descending = client.mouseHandler.isMouseGrabbed()
                    && client.player.getVehicle() instanceof HappyGhast ghast
                    && ghast.getFirstPassenger() == client.player
                    && descendKey.isDown()
                    && ClientPlayNetworking.canSend(DescendPayload.TYPE);
            DescentState.set(client.player, descending);
            if (descending != lastSent) {
                ClientPlayNetworking.send(new DescendPayload(descending));
                lastSent = descending;
            }
        });
    }
}
