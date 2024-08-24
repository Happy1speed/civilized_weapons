package net.happyspeed.civilized_weapons.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.happyspeed.civilized_weapons.enchantments.ModEnchantments;
import net.happyspeed.civilized_weapons.item.custom.AdvancedWeaponTemplate;
import net.happyspeed.civilized_weapons.item.custom.PanItemTemplate;
import net.happyspeed.civilized_weapons.network.PlayerAttackPacket;
//import net.happyspeed.civilized_weapons.network.PlayerDualHandPacket;
import net.happyspeed.civilized_weapons.sounds.ModSounds;
import net.happyspeed.civilized_weapons.util.ModTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static net.minecraft.entity.EquipmentSlot.OFFHAND;

@Mixin(value = PlayerEntity.class, priority = 501)
abstract class PlayerEntityMixin extends LivingEntity implements PlayerClassAccess {

    @Unique
    public float prevAttackStrength = 0.0f;

    @Unique
    public int attackBlockTimer = 1;

    @Unique
    public ItemStack lastSelectedItem = null;

    @Unique
    public int ticksSinceItemSwap = 20;

    @Unique
    public boolean playerParryBlocking;

    @Unique
    public ItemStack heldItemLastTick;

    @Unique
    public ArrayList<ArrowEntity> alreadyhit = new ArrayList<>();

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract float getAttackCooldownProgress(float baseTime);

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract boolean canEquip(ItemStack stack);

    @Shadow @Final private PlayerInventory inventory;

    @Shadow public abstract void increaseStat(Stat<?> stat, int amount);

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow public abstract void useRiptide(int riptideTicks);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    public float civilized_weapons$getPrevAttackCooldown() {
        return this.prevAttackStrength;
    }

    @Override
    public ItemStack civilized_weapons$getHeldItemLastTick() {
        return this.heldItemLastTick;
    }

    @Override
    public void civilized_weapons$setAttackBlockTimer() {
        this.attackBlockTimer = 0;
    }

    @Override
    public void civilized_weapons$setPlayerParryBlocking() {
        this.playerParryBlocking = true;
    }

    @Override
    public int civilized_weapons$getTicksSinceLastItemSwap() {
        return this.ticksSinceItemSwap;
    }

