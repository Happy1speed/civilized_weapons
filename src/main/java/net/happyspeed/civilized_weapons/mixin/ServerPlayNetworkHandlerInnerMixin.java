package net.happyspeed.civilized_weapons.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.server.network.ServerPlayNetworkHandler$1")
public class ServerPlayNetworkHandlerInnerMixin {
//    @Redirect(method = "processInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;swingHand(Lnet/minecraft/util/Hand;Z)V"))
//    public void stackInHand(ServerPlayerEntity instance, Hand hand, boolean b) {
//        instance.setSneaking(true);
//    }
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    public ItemStack getStackInHandInner(ServerPlayerEntity player1, Hand hand) {
        var currentHand = ((PlayerClassAccess) player1).civilized_weapons$getLastAttackHand();
        if (currentHand == Hand.MAIN_HAND) {
            return player1.getStackInHand(Hand.MAIN_HAND);
        }
        return player1.getStackInHand(Hand.OFF_HAND);
    }
}
