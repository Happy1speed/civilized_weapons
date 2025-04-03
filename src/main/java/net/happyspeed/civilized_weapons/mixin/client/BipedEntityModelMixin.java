package net.happyspeed.civilized_weapons.mixin.client;

import net.happyspeed.civilized_weapons.access.ClientPlayerClassAccess;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {

    private int counter = 0;
    private boolean firstInvocation = true;

    @Shadow
    protected abstract void animateArms(T entity, float animationProgress);

    @Shadow protected abstract Arm getPreferredArm(T entity);

    /**
     * Hijack this method_29353() that renders the arm swings in Third person.  Render both arm swings if the player is
     * holding 2 weapons with Dual Wielding.
     */
//    @Inject(method = "animateArms", at = @At(value = "HEAD"), cancellable = true)
//    public void renderThirdPersonModelForBothArms(T livingEntity, float f, CallbackInfo ci) {
//        if (firstInvocation && shouldRenderAternateArmSwing(livingEntity)) {
//            firstInvocation = false;
//            animateArms(livingEntity, f);
//            animateArms(livingEntity, f);
//            firstInvocation = true;
//            ci.cancel();
//        }
//    }

    @Redirect(method = "animateArms", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;getPreferredArm(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/Arm;"))
    public Arm overrideArm(BipedEntityModel bipedEntityModel, T entity) {
        if (entity instanceof PlayerEntity player) {
            if (shouldRenderAternateArmSwing(entity)) {
                if (this.counter == 0) {
                    this.counter++;
                    if (((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == Hand.OFF_HAND && this.getPreferredArm(entity) == Arm.RIGHT)
                        return Arm.LEFT;
                    else if (((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == Hand.OFF_HAND && this.getPreferredArm(entity) == Arm.LEFT)
                        return Arm.RIGHT;
                    else if (((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == Hand.MAIN_HAND && this.getPreferredArm(entity) == Arm.RIGHT)
                        return Arm.RIGHT;
                    else if (((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == Hand.MAIN_HAND && this.getPreferredArm(entity) == Arm.LEFT)
                        return Arm.LEFT;
                } else {
                    this.counter = 0;
                }
            } else {
                return this.getPreferredArm(entity);
            }
        }
        return this.getPreferredArm(entity);
    }

    // Only render both arms swings if this is for a player and we're swinging
    private boolean shouldRenderAternateArmSwing(T entity) {
        if (entity instanceof PlayerEntity player && !(((BipedEntityModel)(Object)this).handSwingProgress <= 0.0F)) {
            return CivilizedHelper.isDualWielding(player);
        }

        return false;
    }
}
