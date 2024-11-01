package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.item.custom.AdvancedWeaponTemplate;
import net.happyspeed.civilized_weapons.item.custom.DuelbladeItemTemplate;
import net.happyspeed.civilized_weapons.util.ModDamageTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

import java.util.List;

public class MultitargetEnchantment extends Enchantment {
	public MultitargetEnchantment() {
		super(Rarity.UNCOMMON, EnchantmentTarget.BREAKABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	@Override
	public int getMinPower(int level) {
		return 23;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return (stack.getItem().asItem() instanceof AdvancedWeaponTemplate || stack.getItem().asItem() instanceof SwordItem);
	}
	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		if (user instanceof PlayerEntity player && !player.getWorld().isClient() && target instanceof LivingEntity entity) {
			List<LivingEntity> list = player.getWorld().getNonSpectatingEntities(LivingEntity.class, entity.getBoundingBox().expand(2.0, 2.0, 2.0));
			for (LivingEntity livingEntity : list) {
				if (!livingEntity.isTeammate(player)) {
					double entityDistance = livingEntity.getPos().distanceTo(entity.getPos());
					if (livingEntity == player || player.isTeammate(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker() ||
							!livingEntity.isAttackable() || entityDistance > 1 + level - 1 || livingEntity.getType() != entity.getType())
						continue;
					if (entity.canSee(livingEntity)) {
						livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 1), player);
						livingEntity.damage(ModDamageTypes.of(target.getWorld(), ModDamageTypes.SLASH_DAMAGE_TYPE), ((float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.5f));
					}
				}
			}
		}
	}
}