package net.happyspeed.civilized_weapons.item.custom;

import net.happyspeed.civilized_weapons.config.UniversalVars;
import net.happyspeed.civilized_weapons.item.ModToolMaterial;
import net.happyspeed.civilized_weapons.sounds.ModSounds;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//            if (!attacker.isSprinting() && attacker.fallDistance > 0 && attacker.isSneaking()) {
//                target.setVelocity(new Vec3d(target.getVelocity().x * 0.45, 0.5, target.getVelocity().z * 0.45));
//                attacker.setVelocity(new Vec3d(attacker.getVelocity().x, 0.5, attacker.getVelocity().z));
//                target.velocityModified = true;
//                attacker.velocityModified = true;
//                attacker.fallDistance = 0.0F;
//            }
//            else {
//                target.takeKnockback(0.6, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
//                target.velocityModified = true;
//            }

//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        Random random = new Random();
//        float pitch = random.ints(60, 100).findFirst().getAsInt();
//        pitch = pitch / 100;
//        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1,pitch);
//        user.swingHand(hand, true);
//        if (user.fallDistance > 5.0F) {
//            user.fallDistance -= 5.0F;
//        }
//        else {
//            user.fallDistance = 0.0F;
//        }
//        user.getItemCooldownManager().set(ModItems.CAST_IRON_PAN, 400);
//        return TypedActionResult.pass(user.getStackInHand(hand));
//    }

public class PanItemTemplate extends AdvancedWeaponTemplate {

    public PanItemTemplate(Settings settings) {
        super(ModToolMaterial.CAST_IRON,2,-2.0F, 1.3f, 0.8f,true,
                0.0f, 0.0f, 3.0f, 0.0f, false, true,
                true,true, ModSounds.HEAVYTHICKSWOOSHSOUND ,0.0f,0.0f, 0.5f, -0.2f, settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            this.playRandomPitchSound(ModSounds.PANHITSOUND, attacker, (float) UniversalVars.SWINGSOUNDSVOLUME, 60, 100);
            target.addVelocity(new Vec3d(target.getVelocity().getX(), 0.2 * this.prevAttackProgress, target.getVelocity().getZ()));
            target.velocityModified = true;
        }
        return true;
    }
    @Override
    public void activeHit(LivingEntity living) {
        if (living instanceof PlayerEntity player && !living.getWorld().isClient()) {
            if (this.hasSwingSFX) {
                if (player.getAttackCooldownProgress(1.0f) > 0.3) {
                    this.swingSoundEvent(living);
                }
            }
        }
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.Cast_iron_pan.tooltip").formatted(Formatting.DARK_GRAY));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.TwoHanded.tooltip").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, 0.4f, 80, 100);
    }

}
