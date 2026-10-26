package de.maxwehrle.happyghastcontrol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Carries the authoritative, per-Ghast parking state to its rider. */
public record ParkingStatusPayload(int entityId, boolean enabled) implements CustomPacketPayload {
    public static final Type<ParkingStatusPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(HappyGhastControl.MOD_ID, "parking_status"));
    public static final StreamCodec<FriendlyByteBuf, ParkingStatusPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, ParkingStatusPayload::entityId,
                    ByteBufCodecs.BOOL, ParkingStatusPayload::enabled,
                    ParkingStatusPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
