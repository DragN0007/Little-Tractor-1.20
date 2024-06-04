package com.dragn0007.littletractor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class Network {

    public static class ToggleTillerPowerRequest {
        private final int id;

        public ToggleTillerPowerRequest(int id) {
            this.id = id;
        }

        public static void encode(ToggleTillerPowerRequest msg, FriendlyByteBuf buffer) {
            buffer.writeInt(msg.id);
        }

        public static ToggleTillerPowerRequest decode(FriendlyByteBuf buffer) {
            return new ToggleTillerPowerRequest(buffer.readInt());
        }

        public static void handle(ToggleTillerPowerRequest msg, Supplier<NetworkEvent.Context> context) {
            ServerPlayer player = context.get().getSender();
            if(player != null) {
                if(player.level().getEntity(msg.id) instanceof Tractor tractor) {
                    tractor.toggleTillerPower();
                }
            }
        }
    }

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(LittleTractorMain.MODID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, ToggleTillerPowerRequest.class, ToggleTillerPowerRequest::encode, ToggleTillerPowerRequest::decode, ToggleTillerPowerRequest::handle);
    }

}
