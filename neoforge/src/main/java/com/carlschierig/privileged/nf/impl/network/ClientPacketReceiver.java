package com.carlschierig.privileged.nf.impl.network;

import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import com.carlschierig.privileged.api.stage.StageMap;
import com.carlschierig.privileged.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.privileged.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.privileged.impl.network.payloads.ProviderPayload;
import com.carlschierig.privileged.impl.network.payloads.StageUpdatePayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketReceiver {
    public static void receiveStages(PlayerStagesPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(
                () -> StageMap.getInstance().setStages(ctx.player(), payload.stages())
        );
    }

    public static void receiveStageUpdate(StageUpdatePayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(
                () -> {
                    if (payload.added()) {
                        StageMap.getInstance().addStage(ctx.player(), payload.stage());
                    } else {
                        StageMap.getInstance().removeStage(ctx.player(), payload.stage());
                    }
                }
        );
    }

    public static void receiveProviders(ProviderPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(
                () -> PrivilegesManager.addProviders(payload.providers())
        );
    }

    public static void receiveClear(ClearPrivilegesPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(
                PrivilegesManager::clear
        );
    }
}
