package net.happyspeed.civilized_weapons.enchantments;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final Enchantment DUMMY = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "dummy"), new DummyEnchantment());
    public static final Enchantment SEQUENCE = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "sequence"), new SequenceEnchantment());
    public static final Enchantment JOUSTING = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "jousting"), new JoustingEnchantment());
    public static final Enchantment RETREAT = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "retreat"), new RetreatEnchantment());
    public static final Enchantment SUNSTRIKE = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "sunstrike"), new SunstrikeEnchantment());
    public static final Enchantment VERTICALITY = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "verticality"), new VerticalityEnchantment());
    public static final Enchantment LARGESPIN = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "largespin"), new LargespinEnchantment());
    public static final Enchantment PURSUER = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "pursuer"), new PursuerEnchantment());
    public static final Enchantment RETRIBUTION = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "retribution"), new RetributionEnchantment());
    public static final Enchantment GROUNDSLAM = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "groundslam"), new GroundslamEnchantment());
    public static final Enchantment CHARGEOFJUSTICE = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "chargeofjustice"), new ChargeofjusticeEnchantment());
    public static final Enchantment ASCEND = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "ascend"), new AscendEnchantment());
    public static final Enchantment WHISPERINGSTARS = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "whisperingstars"), new WhisperingstarsEnchantment());
    public static final Enchantment DRAWRUSH = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "drawrush"), new DrawrushEnchantment());
    public static final Enchantment AERIALSTRIKE = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "aerialstrike"), new AerialstrikeEnchantment());
    public static final Enchantment FIRESPIN = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "firespin"), new FirespinEnchantment());
    public static final Enchantment FOOLISH = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "foolish"), new FoolishEnchantment());
    public static final Enchantment AGAINSTODDS = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "againstodds"), new AgainstoddsEnchantment());
    public static final Enchantment HASTY = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "hasty"), new HastyEnchantment());
    public static final Enchantment EXECUTION = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "execution"), new ExecutionEnchantment());
    public static final Enchantment HUGESWING = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "hugeswing"), new HugeswingEnchantment());
    public static final Enchantment LOCKED = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "locked"), new LockedEnchantment());
    public static final Enchantment TRUSTWORTHY = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "trustworthy"), new TrustworthyEnchantment());
    public static final Enchantment DEFENDER = Registry.register(Registries.ENCHANTMENT, new Identifier(CivilizedWeaponsMod.MOD_ID, "defender"), new DefenderEnchantment());

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, name, enchantment);
    }
    public static void init() {}
}
