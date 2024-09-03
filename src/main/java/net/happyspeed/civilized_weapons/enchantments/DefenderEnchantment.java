package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.item.custom.GlaiveItemTemplate;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class DefenderEnchantment extends Enchantment {
	public DefenderEnchantment() {
		super(Rarity.UNCOMMON, EnchantmentTarget.BREAKABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	@Override
	public int getMinPower(int level) {
		return 23;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return true;
	}
}