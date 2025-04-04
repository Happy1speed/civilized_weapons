package net.happyspeed.civilized_weapons.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.happyspeed.civilized_weapons.item.ModItems;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.CAST_IRON_PAN, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.IRON_BROADSWORD, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.IRON_DUALBLADE, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.IRON_GLAIVE, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.IRON_HALBERD, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.IRON_SABER, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.IRON_KUKRI, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.IRON_SPEAR, Items.IRON_SWORD);

        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.DIAMOND_BROADSWORD, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.DIAMOND_DUALBLADE, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.DIAMOND_GLAIVE, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.DIAMOND_HALBERD, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.DIAMOND_SABER, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.DIAMOND_KUKRI, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.COMBAT, ModItems.DIAMOND_SPEAR, Items.DIAMOND_SWORD);
        
        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_BROADSWORD,  RecipeCategory.COMBAT, ModItems.NETHERITE_BROADSWORD);
        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_DUALBLADE,  RecipeCategory.COMBAT, ModItems.NETHERITE_DUALBLADE);
        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_GLAIVE,  RecipeCategory.COMBAT, ModItems.NETHERITE_GLAIVE);
        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_HALBERD,  RecipeCategory.COMBAT, ModItems.NETHERITE_HALBERD);
        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_SABER,  RecipeCategory.COMBAT, ModItems.NETHERITE_SABER);
        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_KUKRI,  RecipeCategory.COMBAT, ModItems.NETHERITE_KUKRI);
        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_SPEAR,  RecipeCategory.COMBAT, ModItems.NETHERITE_SPEAR);
    }
}
