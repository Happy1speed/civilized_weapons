package net.happyspeed.civilized_weapons.access;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface ClientPlayerClassAccess {
    void civilized_weapons$setLastAttackHand(Hand hand);
    Hand civilized_weapons$getLastAttackHand();
    void civilized_weapons$setSwapHandsLogicRan(boolean bool);
    boolean civilized_weapons$getSwapHandsLogicRan();
    void civilized_weapons$setPreferHand(Hand hand);
}
