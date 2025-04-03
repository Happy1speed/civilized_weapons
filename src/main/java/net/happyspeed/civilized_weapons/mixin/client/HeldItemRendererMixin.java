package net.happyspeed.civilized_weapons.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.happyspeed.civilized_weapons.access.ClientPlayerClassAccess;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {


    private int counter = 0;

    @Shadow
    protected abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta,
                                                  float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices,
                                                  VertexConsumerProvider vertexConsumers, int light);

    @Shadow private float prevEquipProgressMainHand;

    @Shadow private float equipProgressMainHand;

    @Shadow private float prevEquipProgressOffHand;

    @Shadow private float equipProgressOffHand;

    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    /**
     * Render dual wield swings in first person for Dual Wieldable weapons.
     */


    private void applyEquipOffsetwithoutstuff(MatrixStack matrices, Arm arm, float equipProgress) {
        int i = arm == Arm.RIGHT ? 1 : -1;
        matrices.translate((float) i * 0.56f, -0.52, -0.72f);
        //0.48 in place of equipProcess and multiplier?
    }

    @Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 8))
    private void duelEquip1(HeldItemRenderer instance, MatrixStack matrices, Arm arm, float equipProgress, @Local(ordinal = 0) AbstractClientPlayerEntity player, @Local(ordinal = 2) float swingprogress, @Local(ordinal = 0) float tickDelta, @Local(ordinal = 3) float realEquipProgress) {
        if (CivilizedHelper.isDualWielding(player)) {
            if (arm == Arm.RIGHT) {
                if (((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == Hand.OFF_HAND) {
                    equipProgressOffHand = equipProgressMainHand;
                    if (swingprogress > 0 || equipProgress > 0) {
                        applyEquipOffsetwithoutstuff(matrices, arm, equipProgress);
                        return;
                    }
                }
            }
            if (arm == Arm.LEFT) {
                if (((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == Hand.OFF_HAND) {
                    if (swingprogress > 0 || equipProgress > 0) {
                        applyEquipOffset(matrices, arm, equipProgress);
                        return;
                    }
                }
            }
        }
        applyEquipOffset(matrices, arm, equipProgress);
    }

    @Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applySwingOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 1))
    private void duelEquip2(HeldItemRenderer instance, MatrixStack matrices, Arm arm, float equipProgress, @Local(ordinal = 0) AbstractClientPlayerEntity player) {
        applySwingOffset(matrices, arm, equipProgress);
    }


    @Inject(method = "renderFirstPersonItem", at = @At(value = "HEAD"), cancellable = true)
    public void renderOffArm(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        swingProgress = player.getHandSwingProgress(tickDelta);
        if (CivilizedHelper.isDualWielding(player)) {
            if (((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == Hand.MAIN_HAND) {
                equipProgress = 1.0F - MathHelper.lerp(tickDelta, this.prevEquipProgressMainHand, this.equipProgressMainHand);
            } else {
                equipProgress = 1.0F - MathHelper.lerp(tickDelta, this.prevEquipProgressOffHand, this.equipProgressOffHand);
            }
            if (hand == Hand.OFF_HAND && ((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == hand) {
                if ((swingProgress > 0 || equipProgress > 0)
                        && CivilizedHelper.isDualWielding(player)) {

                    if (this.counter == 0) {
                        this.counter++;
                        this.renderFirstPersonItem(player, tickDelta, pitch, hand, swingProgress, item,
                                equipProgress, matrices, vertexConsumers, light);
                        ci.cancel();
                    } else {
                        this.counter = 0;
                    }
                }

            }

            if (hand == Hand.MAIN_HAND && ((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand() == hand) {
                equipProgress = 1.0F - MathHelper.lerp(tickDelta, this.prevEquipProgressMainHand, this.equipProgressMainHand);
                if ((swingProgress > 0 || equipProgress > 0)
                        && CivilizedHelper.isDualWielding(player)) {

                    if (this.counter == 0) {
                        this.counter++;

                        this.renderFirstPersonItem(player, tickDelta, pitch, hand, swingProgress, item,
                                equipProgress, matrices, vertexConsumers, light);
                        ci.cancel();
                    } else {
                        this.counter = 0;
                    }
                }
            }
        }
    }
}
