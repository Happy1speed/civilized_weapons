package net.happyspeed.civilized_weapons.status_effects;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.util.ModDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.RaycastContext;

import java.util.Map;
import java.util.Random;

public class SunStrikeEffect extends StatusEffect {
    public SunStrikeEffect() {
        // category: StatusEffectCategory - describes if the effect is helpful (BENEFICIAL), harmful (HARMFUL) or useless (NEUTRAL)
        // color: int - Color is the color assigned to the effect (in RGB)
        super(StatusEffectCategory.NEUTRAL, 0xe9b8b3);
    }
    // Called every tick to check if the effect can be applied or not
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // In our case, we just make it return true so that it applies the effect every tick
        return true;
    }
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : this.getAttributeModifiers().entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributes.getCustomInstance(entry.getKey());
            if (entityAttributeInstance == null) continue;
            EntityAttributeModifier entityAttributeModifier = entry.getValue();
            entityAttributeInstance.removeModifier(entityAttributeModifier);
            entityAttributeInstance.addPersistentModifier(new EntityAttributeModifier(entityAttributeModifier.getId(), this.getTranslationKey() + " " + amplifier, this.adjustModifierAmount(amplifier, entityAttributeModifier), entityAttributeModifier.getOperation()));
        }
    }
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.getWorld().isClient()) {
            if (entity.getWorld().getLightLevel(LightType.SKY, entity.getBlockPos()) > 12 && entity.getWorld().getAmbientDarkness() < 6) {
                Random random = new Random();
                float pitch = random.ints(180, 200).findFirst().getAsInt();
                pitch = pitch / 100;
                entity.getWorld().playSound(null, BlockPos.ofFloored(entity.getPos()), SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.PLAYERS, 0.6f, pitch);
                entity.getWorld().playSound(null, BlockPos.ofFloored(entity.getPos()), SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.PLAYERS, 1.0f, 0.7f);
                entity.setOnFireFor(2);
                if (entity.getWorld() instanceof ServerWorld) {
                    for (int k = 0; k < 21; k++) {
                        Random random5 = new Random();
                        float offsetx = random5.ints(-60, 60).findFirst().getAsInt();
                        offsetx = offsetx / 100;
                        Random random3 = new Random();
                        float offsetz = random3.ints(-60, 60).findFirst().getAsInt();
                        offsetz = offsetz / 100;
                        Random random2 = new Random();
                        float offsety = random2.ints(-30, 60).findFirst().getAsInt();
                        offsety = offsety / 100;
                        Random random4 = new Random();
                        float speed = random4.ints(4, 10).findFirst().getAsInt();
                        speed = speed / 100;
                        ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.FLAME, entity.getX() + offsetx, entity.getY() + (entity.getHeight() * 0.5), entity.getZ() + offsetz, 1, 0.0, offsety, 0.0, speed);
                    }
                }
                entity.damage(new DamageSource(ModDamageTypes.of(entity.getWorld(), ModDamageTypes.SUNSTRIKEDAMAGE).getTypeRegistryEntry()),
                        amplifier + 1);
            }
        }
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : this.getAttributeModifiers().entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributes.getCustomInstance(entry.getKey());
            if (entityAttributeInstance == null) continue;
            entityAttributeInstance.removeModifier(entry.getValue());
        }
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient()) {
            if (entity.getWorld().getLightLevel(LightType.SKY, entity.getBlockPos()) <= 12 || entity.getWorld().getAmbientDarkness() > 5) {
                entity.removeStatusEffect(this);
            }
        }
        if (entity.getWorld() instanceof ServerWorld) {
            for (int k = 0; k < 4; k++) {
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
                ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.SMOKE, entity.getX() + offsetx, entity.getY() + (entity.getHeight() * 0.5), entity.getZ() + offsetz, 1, 0.0, offsety, 0.0, speed);
            }
        }
    }
}
