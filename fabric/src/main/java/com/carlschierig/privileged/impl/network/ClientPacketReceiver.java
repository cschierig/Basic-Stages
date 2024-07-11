package com.carlschierig.privileged.impl.network;

import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import com.carlschierig.privileged.api.stage.StageMap;
import com.carlschierig.privileged.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.privileged.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.privileged.impl.network.payloads.ProviderPayload;
import com.carlschierig.privileged.impl.network.payloads.StageUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientPacketReceiver {
    public static void receiveStages(PlayerStagesPayload payload, ClientPlayNetworking.Context context) {
        StageMap.getInstance().setStages(context.player(), payload.stages());
    }

    public static void receiveStageUpdate(StageUpdatePayload payload, ClientPlayNetworking.Context context) {
        if (payload.added()) {
            StageMap.getInstance().addStage(context.player(), payload.stage());
        } else {
            StageMap.getInstance().removeStage(context.player(), payload.stage());
        }
    }

    public static void receiveProviders(ProviderPayload payload, ClientPlayNetworking.Context context) {
        PrivilegesManager.addProviders(payload.providers());
    }

    public static void receiveClear(ClearPrivilegesPayload payload, ClientPlayNetworking.Context context) {
        PrivilegesManager.clear();
    }
}
