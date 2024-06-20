package com.carlschierig.privileged.nf.impl.network;

import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import com.carlschierig.privileged.api.stage.StageMap;
import com.carlschierig.privileged.impl.network.S2CPackets;
import com.carlschierig.privileged.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.privileged.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.privileged.impl.network.payloads.PrivilegePayload;
import com.carlschierig.privileged.impl.network.payloads.StageUpdatePayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;

public class S2CPacketsNeoforge extends S2CPackets {
    @Override
    public void sendStages(ServerPlayer player) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToPlayer(player, new PlayerStagesPayload(StageMap.getInstance().getStages(player)));
        }
    }

    @Override
    public void sendStageUpdate(ServerPlayer player, String stage, boolean added) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToPlayer(player, new StageUpdatePayload(stage, added));
        }
    }

    @Override
    public void sendPrivileges() {
        for (var type : PrivilegesManager.getTypes()) {
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                PacketDistributor.sendToAllPlayers(new PrivilegePayload(type, new ArrayList<>(PrivilegesManager.getPrivileges(type))));
            }
        }
    }

    @Override
    public void clearPrivileges() {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToAllPlayers(new ClearPrivilegesPayload());
        }
    }

    @Override
    public void sendPrivileges(ServerPlayer player) {
        for (var type : PrivilegesManager.getTypes()) {
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                PacketDistributor.sendToPlayer(player, new PrivilegePayload(type, new ArrayList<>(PrivilegesManager.getPrivileges(type))));
            }
        }
    }

    @Override
    public void clearPrivileges(ServerPlayer player) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToPlayer(player, new ClearPrivilegesPayload());
        }
    }
}
