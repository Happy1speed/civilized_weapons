package net.happyspeed.civilized_weapons.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.happyspeed.civilized_weapons.item.custom.AdvancedWeaponTemplate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PlayerEntity.class, priority = 500)
abstract class PlayerEntitySprintKnockbackMixin extends LivingEntity implements PlayerClassAccess {

    protected PlayerEntitySprintKnockbackMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 1))
    public float sprintKnockbackMultiplier(float constant) {
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            return advancedWeaponTemplate.weaponSprintKnockbackMultiplier;

        }
        else {
            return 0.5f;
        }
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 2))
    public float sprintKnockbackMultiplier2(float constant) {
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            return advancedWeaponTemplate.weaponSprintKnockbackMultiplier;

        }
        else {
            return 0.5f;
        }
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 3))
    public float sprintKnockbackMultiplier3(float constant) {
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            return advancedWeaponTemplate.weaponSprintKnockbackMultiplier;
        }
        else {
            return 0.5f;
        }
    }
}
