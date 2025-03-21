package net.happyspeed.civilized_weapons.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.ClientPlayerClassAccess;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Environment(EnvType.CLIENT)
@Mixin(value = HeldItemRenderer.class, priority = 100)
public abstract class HeldItemRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "renderFirstPersonItem", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 8))
    private void unapplyEquipOffset(HeldItemRenderer instance, MatrixStack matrices, Arm arm, float equipProgress, @Local(ordinal = 0) Hand hand) {
        int i = arm == Arm.RIGHT ? 1 : -1;
        if (this.client.player != null) {
            Hand active = (((ClientPlayerClassAccess) this.client.player).civilized_weapons$getLastAttackHand());
            if (active != Hand.MAIN_HAND && CivilizedHelper.isDualWielding(this.client.player) && hand == Hand.MAIN_HAND) {
                matrices.translate((float) i * 0.56f, -0.52f + equipProgress * -0.01, -0.72f);
            }
            else if (active == Hand.MAIN_HAND && CivilizedHelper.isDualWielding(this.client.player) && hand == Hand.OFF_HAND) {
                matrices.translate((float) i * 0.56f, -0.52f + equipProgress * -0.01, -0.72f);
            }
            else  {
                matrices.translate((float) i * 0.56f, -0.52f + equipProgress * -0.6f, -0.72f);
            }
        }
    }
}
