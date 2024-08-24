package net.happyspeed.civilized_weapons.item;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum ModToolMaterial implements ToolMaterial {
    CAST_IRON(3, 650, 1.5f, 0.0f, 20,
            () -> Ingredient.ofItems(Items.IRON_INGOT)),
    TERMINITECUBE(MiningLevels.DIAMOND, 300, 2.0f, 0.0f, 20, () -> Ingredient.ofItems(ModItems.TERMINITECUBEITEM)),
    THALLASIUMCUBE(MiningLevels.DIAMOND, 300, 2.0f, 0.0f, 20, () -> Ingredient.ofItems(ModItems.THALLASIUMCUBEITEM)),
    AETERNIUMCUBE(MiningLevels.DIAMOND, 300, 2.0f, 0.0f, 20, () -> Ingredient.ofItems(ModItems.AETERNIUMCUBEITEM)),
    BEDROCKREPAIR(MiningLevels.NETHERITE, 300, 2.0f, 0.0f, 20, () -> Ingredient.ofItems(Items.GUNPOWDER));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
