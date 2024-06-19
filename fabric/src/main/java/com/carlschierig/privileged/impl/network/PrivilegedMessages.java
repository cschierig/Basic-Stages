package com.carlschierig.privileged.impl.network;

import com.carlschierig.privileged.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.privileged.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.privileged.impl.network.payloads.PrivilegePayload;
import com.carlschierig.privileged.impl.network.payloads.StageUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PrivilegedMessages {
    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerStagesPayload.TYPE, ClientPacketReceiver::receiveStages);
        ClientPlayNetworking.registerGlobalReceiver(PrivilegePayload.TYPE, ClientPacketReceiver::receivePrivileges);
        ClientPlayNetworking.registerGlobalReceiver(ClearPrivilegesPayload.TYPE, ClientPacketReceiver::receiveClear);
        ClientPlayNetworking.registerGlobalReceiver(StageUpdatePayload.TYPE, ClientPacketReceiver::receiveStageUpdate);
    }

    public static void registerPayloadsS2C() {
        PayloadTypeRegistry.playS2C().register(PlayerStagesPayload.TYPE, PlayerStagesPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(PrivilegePayload.TYPE, PrivilegePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClearPrivilegesPayload.TYPE, ClearPrivilegesPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(StageUpdatePayload.TYPE, StageUpdatePayload.STREAM_CODEC);
    }
}
