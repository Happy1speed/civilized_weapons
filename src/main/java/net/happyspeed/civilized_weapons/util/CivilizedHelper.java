package net.happyspeed.civilized_weapons.util;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class CivilizedHelper {
    public static boolean isCriticalHit(PlayerEntity player, float critMin) {
        return player.getAttackCooldownProgress(0.5f) > critMin && player.fallDistance > 0.0f && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
    }
}
