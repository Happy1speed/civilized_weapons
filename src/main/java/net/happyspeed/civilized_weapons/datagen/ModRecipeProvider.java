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
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.CAST_IRON_PAN, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.IRON_BROADSWORD, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.IRON_DUELBLADE, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.IRON_FLAIL, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.IRON_GLAIVE, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.IRON_HALBERD, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.IRON_SABER, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.IRON_SICKLE, Items.IRON_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.IRON_SPEAR, Items.IRON_SWORD);

        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.DIAMOND_BROADSWORD, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.DIAMOND_DUELBLADE, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.DIAMOND_FLAIL, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.DIAMOND_GLAIVE, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.DIAMOND_HALBERD, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.DIAMOND_SABER, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.DIAMOND_SICKLE, Items.DIAMOND_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.DIAMOND_SPEAR, Items.DIAMOND_SWORD);

        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.NETHERITE_BROADSWORD, Items.NETHERITE_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.NETHERITE_FLAIL, Items.NETHERITE_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.NETHERITE_DUELBLADE, Items.NETHERITE_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.NETHERITE_GLAIVE, Items.NETHERITE_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.NETHERITE_SABER, Items.NETHERITE_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.NETHERITE_SICKLE, Items.NETHERITE_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.NETHERITE_SPEAR, Items.NETHERITE_SWORD);
        offerStonecuttingRecipe(exporter, RecipeCategory.MISC, ModItems.NETHERITE_HALBERD, Items.NETHERITE_SWORD);
    }
}
