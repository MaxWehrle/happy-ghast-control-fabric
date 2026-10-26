package de.maxwehrle.happyghastcontrol;

import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.phys.Vec3;

/** Stores each Ghast's parking preference in persistent entity tags. */
public final class ParkingState {
    private static final String PARK_ON_DISMOUNT = "happyghastcontrol.park_on_dismount";
    private static final String APPLIED_NO_AI = "happyghastcontrol.applied_no_ai";

    private ParkingState() {
    }

    public static boolean isEnabled(HappyGhast ghast) {
        return ghast.entityTags().contains(PARK_ON_DISMOUNT);
    }

    public static void setEnabled(HappyGhast ghast, boolean enabled) {
        if (enabled) {
            ghast.addTag(PARK_ON_DISMOUNT);
        } else {
            ghast.removeTag(PARK_ON_DISMOUNT);
        }
        update(ghast);
    }

    /** Called on mounting, dismounting, and server ticks after loading a Ghast. */
    public static void update(HappyGhast ghast) {
        if (ghast.level().isClientSide()) {
            return;
        }

        if (isEnabled(ghast) && !ghast.isVehicle()) {
            if (!ghast.isNoAi()) {
                ghast.setNoAi(true);
                ghast.addTag(APPLIED_NO_AI);
            }
            ghast.getNavigation().stop();
            ghast.setDeltaMovement(Vec3.ZERO);
        } else if (ghast.entityTags().contains(APPLIED_NO_AI)) {
            ghast.setNoAi(false);
            ghast.removeTag(APPLIED_NO_AI);
        }
    }
}
