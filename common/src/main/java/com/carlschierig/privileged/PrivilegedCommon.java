package com.carlschierig.privileged;

import com.carlschierig.privileged.api.advancement.criterion.PrivilegedCriteriaTriggers;
import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.stage.event.PrivilegedEvents;
import com.carlschierig.privileged.impl.network.S2CPackets;
import net.minecraft.server.level.ServerPlayer;

public class PrivilegedCommon {
    public static void init() {
        PrivilegeTypes.init();
        PrivilegedCriteriaTriggers.init();

        PrivilegedEvents.STAGE_GAINED.register((player, stage) -> {
                    if (player instanceof ServerPlayer serverPlayer) {
                        S2CPackets.INSTANCE.sendStageUpdate(serverPlayer, stage, true);
                    }
                }
        );
        PrivilegedEvents.STAGE_REMOVED.register((player, stage) -> {
                    if (player instanceof ServerPlayer serverPlayer) {
                        S2CPackets.INSTANCE.sendStageUpdate(serverPlayer, stage, false);
                    }
                }
        );

    }
}
