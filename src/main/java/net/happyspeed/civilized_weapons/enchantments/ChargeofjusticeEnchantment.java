package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.item.custom.SickleItemTemplate;
import net.happyspeed.civilized_weapons.item.custom.SpearItemTemplate;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ChargeofjusticeEnchantment extends Enchantment {
	public ChargeofjusticeEnchantment() {
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
		return stack.getItem().asItem() instanceof SpearItemTemplate;
	}

	@Override
	public float getAttackDamage(int level, EntityGroup group) {
		return 1;
	}
	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		if (user instanceof PlayerEntity player) {
			Vec3d vec3d = player.getVelocity();
			Vec3d vec3d2 = new Vec3d(MathHelper.sin(player.getYaw() * ((float) Math.PI / 180)), 0.0, -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180))).normalize().multiply(-0.4);
			player.setVelocity(vec3d.x / 2.0 - vec3d2.x, vec3d.y, vec3d.z / 2.0 - vec3d2.z);
			player.velocityModified = true;
		}
	}
}