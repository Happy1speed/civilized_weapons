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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


//you can add Armor Piercing damage with this code
//target.damage(ModDamageTypes.of(target.getWorld(), ModDamageTypes.AP_DAMAGE_TYPE), 3.0f);

//check for enchantment on item
//EnchantmentHelper.getLevel(CivilizedWeaponsMod.DUMMY, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0

public class SaberItemTemplate extends AdvancedWeaponTemplate {
    Entity lockedtarget;
    public SaberItemTemplate(ToolMaterial material, float attackDamage, Settings settings) {
        super(material,attackDamage,-2.0f,1.4f,0.4f,true,4.0f,
                0.0f,3.0f,0.4f,true, true,
                true, true, ModSounds.THINSWOOSHSOUND,  2.7f,0.3f, 0.5f,-0.3f, settings);
        this.weaponSweepDamage = this.getAttackDamage();
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (attacker instanceof PlayerEntity player && !player.getWorld().isClient()) {
            if (EnchantmentHelper.getLevel(ModEnchantments.SEQUENCE, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                if (player.hasStatusEffect(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT) && this.prevAttackProgress > 0.75 && player.getStatusEffect(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT).getAmplifier() + 1 < 6) {
                    player.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT, 40, player.getStatusEffect(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT).getAmplifier() + 1, false, true), player);
                } else {
                    player.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT, 40, 0, false, true), player);
                }
            }
            if (EnchantmentHelper.getLevel(ModEnchantments.LOCKED, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                if (this.lockedtarget != null) {
                    if (this.lockedtarget == target && this.prevAttackProgress > 0.75) {
                        if (player.hasStatusEffect(CivilizedWeaponsMod.ADD_ATTACK_DAMAGE_EFFECT) && player.getStatusEffect(CivilizedWeaponsMod.ADD_ATTACK_DAMAGE_EFFECT).getAmplifier() + 2 < 9) {
                            player.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_ATTACK_DAMAGE_EFFECT, 20, player.getStatusEffect(CivilizedWeaponsMod.ADD_ATTACK_DAMAGE_EFFECT).getAmplifier() + 2, false, true), player);
                            player.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_SLOW_EFFECT, 20, player.getStatusEffect(CivilizedWeaponsMod.ATTACK_SPEED_SLOW_EFFECT).getAmplifier() + 2, false, true), player);
                        }
                        else {
                            player.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_ATTACK_DAMAGE_EFFECT, 20,  1, false, true), player);
                            player.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_SLOW_EFFECT, 20, 1, false, true), player);
                        }
                    }
                }
                this.lockedtarget = target;
            }
        }
        return true;
    }
    @Override
    public void activeHit(LivingEntity living) {
        if (living instanceof PlayerEntity player && !living.getWorld().isClient()) {
            if (this.isSweepingWeapon) {
                this.weaponSweepDamage = (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if (((!this.canSweepWithoutSneak && player.isSneaking()) || (this.canSweepWithoutSneak && !player.isSneaking())) && player.getAttackCooldownProgress(1.0f) > 0.3) {
                    if (this.canSweepWhileSprinting || (!player.isSprinting() || player.isSneaking())) {
                        if (this.canSweepWhileCritical || !CivilizedHelper.isCriticalHit(player, 0.9f)) {
                            List<LivingEntity> list = player.getWorld().getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().offset(player.getRotationVector().multiply(this.weaponSweepRange,
                                    this.weaponSweepRange, this.weaponSweepRange)).expand(this.weaponSweepWidth, this.weaponSweepWidth, this.weaponSweepWidth));
                            for (LivingEntity livingEntity : list) {
                                if (!livingEntity.isTeammate(player)) {
                                    double entityDistance = livingEntity.getPos().distanceTo(player.getPos());
                                    if (livingEntity == player || player.isTeammate(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker() ||
                                            !livingEntity.isAttackable() || !this.IsInViewingAngle(player, livingEntity) || entityDistance > this.realSweepDistance)
                                        continue;
                                    affectSweepEntity(livingEntity, player);
                                    livingEntity.takeKnockback(this.weaponSweepKnockback, MathHelper.sin(player.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180)));
                                    if (CivilizedHelper.isCriticalHit(player, 0.9f)) {
                                        livingEntity.damage(new DamageSource(ModDamageTypes.of(livingEntity.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE).getTypeRegistryEntry(), player), (this.weaponSweepDamage * this.weaponCriticalMultiplier) * player.getAttackCooldownProgress(1.0f));
                                        player.addCritParticles(livingEntity);
                                        player.getWorld().playSound(null, livingEntity.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                                    }
                                    else {
                                        livingEntity.damage(new DamageSource(ModDamageTypes.of(livingEntity.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE).getTypeRegistryEntry(), player), this.weaponSweepDamage);
                                    }
                                }
                            }
                            this.sweepSound(player);
                            player.spawnSweepAttackParticles();
                        }
                    }
                }
            }
            if (this.hasSwingSFX && UniversalVars.SWINGSOUNDSENABLED) {
                if (player.getAttackCooldownProgress(1.0f) > 0.3) {
                    this.swingSoundEvent(living);
                }
            }
        }
    }

    @Override
    public void sweepSound(PlayerEntity player) {
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 0.6f, 1.1f);
    }

    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, 0.5f, 90, 110);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.SweepDamage.tooltip", String.valueOf((int) this.weaponSweepDamage)).formatted(Formatting.DARK_GREEN));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
