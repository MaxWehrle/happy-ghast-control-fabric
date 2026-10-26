package de.maxwehrle.happyghastcontrol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DescendPayload(boolean pressed) implements CustomPacketPayload {
    public static final Type<DescendPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(HappyGhastControl.MOD_ID, "descend"));
    public static final StreamCodec<FriendlyByteBuf, DescendPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, DescendPayload::pressed, DescendPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
