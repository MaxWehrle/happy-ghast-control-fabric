package de.maxwehrle.happyghastcontrol;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.player.Player;

/** Tracks held descend input independently on the client and the server. */
public final class DescentState {
    private static final Set<UUID> CLIENT_PLAYERS = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> SERVER_PLAYERS = ConcurrentHashMap.newKeySet();

    private DescentState() {
    }

    public static void set(Player player, boolean descending) {
        Set<UUID> players = player.level().isClientSide() ? CLIENT_PLAYERS : SERVER_PLAYERS;
        if (descending) {
            players.add(player.getUUID());
        } else {
            players.remove(player.getUUID());
        }
    }

    public static boolean isDescending(Player player) {
        Set<UUID> players = player.level().isClientSide() ? CLIENT_PLAYERS : SERVER_PLAYERS;
        return players.contains(player.getUUID());
    }
}
