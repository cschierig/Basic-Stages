package com.carlschierig.basicstages.impl.network;

import com.carlschierig.basicstages.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.basicstages.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.basicstages.impl.network.payloads.PrivilegePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class BSMessages {
    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerStagesPayload.TYPE, ClientPacketReceiver::receiveStages);
        ClientPlayNetworking.registerGlobalReceiver(PrivilegePayload.TYPE, ClientPacketReceiver::receivePrivileges);
        ClientPlayNetworking.registerGlobalReceiver(ClearPrivilegesPayload.TYPE, ClientPacketReceiver::receiveClear);
    }

    public static void registerPayloadsS2C() {
        PayloadTypeRegistry.playS2C().register(PlayerStagesPayload.TYPE, PlayerStagesPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(PrivilegePayload.TYPE, PrivilegePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClearPrivilegesPayload.TYPE, ClearPrivilegesPayload.STREAM_CODEC);
    }
}
