package net.happyspeed.civilized_weapons;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.happyspeed.civilized_weapons.client.EnchantmentDescriptionsEvent;
import net.happyspeed.civilized_weapons.network.S2CHandSyncPacket;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SweepAttackParticle;

public class CivilizedWeaponsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(CivilizedWeaponsMod.SWEEP_DOWN_PARTICLE, SweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CivilizedWeaponsMod.HUGE_SWING_PARTICLE, EndRodParticle.Factory::new);
        ClientPlayNetworking.registerGlobalReceiver(S2CHandSyncPacket.ID, new S2CHandSyncPacket.Receiver());
        initEvents();
    }
    private void initEvents() {
        ItemTooltipCallback.EVENT.register(new EnchantmentDescriptionsEvent());
    }
}
