package net.happyspeed.civilized_weapons.mixin;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


//Sorry if this causes conflicts. I don't know how to mix into registries yet.
@Mixin(value = SwordItem.class)
public class SwordItemMixin extends ToolItem implements Vanishable {

    public SwordItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/EntityAttributeModifier;<init>(Ljava/util/UUID;Ljava/lang/String;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)V", ordinal = 1), index = 2)
    private double attackSpeedModified(double value) {
        return -2.2;
    }
}
