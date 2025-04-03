package net.happyspeed.civilized_weapons.sounds;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent PANHITSOUND  = registerSoundEvent("cast_iron_pan_sound");
    public static final SoundEvent HEAVYTHICKSWOOSHSOUND  = registerSoundEvent("heavy_thick_swoosh_sound");
    public static final SoundEvent MEDIUMSWOOSHSOUND  = registerSoundEvent("medium_swoosh_sound");
    public static final SoundEvent HEFTYSWOOSHSOUND  = registerSoundEvent("hefty_swoosh_sound");
    public static final SoundEvent THINSWOOSHSOUND  = registerSoundEvent("thin_swoosh_sound");
    public static final SoundEvent LIGHTSWOOSHSOUND  = registerSoundEvent("light_swoosh_sound");
    public static final SoundEvent SPINSWOOSHSOUND  = registerSoundEvent("spin_swoosh_sound");
    public static final SoundEvent SPEARHITSOUND  = registerSoundEvent("spear_hit_sound");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(CivilizedWeaponsMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        CivilizedWeaponsMod.LOGGER.info("Registering Sounds for " + CivilizedWeaponsMod.MOD_ID);
    }
}
