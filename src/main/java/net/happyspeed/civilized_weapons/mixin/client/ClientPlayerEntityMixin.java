package net.happyspeed.civilized_weapons.mixin.client;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.happyspeed.civilized_weapons.access.ClientPlayerClassAccess;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
        extends AbstractClientPlayerEntity implements ClientPlayerClassAccess {

    @Unique
    public Hand lastAttackHand = Hand.MAIN_HAND;

    @Unique
    public boolean swapHandsLogicRan = false;


    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public void civilized_weapons$setLastAttackHand(Hand hand) {
        this.lastAttackHand = hand;
    }

    @Override
    public Hand civilized_weapons$getLastAttackHand() {
        return this.lastAttackHand;
    }

    @Override
    public void civilized_weapons$setSwapHandsLogicRan(boolean bool) {
        this.swapHandsLogicRan = bool;
    }

    @Override
    public boolean civilized_weapons$getSwapHandsLogicRan() {
        return this.swapHandsLogicRan;
    }

    @Override
    public void civilized_weapons$setPreferHand(Hand hand) {
        this.preferredHand = hand;
        this.activeItemStack = this.getStackInHand(hand);
    }

    @Inject(method = "tick", at=@At(value = "TAIL"))
    public void tailTickInjectClient(CallbackInfo ci) {
        this.swapHandsLogicRan = false;
    }
}
