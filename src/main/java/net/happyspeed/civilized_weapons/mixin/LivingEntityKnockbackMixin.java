package net.happyspeed.civilized_weapons.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.happyspeed.civilized_weapons.item.custom.AdvancedWeaponTemplate;
import net.happyspeed.civilized_weapons.util.ModDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = LivingEntity.class, priority = 502)
abstract class LivingEntityKnockbackMixin extends Entity {

    protected LivingEntityKnockbackMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(method = "damage", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.takeKnockback (DDD)V"), index = 0)
    private double editBaseKnockBack(double strength, @Local Entity entity2, @Local(argsOnly = true) DamageSource source) {
        if (entity2 instanceof PlayerEntity player) {
            if (player.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate && (source.isOf(DamageTypes.PLAYER_ATTACK) || source.isOf(ModDamageTypes.SHIELD_BREACH_DAMAGE_TYPE)) && source.getAttacker() == player) {
                return Math.max(0.0f, advancedWeaponTemplate.weaponKnockbackMulti - 0.1f);
            }
            else if (player.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate && (source.isOf(ModDamageTypes.SLASH_DAMAGE_TYPE)) && source.getAttacker() == player) {
                return Math.max(0.0f, advancedWeaponTemplate.weaponSweepKnockback - 0.1f);
            }
            else {
                return 0.4f;
            }
        }
        return 0.4;
    }
}
