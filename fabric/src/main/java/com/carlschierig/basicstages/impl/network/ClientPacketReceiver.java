package com.carlschierig.basicstages.impl.network;

import com.carlschierig.basicstages.api.privilege.PrivilegesManager;
import com.carlschierig.basicstages.api.stage.StageMap;
import com.carlschierig.basicstages.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.basicstages.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.basicstages.impl.network.payloads.PrivilegePayload;
import com.carlschierig.basicstages.impl.network.payloads.StageUpdatePayload;
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

    public static void receivePrivileges(PrivilegePayload payload, ClientPlayNetworking.Context context) {
        PrivilegesManager.addPrivileges(payload.privileges());
    }

    public static void receiveClear(ClearPrivilegesPayload payload, ClientPlayNetworking.Context context) {
        PrivilegesManager.clear();
    }
}
