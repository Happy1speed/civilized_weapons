package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.happyspeed.civilized_weapons.item.custom.SaberItemTemplate;
import net.happyspeed.civilized_weapons.item.custom.SickleItemTemplate;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SequenceEnchantment extends Enchantment {
	public SequenceEnchantment() {
		super(Rarity.UNCOMMON, EnchantmentTarget.BREAKABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	@Override
	public int getMinPower(int level) {
		return 15;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.FEATHER_FALLING;
	}
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return stack.getItem().asItem() instanceof SickleItemTemplate || stack.getItem().asItem() instanceof SaberItemTemplate;
	}
}