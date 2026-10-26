package de.maxwehrle.happyghastcontrol;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;

public final class HappyGhastControl implements ModInitializer {
    public static final String MOD_ID = "happyghastcontrol";

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.serverboundPlay().register(DescendPayload.TYPE, DescendPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(
                ParkingRequestPayload.TYPE, ParkingRequestPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(
                ParkingStatusPayload.TYPE, ParkingStatusPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(DescendPayload.TYPE, (payload, context) -> {
            var player = context.player();
            boolean canDescend = player.getVehicle() instanceof HappyGhast ghast
                    && ghast.getFirstPassenger() == player;
            DescentState.set(player, payload.pressed() && canDescend);
        });

        ServerPlayNetworking.registerGlobalReceiver(ParkingRequestPayload.TYPE, (payload, context) -> {
            var player = context.player();
            // Never trust the entity ID in a client packet without checking the rider.
            if (!(player.getVehicle() instanceof HappyGhast ghast)
                    || ghast.getId() != payload.entityId()
                    || ghast.getFirstPassenger() != player) {
                return;
            }

            if (payload.toggle()) {
                ParkingState.setEnabled(ghast, !ParkingState.isEnabled(ghast));
            }
            if (ServerPlayNetworking.canSend(player, ParkingStatusPayload.TYPE)) {
                ServerPlayNetworking.send(player,
                        new ParkingStatusPayload(ghast.getId(), ParkingState.isEnabled(ghast)));
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                DescentState.set(handler.player, false));
    }
}
