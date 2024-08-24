package net.happyspeed.civilized_weapons.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.happyspeed.civilized_weapons.item.custom.AdvancedWeaponTemplate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = PlayerEntity.class, priority = 502)
abstract class PlayerEntityKnockbackMixin extends LivingEntity implements PlayerClassAccess {

    protected PlayerEntityKnockbackMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 0.5f, ordinal = 1))
    public float modifyEntityKnockback(float constant) {
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            return advancedWeaponTemplate.weaponKnockbackMulti;
        }
        else {
            return 0.5f;
        }
    }
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 0.5f, ordinal = 2))
    public float modifyEntityKnockback2(float constant) {
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            return advancedWeaponTemplate.weaponKnockbackMulti;
        }
        else {
            return 0.5f;
        }
    }
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 0.5f, ordinal = 3))
    public float modifyEntityKnockback3(float constant) {
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            return advancedWeaponTemplate.weaponKnockbackMulti;
        }
        else {
            return 0.5f;
        }
    }
}
