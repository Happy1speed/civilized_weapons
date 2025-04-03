package net.happyspeed.civilized_weapons.util;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(CivilizedWeaponsMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TWOHANDED_ITEM_TAG =
                createTag("twohanded");
        public static final TagKey<Item> ALLOWED_OFFHAND_ITEMS =
                createTag("allowed_offhand_items");
        public static final TagKey<Item> GLAIVE =
                createTag("glaive");
        public static final TagKey<Item> SICKLE =
                createTag("sickle");
        public static final TagKey<Item> BROADSWORD =
                createTag("broadsword");
        public static final TagKey<Item> DUELBLADE =
                createTag("duelblade");
        public static final TagKey<Item> SPEAR =
                createTag("spear");
        public static final TagKey<Item> HALBERD =
                createTag("halberd");
        public static final TagKey<Item> PAN =
                createTag("pan");
        public static final TagKey<Item> SABER =
                createTag("saber");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(CivilizedWeaponsMod.MOD_ID, name));
        }
    }
}
