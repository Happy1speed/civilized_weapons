package net.happyspeed.civilized_weapons.item.custom;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.config.UniversalVars;
import net.happyspeed.civilized_weapons.enchantments.ModEnchantments;
import net.happyspeed.civilized_weapons.sounds.ModSounds;
import net.happyspeed.civilized_weapons.util.ModDamageTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class FlailItemTemplate extends AdvancedWeaponTemplate {
    public FlailItemTemplate(ToolMaterial material, float attackDamage, Settings settings) {
        super(material,attackDamage,-2.0f,1.6f,0.5f,false,0.0f,
                0.0f,0.0f,0.0f,false, false,
                false,true, ModSounds.MEDIUMSWOOSHSOUND,  0.0f, 0.0f, 0.4f, -0.2f, settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            if (!target.isTeammate(player) && this.prevAttackProgress > 0.7f) {
                List<LivingEntity> list = player.getWorld().getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(2.5, 2.0, 2.5));
                int damagestack = 0;
                for (LivingEntity livingEntity : list) {
                    if (!livingEntity.isTeammate(player)) {
                        double entityDistance = livingEntity.getPos().distanceTo(player.getPos());
                        if (livingEntity == player || player.isTeammate(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker() ||
                                !livingEntity.isAttackable() || entityDistance > 4)
                            continue;
                        if (player.canSee(livingEntity)) {
                            if (EnchantmentHelper.getLevel(ModEnchantments.WHISPERINGSTARS, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 160, 1), player);
                                if (livingEntity instanceof PlayerEntity player1 && !player1.getInventory().getStack(EquipmentSlot.HEAD.getEntitySlotId()).isEmpty() && !livingEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
                                    player1.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 120, 1), player);
                                }
                                if (!livingEntity.hasStatusEffect(CivilizedWeaponsMod.ADD_WEAKNESS_EFFECT) && !(livingEntity instanceof PlayerEntity player1)) {
                                    livingEntity.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_WEAKNESS_EFFECT, 100, 2), player);
                                }
                            }
                            if (EnchantmentHelper.getLevel(ModEnchantments.AGAINSTODDS, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                                if (damagestack + 1 < 13) {
                                    damagestack++;
                                }
                            }
                        }
                    }
                }
                if (EnchantmentHelper.getLevel(ModEnchantments.AGAINSTODDS, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0 && damagestack > 2) {
                    player.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_ATTACK_DAMAGE_EFFECT, 120, damagestack, false, true), player);
                }
            }
            if (EnchantmentHelper.getLevel(ModEnchantments.HASTY, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0 && !target.isTeammate(player)) {
                if (this.prevAttackProgress < 0.7) {
                    target.setOnFireFor(4);
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
            if (!target.isTeammate(player)) {
                if (EnchantmentHelper.getLevel(ModEnchantments.HUGESWING, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0 && !attacker.getWorld().isClient() && !target.blockedByShield(new DamageSource(target.getDamageSources().playerAttack(player).getTypeRegistryEntry()))) {
                    if (target.getHealth() > target.getMaxHealth() * 0.8) {
                        if (!this.isCriticalHit(player)) {
                            target.damage(ModDamageTypes.of(target.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE), (float) (player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 1.5) * player.getAttackCooldownProgress(1.0f));
                            target.takeKnockback(this.weaponKnockbackMulti, MathHelper.sin(player.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180)));
                        }
                        else {
                            target.damage(ModDamageTypes.of(target.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE), (float) (player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 2) * player.getAttackCooldownProgress(1.0f));
                            target.takeKnockback(this.weaponKnockbackMulti, MathHelper.sin(player.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180)));
                            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0f, 1.0f);
                            player.addCritParticles(target);
                        }
                        if (target.getWorld() instanceof ServerWorld) {
                            for (int k = 0; k < 18; k++) {
                                Random random = new Random();
                                float offsetx = random.ints(-60, 60).findFirst().getAsInt();
                                offsetx = offsetx / 100;
                                Random random3 = new Random();
                                float offsetz = random3.ints(-60, 60).findFirst().getAsInt();
                                offsetz = offsetz / 100;
                                Random random2 = new Random();
                                float offsety = random2.ints(-30, 60).findFirst().getAsInt();
                                offsety = offsety / 100;
                                Random random4 = new Random();
                                float speed = random4.ints(5, 10).findFirst().getAsInt();
                                speed = speed / 100;
                                ((ServerWorld) target.getWorld()).spawnParticles(CivilizedWeaponsMod.HUGE_SWING_PARTICLE, target.getX() + offsetx, target.getY() + (target.getHeight() * 0.5), target.getZ() + offsetz, 1, 0.0, offsety, 0.0, speed);
                            }
                        }
                    }
                }
                if (!this.isCriticalHit(player)) {
                    if (!target.getWorld().isClient() && !attacker.getWorld().isClient() && target.blockedByShield(new DamageSource(target.getDamageSources().playerAttack(player).getTypeRegistryEntry()))) {
                        this.playRandomPitchSound(ModSounds.PANHITSOUND, target, (float) UniversalVars.SWINGSOUNDSVOLUME, 160, 180);
                        target.damage(new DamageSource(ModDamageTypes.of(target.getWorld(), ModDamageTypes.SHIELD_BREACH_DAMAGE_TYPE).getTypeRegistryEntry(), player), ((float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * player.getAttackCooldownProgress(1.0f)));
                    }
                }
            }
        }
    }
    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, 0.5f, 80, 100);
        this.playRandomPitchSound(ModSounds.FLAILSWINGSOUND, livingEntity, 0.5f, 90, 110);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.GREEN));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.FlailShieldDamage.tooltip").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.TwoHanded.tooltip").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
