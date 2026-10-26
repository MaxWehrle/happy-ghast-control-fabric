package de.maxwehrle.happyghastcontrol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Requests the current state or toggles it for the Ghast being ridden. */
public record ParkingRequestPayload(int entityId, boolean toggle) implements CustomPacketPayload {
    public static final Type<ParkingRequestPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(HappyGhastControl.MOD_ID, "parking_request"));
    public static final StreamCodec<FriendlyByteBuf, ParkingRequestPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, ParkingRequestPayload::entityId,
                    ByteBufCodecs.BOOL, ParkingRequestPayload::toggle,
                    ParkingRequestPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
