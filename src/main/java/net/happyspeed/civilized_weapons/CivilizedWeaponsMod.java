package net.happyspeed.civilized_weapons;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.happyspeed.civilized_weapons.enchantments.ModEnchantments;
import net.happyspeed.civilized_weapons.network.PlayerAttackPacket;
//import net.happyspeed.civilized_weapons.network.PlayerDualHandPacket;
import net.happyspeed.civilized_weapons.sounds.ModSounds;
import net.happyspeed.civilized_weapons.status_effects.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.happyspeed.civilized_weapons.item.ModItemGroups;
import net.happyspeed.civilized_weapons.item.ModItems;

import java.util.Optional;
import java.util.UUID;

//

public class CivilizedWeaponsMod implements ModInitializer {
	public static final String MOD_ID = "civilized_weapons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final UUID CIVIL_ATTACK_RANGE_MODIFIER_ID = UUID.fromString("51770b68-a4e4-4084-ad67-40d52a331a21");

	public static StatusEffect ATTACK_SPEED_EFFECT = new AttackSpeedEffect();
	public static StatusEffect ATTACK_SPEED_SLOW_EFFECT = new AttackSpeedSlowEffect();
	public static StatusEffect ADD_WEAKNESS_EFFECT = new AddWeaknessEffect();
	public static StatusEffect ADD_SLOWNESS_EFFECT = new AddSlownessEffect();
	public static StatusEffect ADD_ATTACK_DAMAGE_EFFECT = new AddAttackDamageEffect();
	public static StatusEffect ADD_SPEED_EFFECT = new AddSpeedEffect();
	public static StatusEffect SUNSTRIKE_EFFECT = new SunStrikeEffect();

	public static final DefaultParticleType SWEEP_DOWN_PARTICLE = FabricParticleTypes.simple();
	public static final DefaultParticleType HUGE_SWING_PARTICLE = FabricParticleTypes.simple();

	public static boolean commonEnchantmentDescriptionsModLoaded = false;

	public static boolean armortohealthloaded = false;

	@Override
	public void onInitialize() {
		LOGGER.info("Hello There!");

		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();

		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(CivilizedWeaponsMod.MOD_ID);
		if (FabricLoader.getInstance().isModLoaded("betterend")) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(CivilizedWeaponsMod.MOD_ID, "betterend_recipes"), (ModContainer) modContainer.get(), Text.translatable("pack.civilized_weapons.betterend_compat_text"), ResourcePackActivationType.ALWAYS_ENABLED);

		}

		if (FabricLoader.getInstance().isModLoaded("spectrum")) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(CivilizedWeaponsMod.MOD_ID, "spectrum_recipes"), (ModContainer) modContainer.get(), Text.translatable("pack.civilized_weapons.spectrum_compat_text"), ResourcePackActivationType.ALWAYS_ENABLED);
		}

		if (FabricLoader.getInstance().isModLoaded("armortohealth")) {
			armortohealthloaded = true;
		}

		ResourceManagerHelper.registerBuiltinResourcePack(
				new Identifier(MOD_ID, "weapon_default_positions"), modContainer.get(),
				Text.translatable("pack.civilized_weapons.weaponpospack"),
				ResourcePackActivationType.NORMAL
		);

		ModSounds.registerSounds();

		PlayerAttackPacket.init();

		ModEnchantments.init();
		Registry.register(Registries.STATUS_EFFECT, new Identifier("civilized_weapons", "attack_speed_effect"), ATTACK_SPEED_EFFECT).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "d9423981-31f9-434d-a00a-4e1ac0732767", 0.1f, EntityAttributeModifier.Operation.ADDITION);
		Registry.register(Registries.STATUS_EFFECT, new Identifier("civilized_weapons", "attack_speed_slow_effect"), ATTACK_SPEED_SLOW_EFFECT).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "324dd845-6839-4185-b4b4-883c6bf55d71", -0.1f, EntityAttributeModifier.Operation.ADDITION);
		Registry.register(Registries.STATUS_EFFECT, new Identifier("civilized_weapons", "add_weakness_effect"), ADD_WEAKNESS_EFFECT).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "9e349101-b7a0-416b-b3c5-cb6f531ab015", -0.5f, EntityAttributeModifier.Operation.ADDITION);
		Registry.register(Registries.STATUS_EFFECT, new Identifier("civilized_weapons", "add_slowness_effect"), ADD_SLOWNESS_EFFECT).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "47f8a154-3245-49eb-ac42-70dbbae4fe4a", -0.005f, EntityAttributeModifier.Operation.ADDITION);
		Registry.register(Registries.STATUS_EFFECT, new Identifier("civilized_weapons", "add_speed_effect"), ADD_SPEED_EFFECT).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "90f8b087-4695-408b-bc19-761831c014be", 0.005f, EntityAttributeModifier.Operation.ADDITION);
		Registry.register(Registries.STATUS_EFFECT, new Identifier("civilized_weapons", "add_attack_damage_effect"), ADD_ATTACK_DAMAGE_EFFECT).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "ec980e18-936e-477a-8eb1-122fa76f9332", 0.5f, EntityAttributeModifier.Operation.ADDITION);
		Registry.register(Registries.STATUS_EFFECT, new Identifier("civilized_weapons", "sunstrike_effect"), SUNSTRIKE_EFFECT);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "sweep_down_particle"), SWEEP_DOWN_PARTICLE);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "hugeswingparticle"), HUGE_SWING_PARTICLE);

		for (String mod : new String[]{"enchdesc", "enchantedtooltips", "idwtialsimmoedm"}) {
			if (FabricLoader.getInstance().isModLoaded(mod)) {
				commonEnchantmentDescriptionsModLoaded = true;
				break;
			}
		}

	}
}