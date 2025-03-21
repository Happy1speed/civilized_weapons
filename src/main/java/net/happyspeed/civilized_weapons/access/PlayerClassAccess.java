package net.happyspeed.civilized_weapons.access;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface PlayerClassAccess {
        float civilized_weapons$getPrevAttackCooldown();
        void civilized_weapons$swingingDetected(LivingEntity living);
        ItemStack civilized_weapons$getHeldItemLastTick();
        void civilized_weapons$setAttackBlockTimer();
        void civilized_weapons$setPlayerParryBlocking();
        int civilized_weapons$getTicksSinceLastItemSwap();
        int civilized_weapons$getTicksSinceHit();
        Hand civilized_weapons$getLastAttackHand();
        Hand civilized_weapons$getLastAttackHandInverse();
        void civilized_weapons$setLastAttackHand(Hand hand);
        void civilized_weapons$onSwapDualHands(Hand hand);
    }
