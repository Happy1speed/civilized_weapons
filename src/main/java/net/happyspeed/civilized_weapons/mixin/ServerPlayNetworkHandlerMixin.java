package net.happyspeed.civilized_weapons.mixin;

import net.happyspeed.civilized_weapons.item.custom.AdvancedWeaponTemplate;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Shadow public abstract ServerPlayerEntity getPlayer();

    @Redirect(method = "onPlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    public ItemStack getStackInHand(ServerPlayerEntity player1, Hand hand) {
        ItemStack result = null;
        var player = player1;
        if (hand == Hand.MAIN_HAND) {
            result = ((PlayerEntityAccessor)player).getInventory().getMainHandStack();
        }
        else if (hand == Hand.OFF_HAND) {
            result = ((PlayerEntityAccessor)player).getInventory().offHand.get(0);
        }
        return result;
    }
}
