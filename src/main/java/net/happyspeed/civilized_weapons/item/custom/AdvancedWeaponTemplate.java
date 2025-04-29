package net.happyspeed.civilized_weapons.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.happyspeed.civilized_weapons.config.UniversalVars;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.happyspeed.civilized_weapons.util.ModDamageTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

//apply attack speed status effect (this is removed when switching off of the weapon)
//attacker.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT, 200,2, false, false, false));

public class AdvancedWeaponTemplate extends ToolItem {
    public float prevAttackProgress = 0.0f;
    public float prevReceivedDamage = 0.0f;
    public float weaponCriticalMultiplier = 1.5f;
    public float weaponSprintKnockbackMultiplier = 0.5f;
    public float weaponSweepDamage = 0.0f;
    //long values for the sweep range and with are 1.5, vanilla is 0.5
    public float weaponSweepRange = 0.0f;
    public float weaponSweepWidth = 0.0f;
    public float weaponSweepKnockback = 0.0f;
    public boolean isSweepingWeapon = false;
    public boolean wasSprinting = false;
    public boolean canSweepWhileSprinting = false;
    public boolean canSweepWhileCritical = false;
    public boolean canSweepWithoutSneak = true;
    public boolean hasSwingSFX = false;
    public boolean canBlockProjectiles = false;
    public float realSweepDistance = 0.0f;
    public float sweepAngle = 0.0f;
    public float weaponKnockbackMulti = 0.5f;
    public SoundEvent swingSound = SoundEvents.INTENTIONALLY_EMPTY;
    private final float attackDamage;
    private final float attackSpeed;
    public final float attackRange;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    public AdvancedWeaponTemplate(ToolMaterial material, float attackDamage, float attackSpeed, float weaponCriticalMultiplier,
                                  float weaponSprintKnockbackMultiplier,boolean isSweepingWeapon, float weaponSweepDamage,
                                  float weaponSweepRange, float weaponSweepWidth, float weaponSweepKnockback, boolean canSweepWhileSprinting, boolean canSweepWhileCritical,
                                  boolean canSweepWithoutSneak, boolean hasSwingSFX, SoundEvent swingSound, float realSweepDistance,
                                  float sweepAngle, float weaponKnockbackMulti,float attackRange, Settings settings) {
        super(material, settings);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.weaponCriticalMultiplier = weaponCriticalMultiplier;
        this.weaponSprintKnockbackMultiplier = weaponSprintKnockbackMultiplier;
        this.isSweepingWeapon = isSweepingWeapon;
        this.weaponSweepDamage = weaponSweepDamage;
        this.weaponSweepRange = weaponSweepRange;
        this.weaponSweepWidth = weaponSweepWidth;
        this.canSweepWhileSprinting = canSweepWhileSprinting;
        this.canSweepWhileCritical = canSweepWhileCritical;
        this.canSweepWithoutSneak = canSweepWithoutSneak;
        this.weaponSweepKnockback = weaponSweepKnockback;
        this.hasSwingSFX = hasSwingSFX;
        this.swingSound = swingSound;
        this.realSweepDistance = realSweepDistance;
        this.sweepAngle = sweepAngle;
        this.weaponKnockbackMulti = weaponKnockbackMulti;
        this.attackRange = attackRange;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(CivilizedWeaponsMod.CIVIL_ATTACK_RANGE_MODIFIER_ID, "Weapon modifier", this.attackRange, EntityAttributeModifier.Operation.ADDITION));
        attributeModifiers = builder.build();
    }
    public void playRandomPitchSound(SoundEvent soundEvent, LivingEntity livingEntity, float volume, int pitchMin, int pitchMax) {
        Random random = new Random();
        float pitch = random.ints(pitchMin, pitchMax).findFirst().getAsInt();
        pitch = pitch / 100;
        livingEntity.getWorld().playSound(null, livingEntity.getBlockPos(), soundEvent, CivilizedWeaponsMod.weaponSwingSoundCategory, volume,pitch);
    }

    public void playRandomPitchNonSwingSound(SoundEvent soundEvent, LivingEntity livingEntity, float volume, int pitchMin, int pitchMax) {
        Random random = new Random();
        float pitch = random.ints(pitchMin, pitchMax).findFirst().getAsInt();
        pitch = pitch / 100;
        livingEntity.getWorld().playSound(null, livingEntity.getBlockPos(), soundEvent, SoundCategory.PLAYERS, volume,pitch);
    }

    //handle swing sound effect and custom sweeping if weapon has that
    public void activeHit(LivingEntity living) {
        if (living instanceof PlayerEntity player && !living.getWorld().isClient()) {
            if (this.isSweepingWeapon) {
                this.weaponSweepDamage = (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if (((!this.canSweepWithoutSneak && player.isSneaking()) || (this.canSweepWithoutSneak && !player.isSneaking())) && player.getAttackCooldownProgress(0.5f) > 0.7) {
                    if (this.canSweepWhileSprinting || (!player.isSprinting() || player.isSneaking())) {
                        if (this.canSweepWhileCritical || !CivilizedHelper.isCriticalHit(player, 0.9f)) {
                            List<LivingEntity> list = player.getWorld().getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().offset(player.getRotationVector().multiply(this.weaponSweepRange,
                                    this.weaponSweepRange, this.weaponSweepRange)).expand(this.weaponSweepWidth, this.weaponSweepWidth, this.weaponSweepWidth));
                            for (LivingEntity livingEntity : list) {
                                if (!livingEntity.isTeammate(player)) {
                                    double entityDistance = livingEntity.getPos().distanceTo(player.getPos());
                                    if (livingEntity == player || player.isTeammate(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker() ||
                                            !livingEntity.isAttackable() || !this.IsInViewingAngle(player, livingEntity) || entityDistance > this.realSweepDistance || livingEntity.hurtTime != 0)
                                        continue;
                                    affectSweepEntity(livingEntity, player);
                                    livingEntity.takeKnockback(this.weaponSweepKnockback, MathHelper.sin(player.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180)));
                                    livingEntity.damage(new DamageSource(ModDamageTypes.of(livingEntity.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE).getTypeRegistryEntry(), player), this.weaponSweepDamage);
                                }
                            }
                            this.sweepSound(player);
                            player.spawnSweepAttackParticles();
                        }
                    }
                }
            }
            if (this.hasSwingSFX && UniversalVars.SWINGSOUNDSENABLED) {
                if (player.getAttackCooldownProgress(0.5f) > 0.2) {
                    this.swingSoundEvent(living);
                }
            }
        }
    }

    public void ticker(LivingEntity living) {}

    public void onDraw(LivingEntity living) {}

    @Override
    public int getEnchantability() {
        return 20;
    }
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }



    public void swingSoundEvent(LivingEntity livingEntity) {}

    public void BeforePostHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    public void affectSweepEntity(LivingEntity living, PlayerEntity player) {}

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }
    public void sweepSound(PlayerEntity player) {
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 0.7f, 1.0f);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }
    public void tickTrustEnchant(LivingEntity living) {
        if (living instanceof PlayerEntity player) {
            if (((PlayerClassAccess) player).civilized_weapons$getTicksSinceLastItemSwap() > 180) {
                player.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT, 60,  3, false, true), player);
            }
        }
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public float getAttackSpeed() {
        return this.attackSpeed;
    }

    boolean IsInViewingAngle(LivingEntity living1, LivingEntity living2) {
        Vec3d living1LookDir = living1.getRotationVec(1.0f);
        Vec3d living2Dir = (living2.getPos().subtract(living1.getPos())).normalize();
        double angle = living1LookDir.dotProduct(living2Dir);
        if (angle > this.sweepAngle) {
            return living1.canSee(living2);
        }
        return false;
    }

    boolean IsInBehindViewingAngle(LivingEntity living1, LivingEntity living2) {
        Vec3d living1LookDir = living1.getRotationVec(1.0f);
        Vec3d living2Dir = (living2.getPos().subtract(living1.getPos())).normalize();
        double angle = living1LookDir.dotProduct(living2Dir);
        if (angle < this.sweepAngle) {
            return living1.canSee(living2);
        }
        return false;
    }
}
