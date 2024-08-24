package net.happyspeed.civilized_weapons.item.custom;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.config.UniversalVars;
import net.happyspeed.civilized_weapons.enchantments.ModEnchantments;
import net.happyspeed.civilized_weapons.sounds.ModSounds;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SickleItemTemplate extends AdvancedWeaponTemplate {
    public SickleItemTemplate(ToolMaterial material, float attackDamage, Item.Settings settings) {
        super(material,attackDamage,-1.6f,1.1f,0.1f,false,0.0f,
                0.0f,0.0f,0.0f,false, false,
                true,true, ModSounds.THINSWOOSHSOUND, 0.0f,0.0f, 0.3f,-0.4f, settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            //Sequence Enchant Logic
            if (EnchantmentHelper.getLevel(ModEnchantments.SEQUENCE, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                if (player.hasStatusEffect(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT) && this.prevAttackProgress > 0.75 && player.getStatusEffect(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT).getAmplifier() + 1 < 6) {
                    player.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT, 40, player.getStatusEffect(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT).getAmplifier() + 1, false, true), player);
                } else {
                    player.addStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ATTACK_SPEED_EFFECT, 40, 0, false, true), player);
                }
            }
            if (EnchantmentHelper.getLevel(ModEnchantments.VERTICALITY, player.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                target.setVelocity(new Vec3d(target.getVelocity().getX(), 0.4, target.getVelocity().getZ()));
                target.velocityModified = true;
            }
        }
        return true;
    }
    @Override
    public void swingSoundEvent(LivingEntity livingEntity) {
        this.playRandomPitchSound(this.swingSound, livingEntity, 0.7f, 120, 140);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.civilized_weapons.CritMulti.tooltip", String.valueOf(this.weaponCriticalMultiplier)).formatted(Formatting.RED));
        tooltip.add(Text.translatable("tooltip.civilized_weapons.SickleKnockback.tooltip").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
