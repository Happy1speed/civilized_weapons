package net.happyspeed.civilized_weapons.util;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Unique;

public class CivilizedHelper {
    //Sometimes this doesn't work since cooldown progress is already reset to 0
    public static boolean isCriticalHit(PlayerEntity player, float critMin) {
        return player.getAttackCooldownProgress(0.5f) >= critMin && player.fallDistance > 0.0f && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
    }

    public static boolean isDualWielding(PlayerEntity player) {
        var mainAttributes = player.getMainHandStack().getItem();
        var offAttributes = player.getOffHandStack().getItem();

        return mainAttributes == offAttributes && (!player.getMainHandStack().isEmpty() && !player.getOffHandStack().isEmpty());
    }
}
