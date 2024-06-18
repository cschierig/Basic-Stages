package com.carlschierig.basicstages.api.stage;

import com.carlschierig.basicstages.api.advancement.criterion.BSCriteriaTriggers;
import com.carlschierig.basicstages.api.stage.event.BSEvents;
import com.carlschierig.basicstages.impl.state.BSState;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class StageMap {
    public static final Codec<StageMap> CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.STRING.listOf().xmap(HashSet::new, ArrayList::new)).xmap(
            map -> new StageMap(new HashMap<>(map)),
            map -> map.stagesByPlayer
    );

    private final HashMap<UUID, HashSet<String>> stagesByPlayer;

    public StageMap() {
        this(new HashMap<>());
    }

    private StageMap(HashMap<UUID, HashSet<String>> map) {
        this.stagesByPlayer = map;
    }

    public static StageMap getInstance() {
        return BSState.getInstance().getMap();
    }

    /**
     * Returns true if the player with the given uuid has the given stage, false otherwise.
     *
     * @param player The player whose stages should be checked.
     * @param stage  The stage which should be checked.
     * @return true if the player has the stage, false otherwise.
     */
    public boolean hasStage(Player player, String stage) {
        if (player == null) {
            throw new IllegalArgumentException("Player may not be null");
        }
        var uuid = player.getUUID();
        return stagesByPlayer.containsKey(uuid) && stagesByPlayer.get(uuid).contains(stage);
    }

    public void setStages(Player player, HashSet<String> stages) {
        if (player == null) {
            throw new IllegalArgumentException("Player may not be null");
        }
        stagesByPlayer.put(player.getUUID(), stages);
    }

    public void clearStages(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player may not be null");
        }
        stagesByPlayer.get(player.getUUID()).clear();
    }

    public boolean addStage(Player player, String stage) {
        if (player == null) {
            return false;
        }
        var uuid = player.getUUID();
        stagesByPlayer.putIfAbsent(uuid, new HashSet<>());
        if (stagesByPlayer.get(uuid).add(stage)) {
            BSEvents.STAGE_GAINED.invoker().stageGained(player, stage);
            if (player instanceof ServerPlayer serverPlayer) {
                BSCriteriaTriggers.GAINED_TRIGGER.trigger(serverPlayer, stage);
            }
            return true;
        }
        return false;
    }

    public boolean removeStage(Player player, String stage) {
        if (player == null) {
            return false;
        }
        var uuid = player.getUUID();
        stagesByPlayer.putIfAbsent(uuid, new HashSet<>());
        if (stagesByPlayer.get(uuid).remove(stage)) {
            BSEvents.STAGE_REMOVED.invoker().stageRemoved(player, stage);
            return true;
        }
        return false;
    }

    public HashSet<String> getStages(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player may not be null");
        }
        var uuid = player.getUUID();
        return stagesByPlayer.computeIfAbsent(uuid, (uuid1) -> new HashSet<>());
    }
}
