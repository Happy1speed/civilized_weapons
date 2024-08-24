package net.happyspeed.civilized_weapons.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup CIVILIZED_WEAPONS_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(CivilizedWeaponsMod.MOD_ID, "civilweapons"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.civilized_weapons_group"))
                    .icon(() -> new ItemStack(ModItems.CAST_IRON_PAN)).entries((displayContext, entries) -> {
                        entries.add(ModItems.CAST_IRON_PAN);
                        entries.add(ModItems.IRON_HALBERD);
                        entries.add(ModItems.IRON_GLAIVE);
                        entries.add(ModItems.IRON_SPEAR);
                        entries.add(ModItems.IRON_BROADSWORD);
                        entries.add(ModItems.IRON_SICKLE);
                        entries.add(ModItems.IRON_DUELBLADE);
                        entries.add(ModItems.IRON_SABER);
                        entries.add(ModItems.IRON_FLAIL);
                        entries.add(ModItems.DIAMOND_HALBERD);
                        entries.add(ModItems.DIAMOND_GLAIVE);
                        entries.add(ModItems.DIAMOND_SPEAR);
                        entries.add(ModItems.DIAMOND_BROADSWORD);
                        entries.add(ModItems.DIAMOND_SICKLE);
                        entries.add(ModItems.DIAMOND_DUELBLADE);
                        entries.add(ModItems.DIAMOND_SABER);
                        entries.add(ModItems.DIAMOND_FLAIL);
                        entries.add(ModItems.GOLDEN_HALBERD);
                        entries.add(ModItems.GOLDEN_GLAIVE);
                        entries.add(ModItems.GOLDEN_SPEAR);
                        entries.add(ModItems.GOLDEN_BROADSWORD);
                        entries.add(ModItems.GOLDEN_SICKLE);
                        entries.add(ModItems.GOLDEN_DUELBLADE);
                        entries.add(ModItems.GOLDEN_SABER);
                        entries.add(ModItems.GOLDEN_FLAIL);
                        entries.add(ModItems.NETHERITE_HALBERD);
                        entries.add(ModItems.NETHERITE_GLAIVE);
                        entries.add(ModItems.NETHERITE_SPEAR);
                        entries.add(ModItems.NETHERITE_BROADSWORD);
                        entries.add(ModItems.NETHERITE_SICKLE);
                        entries.add(ModItems.NETHERITE_DUELBLADE);
                        entries.add(ModItems.NETHERITE_SABER);
                        entries.add(ModItems.NETHERITE_FLAIL);
                        entries.add(ModItems.THALLASIUM_BROADSWORD);
                        entries.add(ModItems.THALLASIUM_DUELBLADE);
                        entries.add(ModItems.THALLASIUM_HALBERD);
                        entries.add(ModItems.THALLASIUM_SICKLE);
                        entries.add(ModItems.THALLASIUM_SPEAR);
                        entries.add(ModItems.THALLASIUM_GLAIVE);
                        entries.add(ModItems.THALLASIUM_SABER);
                        entries.add(ModItems.THALLASIUM_FLAIL);
                        entries.add(ModItems.TERMINITE_BROADSWORD);
                        entries.add(ModItems.TERMINITE_DUELBLADE);
                        entries.add(ModItems.TERMINITE_HALBERD);
                        entries.add(ModItems.TERMINITE_SICKLE);
                        entries.add(ModItems.TERMINITE_SPEAR);
                        entries.add(ModItems.TERMINITE_GLAIVE);
                        entries.add(ModItems.TERMINITE_SABER);
                        entries.add(ModItems.TERMINITE_FLAIL);
                        entries.add(ModItems.AETERNIUM_BROADSWORD);
                        entries.add(ModItems.AETERNIUM_DUELBLADE);
                        entries.add(ModItems.AETERNIUM_HALBERD);
                        entries.add(ModItems.AETERNIUM_SICKLE);
                        entries.add(ModItems.AETERNIUM_SPEAR);
                        entries.add(ModItems.AETERNIUM_GLAIVE);
                        entries.add(ModItems.AETERNIUM_SABER);
                        entries.add(ModItems.AETERNIUM_FLAIL);
                        entries.add(ModItems.BEDROCK_BROADSWORD);
                        entries.add(ModItems.BEDROCK_HALBERD);
                        entries.add(ModItems.BEDROCK_GLAIVE);
                        entries.add(ModItems.BEDROCK_FLAIL);
                        entries.add(ModItems.BEDROCK_SPEAR);
                        entries.add(ModItems.BEDROCK_DUELBLADE);
                        entries.add(ModItems.BEDROCK_SICKLE);
                        entries.add(ModItems.BEDROCK_SABER);
                        entries.add(ModItems.THALLASIUMCUBEITEM);
                        entries.add(ModItems.TERMINITECUBEITEM);
                        entries.add(ModItems.AETERNIUMCUBEITEM);

                    }).build());


    public static void registerItemGroups() {
        CivilizedWeaponsMod.LOGGER.info("Registering Item Groups for " + CivilizedWeaponsMod.MOD_ID);
    }
}
