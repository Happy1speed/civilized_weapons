package net.happyspeed.civilized_weapons.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.item.custom.*;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModItems {
    //Iron Weapons
    public static final Item CAST_IRON_PAN = registerItem("cast_iron_pan",
            new PanItemTemplate(new FabricItemSettings().maxDamage(700)));
    public static final Item IRON_HALBERD = registerItem("iron_halberd",
            new HalberdItemTemplate(ToolMaterials.IRON, 6.0F, new FabricItemSettings()));
    public static final Item IRON_KUKRI = registerItem("iron_kukri",
            new KukriItemTemplate(ToolMaterials.IRON, 4.0F,new FabricItemSettings()));
    public static final Item IRON_DUALBLADE = registerItem("iron_dualblade",
              new DualbladeItemTemplate(ToolMaterials.IRON, 5.0F,new FabricItemSettings()));
    public static final Item IRON_SABER = registerItem("iron_saber",
            new SaberItemTemplate(ToolMaterials.IRON, 4.0F,new FabricItemSettings()));
    public static final Item IRON_SPEAR = registerItem("iron_spear",
            new SpearItemTemplate(ToolMaterials.IRON, 4.0F, new FabricItemSettings()));
    public static final Item IRON_BROADSWORD = registerItem("iron_broadsword",
            new BroadswordItemTemplate(ToolMaterials.IRON, 6.0F, new FabricItemSettings()));
    public static final Item IRON_GLAIVE = registerItem("iron_glaive",
            new GlaiveItemTemplate(ToolMaterials.IRON, 5.0F, new FabricItemSettings()));

    //Diamond Weapons
    public static final Item DIAMOND_HALBERD = registerItem("diamond_halberd",
            new HalberdItemTemplate(ToolMaterials.DIAMOND, 7.0F, new FabricItemSettings()));
    public static final Item DIAMOND_KUKRI = registerItem("diamond_kukri",
            new KukriItemTemplate(ToolMaterials.DIAMOND, 5.0F,new FabricItemSettings()));
    public static final Item DIAMOND_DUALBLADE = registerItem("diamond_dualblade",
            new DualbladeItemTemplate(ToolMaterials.DIAMOND, 6.0F,new FabricItemSettings()));
    public static final Item DIAMOND_SABER = registerItem("diamond_saber",
            new SaberItemTemplate(ToolMaterials.DIAMOND, 5.0F,new FabricItemSettings()));
    public static final Item DIAMOND_SPEAR = registerItem("diamond_spear",
            new SpearItemTemplate(ToolMaterials.DIAMOND, 5.0F, new FabricItemSettings()));
    public static final Item DIAMOND_BROADSWORD = registerItem("diamond_broadsword",
            new BroadswordItemTemplate(ToolMaterials.DIAMOND, 7.0F, new FabricItemSettings()));
    public static final Item DIAMOND_GLAIVE = registerItem("diamond_glaive",
            new GlaiveItemTemplate(ToolMaterials.DIAMOND, 6.0F, new FabricItemSettings()));

    public static final Item GOLDEN_HALBERD = registerItem("golden_halberd",
            new HalberdItemTemplate(ToolMaterials.GOLD, 5.0F, new FabricItemSettings()));
    public static final Item GOLDEN_KUKRI = registerItem("golden_kukri",
            new KukriItemTemplate(ToolMaterials.GOLD, 3.0F,new FabricItemSettings()));
    public static final Item GOLDEN_DUALBLADE = registerItem("golden_dualblade",
            new DualbladeItemTemplate(ToolMaterials.GOLD, 4.0F,new FabricItemSettings()));
    public static final Item GOLDEN_SABER = registerItem("golden_saber",
            new SaberItemTemplate(ToolMaterials.GOLD, 3.0F,new FabricItemSettings()));
    public static final Item GOLDEN_SPEAR = registerItem("golden_spear",
            new SpearItemTemplate(ToolMaterials.GOLD, 3.0F, new FabricItemSettings()));
    public static final Item GOLDEN_BROADSWORD = registerItem("golden_broadsword",
            new BroadswordItemTemplate(ToolMaterials.GOLD, 5.0F, new FabricItemSettings()));
    public static final Item GOLDEN_GLAIVE = registerItem("golden_glaive",
            new GlaiveItemTemplate(ToolMaterials.GOLD, 4.0F, new FabricItemSettings()));

    public static final Item NETHERITE_HALBERD = registerItem("netherite_halberd",
            new HalberdItemTemplate(ToolMaterials.NETHERITE, 8.0F, new FabricItemSettings()));
    public static final Item NETHERITE_KUKRI = registerItem("netherite_kukri",
            new KukriItemTemplate(ToolMaterials.NETHERITE, 6.0F,new FabricItemSettings()));
    public static final Item NETHERITE_DUALBLADE = registerItem("netherite_dualblade",
            new DualbladeItemTemplate(ToolMaterials.NETHERITE, 7.0F,new FabricItemSettings()));
    public static final Item NETHERITE_SABER = registerItem("netherite_saber",
            new SaberItemTemplate(ToolMaterials.NETHERITE, 6.0F,new FabricItemSettings()));
    public static final Item NETHERITE_SPEAR = registerItem("netherite_spear",
            new SpearItemTemplate(ToolMaterials.NETHERITE, 6.0F, new FabricItemSettings()));
    public static final Item NETHERITE_BROADSWORD = registerItem("netherite_broadsword",
            new BroadswordItemTemplate(ToolMaterials.NETHERITE, 8.0F, new FabricItemSettings()));
    public static final Item NETHERITE_GLAIVE = registerItem("netherite_glaive",
            new GlaiveItemTemplate(ToolMaterials.NETHERITE, 7.0F, new FabricItemSettings()));

    public static final Item THALLASIUM_HALBERD = registerItem("thallasium_halberd",
            new HalberdItemTemplate(ModToolMaterial.THALLASIUMCUBE, 6.0F, new FabricItemSettings()));
    public static final Item THALLASIUM_KUKRI = registerItem("thallasium_kukri",
            new KukriItemTemplate(ModToolMaterial.THALLASIUMCUBE, 4.0F,new FabricItemSettings()));
    public static final Item THALLASIUM_DUALBLADE = registerItem("thallasium_dualblade",
            new DualbladeItemTemplate(ModToolMaterial.THALLASIUMCUBE, 5.0F,new FabricItemSettings()));
    public static final Item THALLASIUM_SABER = registerItem("thallasium_saber",
            new SaberItemTemplate(ModToolMaterial.THALLASIUMCUBE, 4.0F,new FabricItemSettings()));
    public static final Item THALLASIUM_SPEAR = registerItem("thallasium_spear",
            new SpearItemTemplate(ModToolMaterial.THALLASIUMCUBE, 4.0F, new FabricItemSettings()));
    public static final Item THALLASIUM_BROADSWORD = registerItem("thallasium_broadsword",
            new BroadswordItemTemplate(ModToolMaterial.THALLASIUMCUBE, 6.0F, new FabricItemSettings()));
    public static final Item THALLASIUM_GLAIVE = registerItem("thallasium_glaive",
            new GlaiveItemTemplate(ModToolMaterial.THALLASIUMCUBE, 5.0F, new FabricItemSettings()));

    public static final Item TERMINITE_HALBERD = registerItem("terminite_halberd",
            new HalberdItemTemplate(ModToolMaterial.TERMINITECUBE, 7.0F, new FabricItemSettings()));
    public static final Item TERMINITE_KUKRI = registerItem("terminite_kukri",
            new KukriItemTemplate(ModToolMaterial.TERMINITECUBE, 5.0F,new FabricItemSettings()));
    public static final Item TERMINITE_DUALBLADE = registerItem("terminite_dualblade",
            new DualbladeItemTemplate(ModToolMaterial.TERMINITECUBE, 6.0F,new FabricItemSettings()));
    public static final Item TERMINITE_SABER = registerItem("terminite_saber",
            new SaberItemTemplate(ModToolMaterial.TERMINITECUBE, 5.0F,new FabricItemSettings()));
    public static final Item TERMINITE_SPEAR = registerItem("terminite_spear",
            new SpearItemTemplate(ModToolMaterial.TERMINITECUBE, 5.0F, new FabricItemSettings()));
    public static final Item TERMINITE_BROADSWORD = registerItem("terminite_broadsword",
            new BroadswordItemTemplate(ModToolMaterial.TERMINITECUBE, 7.0F, new FabricItemSettings()));
    public static final Item TERMINITE_GLAIVE = registerItem("terminite_glaive",
            new GlaiveItemTemplate(ModToolMaterial.TERMINITECUBE, 6.0F, new FabricItemSettings()));

    public static final Item AETERNIUM_HALBERD = registerItem("aeternium_halberd",
            new HalberdItemTemplate(ModToolMaterial.AETERNIUMCUBE, 8.5F, new FabricItemSettings()));
    public static final Item AETERNIUM_KUKRI = registerItem("aeternium_kukri",
            new KukriItemTemplate(ModToolMaterial.AETERNIUMCUBE, 6.5F,new FabricItemSettings()));
    public static final Item AETERNIUM_DUALBLADE = registerItem("aeternium_dualblade",
            new DualbladeItemTemplate(ModToolMaterial.AETERNIUMCUBE, 7.5F,new FabricItemSettings()));
    public static final Item AETERNIUM_SABER = registerItem("aeternium_saber",
            new SaberItemTemplate(ModToolMaterial.AETERNIUMCUBE, 6.5F,new FabricItemSettings()));
    public static final Item AETERNIUM_SPEAR = registerItem("aeternium_spear",
            new SpearItemTemplate(ModToolMaterial.AETERNIUMCUBE, 6.5F, new FabricItemSettings()));
    public static final Item AETERNIUM_BROADSWORD = registerItem("aeternium_broadsword",
            new BroadswordItemTemplate(ModToolMaterial.AETERNIUMCUBE, 8.5F, new FabricItemSettings()));
    public static final Item AETERNIUM_GLAIVE = registerItem("aeternium_glaive",
            new GlaiveItemTemplate(ModToolMaterial.AETERNIUMCUBE, 7.5F, new FabricItemSettings()));

    //Bedrock Weapons
    public static final Item BEDROCK_HALBERD = registerItem("bedrock_halberd",
            new HalberdItemTemplate(ModToolMaterial.BEDROCKREPAIR, 9.0F, new FabricItemSettings()));
    public static final Item BEDROCK_KUKRI = registerItem("bedrock_kukri",
            new KukriItemTemplate(ModToolMaterial.BEDROCKREPAIR, 7.0F,new FabricItemSettings()));
    public static final Item BEDROCK_DUALBLADE = registerItem("bedrock_dualblade",
            new DualbladeItemTemplate(ModToolMaterial.BEDROCKREPAIR, 8.0F,new FabricItemSettings()));
    public static final Item BEDROCK_SABER = registerItem("bedrock_saber",
            new SaberItemTemplate(ModToolMaterial.BEDROCKREPAIR, 7.0F,new FabricItemSettings()));
    public static final Item BEDROCK_SPEAR = registerItem("bedrock_spear",
            new SpearItemTemplate(ModToolMaterial.BEDROCKREPAIR, 7.0F, new FabricItemSettings()));
    public static final Item BEDROCK_BROADSWORD = registerItem("bedrock_broadsword",
            new BroadswordItemTemplate(ModToolMaterial.BEDROCKREPAIR, 9.0F, new FabricItemSettings()));
    public static final Item BEDROCK_GLAIVE = registerItem("bedrock_glaive",
            new GlaiveItemTemplate(ModToolMaterial.BEDROCKREPAIR, 8.0F, new FabricItemSettings()));

    public static final Item TERMINITECUBEITEM = registerItem("terminite_cube_item", new Item(new FabricItemSettings()));
    public static final Item THALLASIUMCUBEITEM = registerItem("thallasium_cube_item", new Item(new FabricItemSettings()));
    public static final Item AETERNIUMCUBEITEM = registerItem("aeternium_cube_item", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(CAST_IRON_PAN);
        entries.add(IRON_HALBERD);
        entries.add(IRON_BROADSWORD);
        entries.add(IRON_GLAIVE);
        entries.add(IRON_SPEAR);
        entries.add(IRON_KUKRI);
        entries.add(IRON_DUALBLADE);
        entries.add(IRON_SABER);
        entries.add(DIAMOND_BROADSWORD);
        entries.add(DIAMOND_HALBERD);
        entries.add(DIAMOND_GLAIVE);
        entries.add(DIAMOND_SPEAR);
        entries.add(DIAMOND_KUKRI);
        entries.add(DIAMOND_DUALBLADE);
        entries.add(DIAMOND_SABER);
        entries.add(GOLDEN_BROADSWORD);
        entries.add(GOLDEN_HALBERD);
        entries.add(GOLDEN_GLAIVE);
        entries.add(GOLDEN_SPEAR);
        entries.add(GOLDEN_KUKRI);
        entries.add(GOLDEN_DUALBLADE);
        entries.add(GOLDEN_SABER);
        entries.add(NETHERITE_BROADSWORD);
        entries.add(NETHERITE_HALBERD);
        entries.add(NETHERITE_GLAIVE);
        entries.add(NETHERITE_SABER);
        entries.add(NETHERITE_KUKRI);
        entries.add(NETHERITE_DUALBLADE);
        entries.add(NETHERITE_SPEAR);
        entries.add(BEDROCK_BROADSWORD);
        entries.add(BEDROCK_HALBERD);
        entries.add(BEDROCK_GLAIVE);
        entries.add(BEDROCK_SABER);
        entries.add(BEDROCK_KUKRI);
        entries.add(BEDROCK_DUALBLADE);
        entries.add(BEDROCK_SPEAR);
        entries.add(AETERNIUMCUBEITEM);
        entries.add(THALLASIUMCUBEITEM);
        entries.add(TERMINITECUBEITEM);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(CivilizedWeaponsMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CivilizedWeaponsMod.LOGGER.info("Registering Mod Items for " + CivilizedWeaponsMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);

    }
}
