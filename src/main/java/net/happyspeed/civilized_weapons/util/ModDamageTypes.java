package net.happyspeed.civilized_weapons.util;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModDamageTypes {

    public static final RegistryKey<DamageType> AP_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("civilized_weapons", "apdamage"));
    public static final RegistryKey<DamageType> LAYER_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("civilized_weapons", "layerdamage"));
    public static final RegistryKey<DamageType> SLASH_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("civilized_weapons", "slashdamage"));
    public static final RegistryKey<DamageType> SHIELD_BREACH_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("civilized_weapons", "shieldbreachdamage"));
    public static final RegistryKey<DamageType> SUNSTRIKEDAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("civilized_weapons", "sunstrikedamage"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }
}