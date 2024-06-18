package com.carlschierig.basicstages;

import com.carlschierig.basicstages.api.advancement.criterion.BSCriteriaTriggers;
import com.carlschierig.basicstages.api.privilege.PrivilegeTypes;
import com.carlschierig.basicstages.api.stage.event.BSEvents;
import com.carlschierig.basicstages.impl.network.S2CPackets;
import net.minecraft.server.level.ServerPlayer;

public class BasicStagesCommon {
    public static void init() {
        PrivilegeTypes.init();
        BSCriteriaTriggers.init();

        BSEvents.STAGE_GAINED.register((player, stage) -> {
                    if (player instanceof ServerPlayer serverPlayer) {
                        S2CPackets.INSTANCE.sendStageUpdate(serverPlayer, stage, true);
                    }
                }
        );
        BSEvents.STAGE_REMOVED.register((player, stage) -> {
                    if (player instanceof ServerPlayer serverPlayer) {
                        S2CPackets.INSTANCE.sendStageUpdate(serverPlayer, stage, false);
                    }
                }
        );

    }
}
