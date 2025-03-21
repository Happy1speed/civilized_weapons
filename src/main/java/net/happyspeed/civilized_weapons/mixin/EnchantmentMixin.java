package net.happyspeed.civilized_weapons.mixin;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    // FEATURE: Dual wielding

    @Inject(method = "getEquipment", at = @At("RETURN"), cancellable = true)
    private void getEquipmentFix(LivingEntity entity, CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        if(entity instanceof PlayerEntity player) {
            var currentHand = ((PlayerClassAccess) player).civilized_weapons$getLastAttackHand();
            var map = cir.getReturnValue();

            if (CivilizedHelper.isDualWielding(player)) {
                if (currentHand == Hand.MAIN_HAND) {
                    if (map.get(EquipmentSlot.OFFHAND) != null) {
                        map.remove(EquipmentSlot.OFFHAND);
                    }
                    var mainHandStack = player.getMainHandStack();
                    if (!mainHandStack.isEmpty()) {
                        map.put(EquipmentSlot.MAINHAND, mainHandStack);
                    }
                    cir.setReturnValue(map);

                } else {
                    if (map.get(EquipmentSlot.MAINHAND) != null) {
                        map.remove(EquipmentSlot.MAINHAND);
                    }
                    var offHandStack = player.getOffHandStack();
                    if (!offHandStack.isEmpty()) {
                        map.put(EquipmentSlot.OFFHAND, offHandStack);
                    }
                    cir.setReturnValue(map);

                }
            }
            else {
                if (map.get(EquipmentSlot.OFFHAND) != null) {
                    map.remove(EquipmentSlot.OFFHAND);
                }
                var mainHandStack = player.getMainHandStack();
                if (!mainHandStack.isEmpty()) {
                    map.put(EquipmentSlot.MAINHAND, mainHandStack);
                }
                cir.setReturnValue(map);
            }
        }
    }
}
