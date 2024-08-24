package net.happyspeed.civilized_weapons.mixin;

import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerEntity.class, priority = 1000)
abstract class PlayerEntityCorrectSweepMixin extends LivingEntity implements PlayerClassAccess {
    @Shadow public abstract float getMovementSpeed();

    protected PlayerEntityCorrectSweepMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "spawnSweepAttackParticles", at = @At("HEAD"), cancellable = true)
    public void fixSweepAttackParticles(CallbackInfo ci) {
        double d = -MathHelper.sin(this.getYaw() * ((float)Math.PI / 180)) * Math.min(2.0, (this.getMovementSpeed() * ((this.isSprinting()) ? 16 : 15)));
        double e = MathHelper.cos(this.getYaw() * ((float)Math.PI / 180)) * Math.min(2.0, (this.getMovementSpeed() * ((this.isSprinting()) ? 16 : 15)));
        if (this.getWorld() instanceof ServerWorld) {
            ((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + d, this.getBodyY(0.5), this.getZ() + e, 0, d, 0.0, e, 0.0);
        }
        ci.cancel();
    }
}
