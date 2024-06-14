package com.carlschierig.basicstages.impl.network;

import com.carlschierig.basicstages.api.stage.StageMap;
import com.carlschierig.basicstages.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.basicstages.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.basicstages.impl.network.payloads.PrivilegePayload;
import com.carlschierig.basicstages.impl.privilege.PrivilegesManagerImpl;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientPacketReceiver {
    public static void receiveStages(PlayerStagesPayload payload, ClientPlayNetworking.Context context) {
        StageMap.setStages(context.player().getUUID(), payload.stages());
    }

    public static void receivePrivileges(PrivilegePayload payload, ClientPlayNetworking.Context context) {
        PrivilegesManagerImpl.INSTANCE.addPrivileges(payload.privileges());
    }

    public static void receiveClear(ClearPrivilegesPayload payload, ClientPlayNetworking.Context context) {
        PrivilegesManagerImpl.INSTANCE.clear();
    }
}
