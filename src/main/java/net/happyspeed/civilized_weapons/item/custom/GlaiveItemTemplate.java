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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GlaiveItemTemplate extends AdvancedWeaponTemplate {
    public GlaiveItemTemplate(ToolMaterial material, float attackDamage, Item.Settings settings) {
        super(material,attackDamage,-2.8f,1.6f,0.4f,true,5.0f,
                0.0f,3.8f,0.35f,true, true,
                true,true, ModSounds.MEDIUMSWOOSHSOUND,  3.2f, 0.2f, 0.5f, 0.5f, settings);
        this.weaponSweepDamage = this.getAttackDamage() + 1;
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }
    @Override
    public void affectSweepEntity(LivingEntity living, PlayerEntity player) {
        //Sunstrike Enchant Logic
        if ((EnchantmentHelper.getLevel(ModEnchantments.SUNSTRIKE, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0)) {
            if (!living.hasStatusEffect(CivilizedWeaponsMod.SUNSTRIKE_EFFECT)) {
                living.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.SUNSTRIKE_EFFECT, 100, 7), player);
            }
        }
        //Aerial Strike Enchant Logic
        if ((EnchantmentHelper.getLevel(ModEnchantments.AERIALSTRIKE, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0)) {
            living.setVelocity(new Vec3d(living.getVelocity().getX(), -0.5, living.getVelocity().getZ()));
            living.velocityModified = true;
            if (!living.groundCollision) {
                living.damage(new DamageSource(ModDamageTypes.of(living.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE).getTypeRegistryEntry(), player), ((float) (player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 1.5f) * player.getAttackCooldownProgress(1.0f)));
            }
        }
    }

    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, (float) UniversalVars.SWINGSOUNDSVOLUME, 60, 75);
    }
    @Override
    public void sweepSound(PlayerEntity player) {
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 0.7f, 0.8f);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.GREEN));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.SweepDamage.tooltip", String.valueOf((int) this.weaponSweepDamage)).formatted(Formatting.DARK_GREEN));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.TwoHanded.tooltip").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
