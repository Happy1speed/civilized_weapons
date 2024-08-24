package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.item.custom.HalberdItemTemplate;
import net.happyspeed.civilized_weapons.item.custom.PanItemTemplate;
import net.happyspeed.civilized_weapons.item.custom.SickleItemTemplate;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class DummyEnchantment extends Enchantment {
	public DummyEnchantment() {
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
		return stack.getItem().asItem() instanceof PanItemTemplate;
	}
	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		user.sendMessage(Text.literal("!dummy!"));
	}
}