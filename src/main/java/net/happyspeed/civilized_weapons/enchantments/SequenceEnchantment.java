package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.item.custom.SaberItemTemplate;
import net.happyspeed.civilized_weapons.item.custom.KukriItemTemplate;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
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
		return super.canAccept(other);
	}
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return stack.getItem().asItem() instanceof KukriItemTemplate || stack.getItem().asItem() instanceof SaberItemTemplate;
	}
}