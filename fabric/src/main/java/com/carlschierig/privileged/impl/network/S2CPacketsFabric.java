package com.carlschierig.privileged.impl.network;

import com.carlschierig.privileged.PrivilegedFabric;
import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import com.carlschierig.privileged.api.stage.StageMap;
import com.carlschierig.privileged.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.privileged.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.privileged.impl.network.payloads.ProviderPayload;
import com.carlschierig.privileged.impl.network.payloads.StageUpdatePayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class S2CPacketsFabric extends S2CPackets {
    @Override
    public void sendStages(ServerPlayer player) {
        ServerPlayNetworking.send(player, new PlayerStagesPayload(StageMap.getInstance().getStages(player)));
    }

    @Override
    public void sendStageUpdate(ServerPlayer player, String stage, boolean added) {
        ServerPlayNetworking.send(player, new StageUpdatePayload(stage, added));
    }

    @Override
    public void sendProviders() {
        var server = PrivilegedFabric.server;
        if (server != null) {
            for (var player : PlayerLookup.all(server)) {
                sendProviders(player);
            }
        }
    }

    @Override
    public void sendProviders(ServerPlayer player) {
        ServerPlayNetworking.send(player, new ProviderPayload(PrivilegesManager.getProviders()));
    }

    @Override
    public void clearPrivileges() {
        var server = PrivilegedFabric.server;
        if (server != null) {
            for (var player : PlayerLookup.all(server)) {
                clearPrivileges(player);
            }
        }
    }

    @Override
    public void clearPrivileges(ServerPlayer player) {
        ServerPlayNetworking.send(player, new ClearPrivilegesPayload());
    }
}
