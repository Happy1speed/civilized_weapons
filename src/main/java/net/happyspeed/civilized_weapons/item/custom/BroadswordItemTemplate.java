package net.happyspeed.civilized_weapons.item.custom;

import com.google.common.collect.ImmutableMultimap;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.config.UniversalVars;
import net.happyspeed.civilized_weapons.enchantments.ModEnchantments;
import net.happyspeed.civilized_weapons.sounds.ModSounds;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.happyspeed.civilized_weapons.util.ModDamageTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
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
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

import java.util.List;
import java.util.Random;

public class BroadswordItemTemplate extends AdvancedWeaponTemplate {
    public BroadswordItemTemplate(ToolMaterial material, float attackDamage, Item.Settings settings) {
        super(material,attackDamage,-2.8f,1.7f,0.4f,false,0.0f,
                1.0f,1.0f,0.0f,false, false,true,
                true, ModSounds.HEFTYSWOOSHSOUND,  0.0f, 0.0f, 0.5f, -0.5f, settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            //Ground Slam Enchant Logic
            if (EnchantmentHelper.getLevel(ModEnchantments.GROUNDSLAM, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0 && this.wasSprinting) {
                player.setSprinting(true);
                List<LivingEntity> list = player.getWorld().getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(3.0, 2.2, 3.0));
                for (LivingEntity livingEntity : list) {
                    if (!livingEntity.isTeammate(player)) {
                        double entityDistance = livingEntity.getPos().distanceTo(player.getPos());
                        if (livingEntity == player || player.isTeammate(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker() ||
                                !livingEntity.isAttackable() || entityDistance > 4)
                            continue;
                        livingEntity.takeKnockback(1.0, player.getX() - livingEntity.getX(), player.getZ() - livingEntity.getZ());
                        livingEntity.damage(new DamageSource(ModDamageTypes.of(livingEntity.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE).getTypeRegistryEntry(), player), (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) + 2);
                        this.applyGroundSlamParticles(livingEntity);
                    }
                }
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), (float) UniversalVars.SWINGSOUNDSVOLUME, 0.8f);
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, player.getSoundCategory(), (float) UniversalVars.SWINGSOUNDSVOLUME, 0.8f);
            }
        }
        return true;
    }

    @Override
    public void ticker(LivingEntity living) {
        //Retribution Enchantment Logic
        if (living instanceof PlayerEntity player && !player.getWorld().isClient()) {
            if (EnchantmentHelper.getLevel(ModEnchantments.RETRIBUTION, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0 && player.getHealth() < player.getMaxHealth()) {
                player.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT, 20, Math.min(10,(int) -((player.getHealth() - player.getMaxHealth()) * 0.3)), false, true), player);
            }
        }
    }

    public void applyGroundSlamParticles(LivingEntity entity) {
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
                float speed = random2.ints(5, 10).findFirst().getAsInt();
                speed = speed / 100;
                ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.CRIT, entity.getX() + offsetx, entity.getY() + (entity.getHeight() * 0.5), entity.getZ() + offsetz, 1, 0.0, offsety, 0.0, speed);
            }
        }
    }

    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, 0.6f, 70, 85);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.GREEN));
        super.appendTooltip(stack, world, tooltip, context);
    }
}