package de.maxwehrle.happyghastcontrol.mixin;

import de.maxwehrle.happyghastcontrol.DescentState;
import de.maxwehrle.happyghastcontrol.ParkingState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HappyGhast.class)
public abstract class HappyGhastMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void happyghastcontrol$keepParked(CallbackInfo callback) {
        ParkingState.update((HappyGhast) (Object) this);
    }

    @Inject(method = "addPassenger", at = @At("TAIL"))
    private void happyghastcontrol$unparkWhileRidden(Entity passenger, CallbackInfo callback) {
        ParkingState.update((HappyGhast) (Object) this);
    }

    @Inject(method = "getRiddenInput", at = @At("RETURN"), cancellable = true)
    private void happyghastcontrol$descend(Player rider, Vec3 input,
            CallbackInfoReturnable<Vec3> callback) {
        HappyGhast ghast = (HappyGhast) (Object) this;
        if (!DescentState.isDescending(rider)) {
            return;
        }

        // Vanilla adds +0.5 before multiplying by 3.9 * flying speed for jump.
        // Apply the symmetric negative input after vanilla has computed steering.
        double descendSpeed = 0.5D * 3.9D * ghast.getAttributeValue(Attributes.FLYING_SPEED);
        callback.setReturnValue(callback.getReturnValue().add(0.0D, -descendSpeed, 0.0D));
    }

    @Inject(method = "removePassenger", at = @At("TAIL"))
    private void happyghastcontrol$stopOnDismount(Entity passenger, CallbackInfo callback) {
        HappyGhast ghast = (HappyGhast) (Object) this;
        if (passenger instanceof Player player) {
            DescentState.set(player, false);
        }
        if (!ghast.isVehicle()) {
            ghast.setDeltaMovement(Vec3.ZERO);
            if (!ghast.level().isClientSide()) {
                ghast.getNavigation().stop();
            }
        }
        ParkingState.update(ghast);
    }
}
