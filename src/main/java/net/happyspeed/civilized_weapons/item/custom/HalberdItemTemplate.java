package net.happyspeed.civilized_weapons.item.custom;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.config.UniversalVars;
import net.happyspeed.civilized_weapons.enchantments.ModEnchantments;
import net.happyspeed.civilized_weapons.sounds.ModSounds;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.happyspeed.civilized_weapons.util.ModDamageTypes;
import net.happyspeed.civilized_weapons.util.ModTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


//you can add Armor Piercing damage with this code
//target.damage(ModDamageTypes.of(target.getWorld(), ModDamageTypes.AP_DAMAGE_TYPE), 3.0f);

//check for enchantment on item
//EnchantmentHelper.getLevel(CivilizedWeaponsMod.DUMMY, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0

public class HalberdItemTemplate extends AdvancedWeaponTemplate {
    public HalberdItemTemplate(ToolMaterial material, float attackDamage, Item.Settings settings) {
        super(material,attackDamage,-3.2f,1.6f,0.6f,false,0.0f,
                0.0f,0.0f,0.0f,false, false,
                false,true, ModSounds.HEAVYTHICKSWOOSHSOUND,  0.0f, 0.0f, 0.5f, 0.5f, settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            if (!target.isTeammate(player)) {
                if (this.prevAttackProgress > 0.9 && player.fallDistance > 0.0f && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle()) {
                    if (EnchantmentHelper.getLevel(ModEnchantments.EXECUTION, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0 && player.fallDistance > 4) {
                        target.damage(ModDamageTypes.of(target.getWorld(), ModDamageTypes.LAYER_DAMAGE_TYPE), (float) ((float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * Math.min(2.5, player.fallDistance * 0.1)));
                    }
                }
                if (EnchantmentHelper.getLevel(ModEnchantments.FOOLISH, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                    target.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_WEAKNESS_EFFECT, 100, 5, false, true), player);
                    target.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_SPEED_EFFECT, 100, 7, false, true), player);
                }
            }
        }
        return true;
    }
    @Override
    public void BeforePostHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.isAttackable()) {
            return;
        }
        if (target.handleAttack(attacker)) {
            return;
        }
        if (attacker instanceof PlayerEntity player) {
            if (this.wasSprinting && player.blockedByShield(attacker.getDamageSources().playerAttack(player))) {
                player.disableShield(true);
            }
        }
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (EnchantmentHelper.getLevel(ModEnchantments.RETREAT, user.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
            user.swingHand(hand, true);
            user.takeKnockback(0.6, MathHelper.sin((user.getYaw() + 180) * ((float) Math.PI / 180)), -MathHelper.cos((user.getYaw() + 180) * ((float) Math.PI / 180)));
            user.setVelocity(user.getVelocity().getX(), 0.3, user.getVelocity().getZ());
            user.velocityModified = true;
            user.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_ATTACK_DAMAGE_EFFECT, 120, 3, false, true), user);
            for (int i = 0; i < user.getInventory().size(); i++) {
                if (user.getInventory().getStack(i).isIn(ModTags.Items.HALBERD)) {
                    user.getItemCooldownManager().set(user.getInventory().getStack(i).getItem(), 160);
                }
            }
            user.clearActiveItem();
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }
    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, 0.7f, 90, 110);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.GREEN));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.HalberdDisableShield.tooltip").formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.TwoHanded.tooltip").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
