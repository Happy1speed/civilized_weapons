package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.item.custom.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class SneakattackEnchantment extends Enchantment {
	public SneakattackEnchantment() {
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
		return stack.getItem().asItem() instanceof SaberItemTemplate
				|| stack.getItem().asItem() instanceof BroadswordItemTemplate
				|| stack.getItem().asItem() instanceof SwordItem
				|| stack.getItem().asItem() instanceof HalberdItemTemplate
				|| stack.getItem().asItem() instanceof GlaiveItemTemplate;
	}
}