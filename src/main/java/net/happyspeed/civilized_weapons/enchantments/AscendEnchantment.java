package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.item.custom.AdvancedWeaponTemplate;
import net.happyspeed.civilized_weapons.item.custom.DualbladeItemTemplate;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class AscendEnchantment extends Enchantment {
	public AscendEnchantment() {
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
		return stack.getItem().asItem() instanceof AdvancedWeaponTemplate;
	}
}