package net.happyspeed.civilized_weapons.item.custom;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


//you can add Armor Piercing damage with this code
//target.damage(ModDamageTypes.of(target.getWorld(), ModDamageTypes.AP_DAMAGE_TYPE), 3.0f);

//check for enchantment on item
//EnchantmentHelper.getLevel(CivilizedWeaponsMod.DUMMY, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0

public class SpearItemTemplate extends AdvancedWeaponTemplate {
    public SpearItemTemplate(ToolMaterial material, float attackDamage, Item.Settings settings) {
        super(material,attackDamage,-2.4f,1.2f,0.5f,false,0.0f,
                0.0f,0.0f,0.0f,false, false,
                false,true, ModSounds.HEFTYSWOOSHSOUND, 0.0f,0.0f, 0.5f,0.2f, settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player && !target.isTeammate(player)) {
            if (!CivilizedHelper.isCriticalHit(player, 0.9f) && this.prevAttackProgress > 0.5f && !player.isSneaking() && this.wasSprinting) {
                this.playRandomPitchSound(ModSounds.SPEARHITSOUND, target, 0.6f, 70, 100);
                target.damage(new DamageSource(ModDamageTypes.of(target.getWorld(), ModDamageTypes.LAYER_DAMAGE_TYPE).getTypeRegistryEntry(), player), ((((float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.5f) * this.prevAttackProgress)));
                player.setSprinting(true);
            }
            //Jousting Enchant Logic
            if (player.hasVehicle() && player.getVehicle() instanceof HorseEntity && EnchantmentHelper.getLevel(ModEnchantments.JOUSTING, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0 && this.prevAttackProgress > 0.7f && !player.isSneaking()) {
                this.playRandomPitchSound(ModSounds.SPEARHITSOUND, target, 0.6f, 70, 100);
                if (!(CivilizedWeaponsMod.armortohealthloaded)) {
                    target.damage(new DamageSource(ModDamageTypes.of(target.getWorld(), ModDamageTypes.AP_DAMAGE_TYPE).getTypeRegistryEntry(), player), ((float) (player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.75f) * this.prevAttackProgress));
                }
                else {
                    target.damage(new DamageSource(ModDamageTypes.of(target.getWorld(), DamageTypes.MOB_ATTACK).getTypeRegistryEntry(), player), ((float) (player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)) * this.prevAttackProgress));
                }
                target.takeKnockback(0.6, MathHelper.sin(player.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180)));
            }
            //Pursuer Enchant Logic
            if (EnchantmentHelper.getLevel(ModEnchantments.PURSUER, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0 && this.prevAttackProgress > 0.7f && this.wasSprinting && !player.isSneaking()) {
                this.playRandomPitchSound(ModSounds.SPEARHITSOUND, target, 0.6f, 70, 100);
                if (player.hasStatusEffect(CivilizedWeaponsMod.ADD_SPEED_EFFECT) && this.prevAttackProgress > 0.7 && player.getStatusEffect(CivilizedWeaponsMod.ADD_SPEED_EFFECT).getAmplifier() + 1 < 12) {
                    player.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_SPEED_EFFECT, 220, player.getStatusEffect(CivilizedWeaponsMod.ADD_SPEED_EFFECT).getAmplifier() + 1, false, true), player);
                }
                else if (player.hasStatusEffect(CivilizedWeaponsMod.ADD_SPEED_EFFECT) && this.prevAttackProgress > 0.7 && player.getStatusEffect(CivilizedWeaponsMod.ADD_SPEED_EFFECT).getAmplifier() + 1 >= 12) {
                    player.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_SPEED_EFFECT, 220, player.getStatusEffect(CivilizedWeaponsMod.ADD_SPEED_EFFECT).getAmplifier(), false, true), player);
                }
                else {
                    player.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_SPEED_EFFECT, 60, 1, false, true), player);
                }
            }

        }
        return true;
    }
    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, 0.5f, 100, 120);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.RED));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.SpearCharge.tooltip").formatted(Formatting.DARK_GREEN));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
