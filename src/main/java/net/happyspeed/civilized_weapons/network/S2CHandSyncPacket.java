package net.happyspeed.civilized_weapons.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.happyspeed.civilized_weapons.CivilizedWeaponsMod;
import net.happyspeed.civilized_weapons.access.ClientPlayerClassAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class S2CHandSyncPacket {
	public static final Identifier ID = CivilizedWeaponsMod.id("sc_hand_sync");

	public static void send(ServerPlayerEntity player, int id, boolean mainhand) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(id);
		buf.writeBoolean(mainhand);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			int id = buf.readInt();
			boolean mainhand = buf.readBoolean();
			client.execute(() -> {
				Entity entity = handler.getWorld().getEntityById(id);
				if (entity != null) {
					if (entity instanceof PlayerEntity player) {
						if (mainhand) {
							((ClientPlayerClassAccess) player).civilized_weapons$setLastAttackHand(Hand.MAIN_HAND);
							((ClientPlayerClassAccess) player).civilized_weapons$setPreferHand(Hand.MAIN_HAND);
						}
						else {
							((ClientPlayerClassAccess) player).civilized_weapons$setLastAttackHand(Hand.OFF_HAND);
							((ClientPlayerClassAccess) player).civilized_weapons$setPreferHand(Hand.OFF_HAND);
						}
					}
				}
			});
		}
	}
}