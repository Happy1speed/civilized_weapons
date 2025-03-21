package net.happyspeed.civilized_weapons.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.happyspeed.civilized_weapons.access.ClientPlayerClassAccess;
import net.happyspeed.civilized_weapons.access.PlayerClassAccess;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class PlayerHandSwapPacket {

    public static final Identifier DUAL_SWAP_PACKET = new Identifier("civilized_weapons", "swap_dual_hands");

    public static Packet<?> swapPacket(Entity entity, boolean mainHand) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(entity.getId());
        buf.writeBoolean(mainHand);

        return ClientPlayNetworking.createC2SPacket(DUAL_SWAP_PACKET, buf);
    }

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(DUAL_SWAP_PACKET, (server, player, handler, buffer, sender) -> {
            int entityId = buffer.readInt();
            boolean mainHand = buffer.readBoolean();
            server.execute(() -> {
                player.updateLastActionTime();
                if (player.getWorld().getEntityById(entityId) != null) {
                    if (!player.getWorld().isClient()) {
                        if (mainHand) {
                            ((PlayerClassAccess) player).civilized_weapons$onSwapDualHands(Hand.MAIN_HAND);
                        } else {
                            ((PlayerClassAccess) player).civilized_weapons$onSwapDualHands(Hand.OFF_HAND);
                        }
                    }
                }

            });

        });

    }

}