    @Unique
    public void civilized_weapons$swingingDetected(LivingEntity living) {
        if (living instanceof PlayerEntity player) {
            if (this.heldItemLastTick != null && !this.getWorld().isClient()) {
                if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
                    advancedWeaponTemplate.activeHit(this);
                }
            }
            this.attackBlockTimer = 0;
            this.playerParryBlocking = true;
        }
    }

    @Unique
    boolean isInView(LivingEntity player, ArrowEntity arrow) {
        Vec3d playerLookDir = player.getRotationVec(1.0f);
        Vec3d projectileDir = (arrow.getPos().subtract(player.getPos())).normalize();
        double angle = playerLookDir.dotProduct(projectileDir);
        if (angle > 0.3) {
            return player.canSee(arrow);
        }
        return false;
    }

    @Unique
    public void spawnParryParticles(LivingEntity player, Vec3d projectilePosition) {
        if (player.getWorld() instanceof ServerWorld) {
            for (int k = 0; k < 11; k++) {
                Random random = new Random();
                float offsetx = random.ints(-20, 20).findFirst().getAsInt();
                offsetx = offsetx / 100;
                Random random3 = new Random();
                float offsetz = random3.ints(-20, 20).findFirst().getAsInt();
                offsetz = offsetz / 100;
                Random random2 = new Random();
                float offsety = random2.ints(-20, 20).findFirst().getAsInt();
                offsety = offsety / 100;
                Random random4 = new Random();
                float speed = random4.ints(4, 8).findFirst().getAsInt();
                speed = speed / 100;
                ((ServerWorld) player.getWorld()).spawnParticles(ParticleTypes.CRIT, projectilePosition.x, projectilePosition.y, projectilePosition.z, 1, offsetx, offsety, offsetz, speed);
            }
        }
    }

    @Inject(method = "getEquippedStack", at = @At("HEAD"), cancellable = true)
    public void getEquippedStack(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        var mainHandHasTwoHanded = false;
        var offHandHasTwoHanded = false;
        var offHandStack = ((PlayerEntityAccessor) this).getInventory().offHand.get(0);
        var mainHandStack = ((PlayerEntityAccessor) this).getInventory().getMainHandStack();
        if (mainHandStack.isIn(ModTags.Items.TWOHANDED_ITEM_TAG)) {
            if (!offHandStack.isIn(ModTags.Items.ALLOWED_OFFHAND_ITEMS)) {
                mainHandHasTwoHanded = true;
            }
        }
        offHandHasTwoHanded = offHandStack.isIn(ModTags.Items.TWOHANDED_ITEM_TAG);

        if (slot == OFFHAND) {
            if (mainHandHasTwoHanded || offHandHasTwoHanded) {
                cir.setReturnValue(ItemStack.EMPTY);
                cir.cancel();
            }
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    public void recordPrevAttackStrength(Entity target, CallbackInfo ci) {
        this.civilized_weapons$swingingDetected(this);
        this.playerParryBlocking = false;
        this.attackBlockTimer = 5;
        this.prevAttackStrength = this.getAttackCooldownProgress(1.0f);
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            advancedWeaponTemplate.prevAttackProgress = this.getAttackCooldownProgress(1.0f);
            advancedWeaponTemplate.wasSprinting = this.isSprinting();
            advancedWeaponTemplate.prevReceivedDamage = this.lastDamageTaken;
            if (target instanceof LivingEntity living) {
                advancedWeaponTemplate.BeforePostHit(this.getMainHandStack(), living, this);
            }
        }
    }

    //Fun is not something one takes into account when balancing the mod, but this does put a smile on my face
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void blockProjectiles(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getWorld().isClient() && this.playerParryBlocking && source.getName().equals("arrow") && (EnchantmentHelper.getLevel(ModEnchantments.DEFENDER, this.getEquippedStack(EquipmentSlot.MAINHAND)) > 0)) {
            List<ArrowEntity> list = this.getWorld().getNonSpectatingEntities(ArrowEntity.class, this.getBoundingBox().expand(3.0, 4.0, 3.0));
            for (ArrowEntity projectile : list) {
                Vec3d projectilePosition = new Vec3d(projectile.getX(), projectile.getY(), projectile.getZ());
                if (!projectile.getWorld().isSpaceEmpty(new Box(projectile.getPos(), projectile.getPos()).expand(0.06)) ||
                        this.alreadyhit.contains(projectile) || !this.isInView(this, projectile))
                    continue;
                projectile.updatePositionAndAngles(projectile.getX() - (projectile.getVelocity().getX() * 0.8),
                        projectile.getY() - (projectile.getVelocity().getY() * 0.8), projectile.getZ() - (projectile.getVelocity().getZ() * 0.8),this.getYaw(), this.getPitch());
                this.spawnParryParticles(this, projectilePosition);
                this.getWorld().playSound(null, projectilePosition.x, projectilePosition.y, projectilePosition.z, ModSounds.PANHITSOUND, this.getSoundCategory(), 0.8f, 1.3f);
                projectile.setVelocity((MathHelper.sin(this.getYaw() * ((float) Math.PI / 180))) * 3 ,-1.5, (-MathHelper.cos(this.getYaw() * ((float) Math.PI / 180))) * 3);
                projectile.velocityModified = true;
                projectile.setOwner(this);
                this.alreadyhit.add(projectile);
                cir.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void testForItemSwitch(CallbackInfo ci) {
        this.heldItemLastTick = this.getMainHandStack();
        if (this.ticksSinceItemSwap < 200) {
            this.ticksSinceItemSwap++;
        }
        if (this.getMainHandStack().getItem() != null) {
            if (this.lastSelectedItem != null) {
                if (this.getMainHandStack().getItem() != this.lastSelectedItem.getItem()) {
                    this.lastSelectedItem = this.getMainHandStack().getItem().getDefaultStack();
                    this.ticksSinceItemSwap = 0;
                }
            } else {
                this.lastSelectedItem = this.getMainHandStack().getItem().getDefaultStack();
                this.ticksSinceItemSwap = 0;
                if (this.attackBlockTimer < 5) {
                    this.attackBlockTimer = 5;
                    this.playerParryBlocking = false;
                }
            }
        }
        if (this.attackBlockTimer < 5) {
            this.attackBlockTimer++;
        } else {
            this.playerParryBlocking = false;
            this.alreadyhit.clear();
        }
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            advancedWeaponTemplate.ticker(this);
            if (EnchantmentHelper.getLevel(ModEnchantments.TRUSTWORTHY, this.getEquippedStack(EquipmentSlot.MAINHAND)) > 0) {
                advancedWeaponTemplate.tickTrustEnchant(this);
            }
        }
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=1.5F", ordinal = 0))
    public float criticalMultiplier(float constant) {
        if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            return advancedWeaponTemplate.weaponCriticalMultiplier;
        }
        else {
            return 1.5f;
        }
    }
//    @Redirect(method = "attack", at = @At(value = "INVOKE",
//            target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"))
//    public void setStackInHand_Redirect(PlayerEntity instance, Hand handArg, ItemStack itemStack) {
//        // DUAL WIELDING LOGIC
//        // In case item got destroyed due to durability loss
//        // We empty the correct hand
//        if (comboCount < 0) {
//            // Vanilla behaviour
//            instance.setStackInHand(handArg, itemStack);
//        }
//        // `handArg` argument is always `MAIN`, we can ignore it
//        AttackHand hand = lastAttack;
//        if (hand == null) {
//            hand = PlayerAttackHelper.getCurrentAttack(instance, comboCount);
//        }
//        if (hand == null) {
//            instance.setStackInHand(handArg, itemStack);
//            return;
//        }
//        var redirectedHand = hand.isOffHand() ? Hand.OFF_HAND : Hand.MAIN_HAND;
//        instance.setStackInHand(redirectedHand, itemStack);
//    }

//    @Override
//    public float getHandSwingProgress(float tickDelta) {
//        float f = this.handSwingProgress - this.lastHandSwingProgress;
//        if (f < 0.0f) {
//            f += 1.0f;
//        }
//        if (this.getAttackCooldownProgress(tickDelta) < 1.0 && this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED) < 4.0) {
//            return this.getAttackCooldownProgress(tickDelta);
//        } else {
//            return this.lastHandSwingProgress + f * tickDelta;
//        }
//    }
}
