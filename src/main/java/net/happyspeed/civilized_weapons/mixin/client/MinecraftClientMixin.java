package net.happyspeed.civilized_weapons.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.ClientPlayerClassAccess;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.happyspeed.civilized_weapons.network.PlayerAttackPacket;
import net.happyspeed.civilized_weapons.network.PlayerHandSwapPacket;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(value = MinecraftClient.class, priority = 100)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;


    @Inject(method = "doAttack", at = @At(value = "HEAD"))
    public void attackHeadClient(CallbackInfoReturnable<Boolean> cir) {
        swapHandsClient();
    }

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V", shift = At.Shift.BEFORE))
    private void doAttackMissMixin(CallbackInfoReturnable<Boolean> cir) {
        if ( this.player != null) {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(PlayerAttackPacket.attackPacket(this.player));
        }
    }

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
    private void doBlockHitMixin(CallbackInfoReturnable<Boolean> cir) {
        if ( this.player != null) {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(PlayerAttackPacket.attackPacket(this.player));
        }
    }

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V"))
    private void doAttackHitMixin(CallbackInfoReturnable<Boolean> cir) {
        if ( this.player != null) {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(PlayerAttackPacket.attackPacket(this.player));
        }
    }

    @Redirect(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
    public void swingOtherHand(ClientPlayerEntity instance, Hand hand) {
        if (this.player != null) {
            Hand hand1 = ((ClientPlayerClassAccess) player).civilized_weapons$getLastAttackHand();
            this.player.swingHand(hand1);
        }
    }

    @Unique
    public void swapHandsClient() {
        if (this.player != null) {
            boolean hasSwapped = ((ClientPlayerClassAccess) this.player).civilized_weapons$getSwapHandsLogicRan();
            if (CivilizedHelper.isDualWielding(this.player)) {
                if (!hasSwapped) {
                    if (((ClientPlayerClassAccess) this.player).civilized_weapons$getLastAttackHand() == Hand.MAIN_HAND) {
                        ((ClientPlayerClassAccess) this.player).civilized_weapons$setLastAttackHand(Hand.OFF_HAND);
                        ((ClientPlayerClassAccess) this.player).civilized_weapons$setPreferHand(Hand.OFF_HAND);
                    } else {
                        ((ClientPlayerClassAccess) this.player).civilized_weapons$setLastAttackHand(Hand.MAIN_HAND);
                        ((ClientPlayerClassAccess) this.player).civilized_weapons$setPreferHand(Hand.MAIN_HAND);
                    }
                    ((ClientPlayerClassAccess) this.player).civilized_weapons$setSwapHandsLogicRan(true);
                }

            } else {
                ((ClientPlayerClassAccess) this.player).civilized_weapons$setLastAttackHand(Hand.MAIN_HAND);
                ((ClientPlayerClassAccess) this.player).civilized_weapons$setPreferHand(Hand.MAIN_HAND);
            }
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(PlayerHandSwapPacket.swapPacket(this.player, ((ClientPlayerClassAccess) this.player).civilized_weapons$getLastAttackHand() == Hand.MAIN_HAND));
        }
    }

}
