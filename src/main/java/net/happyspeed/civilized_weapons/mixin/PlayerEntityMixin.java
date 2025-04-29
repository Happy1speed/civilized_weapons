package net.happyspeed.civilized_weapons.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.happyspeed.civilized_weapons.enchantments.ModEnchantments;
import net.happyspeed.civilized_weapons.item.custom.AdvancedWeaponTemplate;
import net.happyspeed.civilized_weapons.item.custom.KukriItemTemplate;
import net.happyspeed.civilized_weapons.item.custom.SaberItemTemplate;
import net.happyspeed.civilized_weapons.item.custom.SpearItemTemplate;
import net.happyspeed.civilized_weapons.network.S2CHandSyncPacket;
import net.happyspeed.civilized_weapons.sounds.ModSounds;
import net.happyspeed.civilized_weapons.util.CivilizedHelper;
import net.happyspeed.civilized_weapons.util.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

import static net.happyspeed.civilized_weapons.CivilizedWeaponsMod.dualWieldingSpeedModifierId;
import static net.minecraft.entity.EquipmentSlot.MAINHAND;
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
    public int ticksSinceHit;


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

    @Shadow public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    @Shadow public abstract PlayerInventory getInventory();

    @Shadow public abstract ItemCooldownManager getItemCooldownManager();

    @Shadow public abstract float getMovementSpeed();

    @Shadow public abstract boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource);

    @Shadow public abstract float getLuck();

    @Shadow public abstract PlayerAbilities getAbilities();

    @Unique public Hand lastAttackHand = Hand.MAIN_HAND;

    @Unique public boolean hasSwappedThisTick = false;

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

    @Override
    public int civilized_weapons$getTicksSinceHit() {
        return this.ticksSinceHit;
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
        if (mainHandStack.isIn(ModTags.Items.TWOHANDED_ITEM_TAG) && !(EnchantmentHelper.getLevel(ModEnchantments.LIGHTWEIGHT, mainHandStack) > 0)) {
            if (!offHandStack.isIn(ModTags.Items.ALLOWED_OFFHAND_ITEMS)) {
                mainHandHasTwoHanded = true;
            }
        }
        offHandHasTwoHanded = offHandStack.isIn(ModTags.Items.TWOHANDED_ITEM_TAG) && !(EnchantmentHelper.getLevel(ModEnchantments.LIGHTWEIGHT, offHandStack) > 0);

        if (slot == OFFHAND) {
            if (mainHandHasTwoHanded || offHandHasTwoHanded) {
                cir.setReturnValue(ItemStack.EMPTY);
                cir.cancel();
            }
        }
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSprinting()Z"))
    public boolean kukruiNoCrit(PlayerEntity instance) {
        if (instance.getMainHandStack().getItem() instanceof KukriItemTemplate) {
            return true;
        }
        if (instance.getMainHandStack().getItem() instanceof SpearItemTemplate) {
            return false;
        }
        return this.isSprinting();
    }

    @Inject(method = "attack", at = @At("HEAD"))
    public void recordPrevAttackStrength(Entity target, CallbackInfo ci) {
        if (!this.getWorld().isClient()) {
            this.civilized_weapons$swingingDetected(this);
            if (!this.hasSwappedThisTick) {
                if (this.lastAttackHand == Hand.MAIN_HAND) {
                    this.civilized_weapons$onSwapDualHands(Hand.OFF_HAND);
                } else {
                    this.civilized_weapons$onSwapDualHands(Hand.MAIN_HAND);
                }
            }
        }
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
        if (!this.getWorld().isClient) {
            this.ticksSinceHit = 0;
        }
        if (!this.getWorld().isClient() && this.playerParryBlocking && source.getName().equals("arrow") && (EnchantmentHelper.getLevel(ModEnchantments.DEFENDER, this.getEquippedStack(MAINHAND)) > 0)) {
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
        updateDualWieldingSpeedBoost();

        this.heldItemLastTick = this.getStackInHand(lastAttackHand);
        if (this.ticksSinceItemSwap < 200) {
            this.ticksSinceItemSwap++;
        }
        if (this.ticksSinceHit < 21) {
            if (this.ticksSinceHit == 0 && this.getItemCooldownManager().getCooldownProgress(this.getStackInHand(lastAttackHand).getItem(), 1.0f) < 0.01) {
                if (this.getStackInHand(lastAttackHand).getItem() instanceof SaberItemTemplate && EnchantmentHelper.getLevel(ModEnchantments.RHYTHM, this.getStackInHand(lastAttackHand)) > 0) {
                    this.setStatusEffect(new StatusEffectInstance(CivilizedWeaponsMod.ADD_ATTACK_DAMAGE_EFFECT, 20, 5, false, true), this);
                    playSound(SoundEvents.BLOCK_NETHERITE_BLOCK_HIT, SoundCategory.PLAYERS, 1.0f, 2.0f);
                    for (int i = 0; i < this.getInventory().size(); i++) {
                        if (this.getInventory().getStack(i).isIn(ModTags.Items.SABER)) {
                            this.getItemCooldownManager().set(this.getInventory().getStack(i).getItem(), 120);
                        }
                    }
                    this.clearActiveItem();
                }
            }
            this.ticksSinceHit++;
        }
        if (this.getMainHandStack().getItem() != null) {
            if (this.lastSelectedItem != null) {
                if (this.getMainHandStack().getItem() != this.lastSelectedItem.getItem()) {
                    this.lastSelectedItem = this.getStackInHand(lastAttackHand).getItem().getDefaultStack();
                    this.ticksSinceItemSwap = 0;
                }
            } else {
                this.lastSelectedItem = this.getStackInHand(lastAttackHand).getItem().getDefaultStack();
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
        if (this.getStackInHand(lastAttackHand).getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            advancedWeaponTemplate.ticker(this);
            if (EnchantmentHelper.getLevel(ModEnchantments.TRUSTWORTHY, this.getStackInHand(lastAttackHand)) > 0) {
                advancedWeaponTemplate.tickTrustEnchant(this);
            }
        }
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=1.5F", ordinal = 0))
    public float criticalMultiplier(float constant, @Local(ordinal = 0, argsOnly = true) Entity target) {

        float crit = 0;
        if (this.getStatusEffect(CivilizedWeaponsMod.CRITICAL_BOOST_EFFECT) != null) {
            crit += (float) (this.getStatusEffect(CivilizedWeaponsMod.CRITICAL_BOOST_EFFECT).getAmplifier() * 0.5);
        }
        if (this.getStackInHand(lastAttackHand).getItem() instanceof SpearItemTemplate && this.isSprinting() && !this.isSneaking()) {
            crit += 0.4f;
        }
        if (this.getStackInHand(lastAttackHand).getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            crit += advancedWeaponTemplate.weaponCriticalMultiplier;
        } else {
            crit += 1.5f;
        }
        if (this.getStackInHand(this.lastAttackHand).getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
            if (EnchantmentHelper.getLevel(ModEnchantments.ASCEND, this.getStackInHand(this.lastAttackHand)) > 0) {
                if (advancedWeaponTemplate.prevAttackProgress >= 1.0f && this.fallDistance > 0.0f && !this.isOnGround() && !this.isClimbing() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && !this.hasVehicle()) {
                    target.setVelocity(new Vec3d(target.getVelocity().getX(), 0.7, target.getVelocity().getZ()));
                    target.velocityModified = true;
                }
            }
        }

        return crit;
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tailTick(CallbackInfo ci) {
        this.hasSwappedThisTick = false;
    }



    @Unique
    private void updateDualWieldingSpeedBoost() {

        var player = ((PlayerEntity) ((Object) this));

        var isDuel = CivilizedHelper.isDualWielding(player);


        EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(dualWieldingSpeedModifierId, EntityAttributes.GENERIC_ATTACK_SPEED.getTranslationKey(), 0.5, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);

        if (entityAttributeInstance != null) {
            if (isDuel && !entityAttributeInstance.hasModifier(entityAttributeModifier)) {
                entityAttributeInstance.addTemporaryModifier(entityAttributeModifier);
            }
            else if (!isDuel && entityAttributeInstance.hasModifier(entityAttributeModifier)) {
                entityAttributeInstance.removeModifier(entityAttributeModifier);
            }
        }
    }

    @Inject(method = "attack", at = @At(value = "HEAD"))
    public void hookforHandSwitch(Entity target, CallbackInfo ci) {
        if (this.heldItemLastTick != null && !this.getWorld().isClient()) {
            if (this.getMainHandStack().getItem() instanceof AdvancedWeaponTemplate advancedWeaponTemplate) {
                advancedWeaponTemplate.activeHit(this);
            }
        }
        this.attackBlockTimer = 0;
        this.playerParryBlocking = true;
    }

    @ModifyArg(method = "attack", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"),
            index = 0)
    public Hand getHand(Hand hand2) {
        var player = ((PlayerEntity) ((Object)this));
        return ((PlayerClassAccess) player).civilized_weapons$getLastAttackHandInverse();
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;"))
    public ItemStack getMainHandStack_Redirect(PlayerEntity instance) {
        // DUAL WIELDING LOGIC
        // Here we return the off-hand stack as fake main-hand, purpose:
        // - Getting enchants
        // - Getting itemstack to be damaged

        return this.getStackInHand(((PlayerClassAccess) instance).civilized_weapons$getLastAttackHandInverse());
    }



    @Redirect(method = "attack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"))
    public void setStackInHand_Redirect(PlayerEntity instance, Hand handArg, ItemStack itemStack) {
        // DUAL WIELDING LOGIC
        // In case item got destroyed due to durability loss
        // We empty the correct hand

        Hand hand = ((PlayerClassAccess) instance).civilized_weapons$getLastAttackHandInverse();
        if (hand == null) {
            instance.setStackInHand(handArg, itemStack);
            return;
        }
        instance.setStackInHand(hand, itemStack);
    }

    @Unique
    public void civilized_weapons$onSwapDualHands(Hand hand) {
        if (!this.hasSwappedThisTick) {
            this.civilized_weapons$setLastAttackHand(hand);
            this.preferredHand = hand;
            this.hasSwappedThisTick = true;
        }
    }

    @Unique
    public Hand civilized_weapons$getLastAttackHand() {
        return this.lastAttackHand;
    }

    @Unique
    public void civilized_weapons$setLastAttackHand(Hand hand) {
        if (!this.getWorld().isClient()) {
            this.lastAttackHand = hand;
            this.activeItemStack = this.getStackInHand(hand);
            var player = ((PlayerEntity) ((Object) this));
            if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                S2CHandSyncPacket.send(serverPlayerEntity, this.getId(), this.lastAttackHand == Hand.MAIN_HAND);
            }
        }
    }

    @Unique
    public Hand civilized_weapons$getLastAttackHandInverse() {
        return this.lastAttackHand;
    }

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
