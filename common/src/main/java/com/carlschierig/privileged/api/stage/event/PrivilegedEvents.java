package com.carlschierig.privileged.api.stage.event;

import net.minecraft.world.entity.player.Player;

public class PrivilegedEvents {

    public static final Event<StageGained> STAGE_GAINED = new Event<>(
            callbacks -> (player, stage) -> {
                for (var callback : callbacks) {
                    callback.stageGained(player, stage);
                }
            }
    );

    @FunctionalInterface
    public interface StageGained {
        void stageGained(Player player, String stage);
    }

    public static final Event<StageRemoved> STAGE_REMOVED = new Event<>(
            callbacks -> (player, stage) -> {
                for (var callback : callbacks) {
                    callback.stageRemoved(player, stage);
                }
            }
    );

    @FunctionalInterface
    public interface StageRemoved {
        void stageRemoved(Player player, String stage);
    }
}
