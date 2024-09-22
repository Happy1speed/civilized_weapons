package net.happyspeed.civilized_weapons.item.custom;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.CivilizedWeaponsModClient;
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
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DuelbladeItemTemplate extends AdvancedWeaponTemplate {
    SoundEvent tempsweepSound = SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP;
    int soundpitchmin = 0;
    int soundpitchmax = 0;
    float tempsweepknockback = 0.0f;
    public DuelbladeItemTemplate(ToolMaterial material, float attackDamage, Item.Settings settings) {
        super(material,attackDamage,-2.6f,1.4f,0.6f,true,6.0f,
                0.0f,4.0f,0.35f,true, true,
                true,true, ModSounds.THINSWOOSHSOUND,  3.0f, 0.1f, 0.5f, 0.2f, settings);
        this.weaponSweepDamage = this.getAttackDamage() + 1;
    }
    @Override
    public void activeHit(LivingEntity living) {
        if (living instanceof PlayerEntity player && !living.getWorld().isClient()) {
            if (this.isSweepingWeapon) {
                this.weaponSweepDamage = (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if (player.getAttackCooldownProgress(1.0f) > 0.7) {
                    if (this.canSweepWhileSprinting || !player.isSprinting() || player.isSneaking()) {
                        if (this.canSweepWhileCritical || !CivilizedHelper.isCriticalHit(player, 0.9f)) {

                            if (!player.isSneaking()) {
                                this.realSweepDistance = 3.0f;
                                this.tempsweepknockback = this.weaponSweepKnockback;
                                this.tempsweepSound = SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP;
                                this.soundpitchmin = 130;
                                this.soundpitchmax = 140;
                            } else {
                                if (EnchantmentHelper.getLevel(ModEnchantments.LARGESPIN, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                                    this.realSweepDistance = 5.0f;
                                    player.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_SLOW_EFFECT, 30, 7, false, true), player);
                                    this.soundpitchmin = 50;
                                    this.soundpitchmax = 60;
                                }
                                else {
                                    this.realSweepDistance = 3.0f;
                                    this.soundpitchmin = 70;
                                    this.soundpitchmax = 80;
                                }
                                this.tempsweepknockback = 0.0f;
                                this.tempsweepSound = ModSounds.SPINSWOOSHSOUND;
                            }
                            List<LivingEntity> list = player.getWorld().getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().offset(player.getRotationVector().multiply(0.0f,
                                    0.0f, 0.0f)).expand(this.weaponSweepWidth, this.weaponSweepWidth, this.weaponSweepWidth));
                            for (LivingEntity livingEntity : list) {
                                if (!livingEntity.isTeammate(player)) {
                                    double entityDistance = livingEntity.getPos().distanceTo(player.getPos());
                                    if (livingEntity == player || player.isTeammate(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker() ||
                                            !livingEntity.isAttackable() || entityDistance > this.realSweepDistance)
                                        continue;
                                    if (!this.IsInViewingAngle(player, livingEntity) && !player.isSneaking()) {
                                        continue;
                                    }
                                    if (player.canSee(livingEntity)) {
                                        if (CivilizedHelper.isCriticalHit(player, 0.9f)) {
                                            livingEntity.damage(new DamageSource(ModDamageTypes.of(livingEntity.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE).getTypeRegistryEntry(), player), (this.weaponSweepDamage * this.weaponCriticalMultiplier) * player.getAttackCooldownProgress(1.0f));
                                            player.addCritParticles(livingEntity);
                                            player.getWorld().playSound(null, livingEntity.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                                        }
                                        else {
                                            livingEntity.damage(new DamageSource(ModDamageTypes.of(livingEntity.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE).getTypeRegistryEntry(), player), this.weaponSweepDamage * player.getAttackCooldownProgress(1.0f));
                                        }
                                        if (!player.isSneaking()) {
                                            livingEntity.takeKnockback(this.tempsweepknockback, MathHelper.sin(player.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180)));
                                            livingEntity.velocityModified = true;
                                        }
                                        else {
                                            if (EnchantmentHelper.getLevel(ModEnchantments.ASCEND, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                                                livingEntity.setVelocity(new Vec3d(livingEntity.getVelocity().getX(), 0.7, livingEntity.getVelocity().getZ()));
                                                livingEntity.velocityModified = true;
                                            }
                                            if (EnchantmentHelper.getLevel(ModEnchantments.FIRESPIN, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                                                livingEntity.setOnFireFor(2);
                                            }
                                        }
                                    }
                                }
                            }
                            this.playRandomPitchSound(this.tempsweepSound, player, 0.5f, this.soundpitchmin, this.soundpitchmax);
                            player.spawnSweepAttackParticles();
                            if (player.isSneaking()) {
                                this.spawnBackSweepAttackParticles(player);
                            }
                        }
                    }
                }
            }
            if (this.hasSwingSFX) {
                if (player.getAttackCooldownProgress(1.0f) > 0.3) {
                    this.swingSoundEvent(living);
                }
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
                if (user.getInventory().getStack(i).isIn(ModTags.Items.DUELBLADE)) {
                    user.getItemCooldownManager().set(user.getInventory().getStack(i).getItem(), 160);
                }
            }
            user.clearActiveItem();
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }
    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, 0.6f, 60, 80);
    }
    public void spawnBackSweepAttackParticles(PlayerEntity player) {
        double d = -MathHelper.sin((player.getYaw() + 180) * ((float)Math.PI / 180));
        double e = MathHelper.cos((player.getYaw() + 180) * ((float)Math.PI / 180));
        if (player.getWorld() instanceof ServerWorld) {
            ((ServerWorld)player.getWorld()).spawnParticles(CivilizedWeaponsMod.SWEEP_DOWN_PARTICLE, player.getX() + d, player.getBodyY(0.5), player.getZ() + e, 0, d, 0.0, e, 0.0);
        }
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.SweepDamage.tooltip", String.valueOf((int) this.weaponSweepDamage)).formatted(Formatting.DARK_GREEN));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.DuelbladeSweepFull.tooltip").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.TwoHanded.tooltip").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
