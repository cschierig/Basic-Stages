package com.carlschierig.basicstages.api.stage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class StageMap {
    private static final Map<UUID, HashSet<String>> stagesByPlayer = new HashMap<>();

    /**
     * Returns true if the player with the given uuid has the given stage, false otherwise.
     *
     * @param player The player whose stages should be checked.
     * @param stage  The stage which should be checked.
     * @return true if the player has the stage, false otherwise.
     */
    public static boolean hasStage(UUID player, String stage) {
        return stagesByPlayer.containsKey(player) && stagesByPlayer.get(player).contains(stage);
    }

    public static void setStages(UUID player, HashSet<String> stages) {
        stagesByPlayer.put(player, stages);
    }

    public static HashSet<String> getStages(UUID player) {
        stagesByPlayer.putIfAbsent(player, new HashSet<>());
        return stagesByPlayer.get(player);
    }
}
