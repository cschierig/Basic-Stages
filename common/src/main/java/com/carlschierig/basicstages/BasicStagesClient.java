package com.carlschierig.basicstages;

import net.minecraft.client.Minecraft;

import java.util.UUID;

public class BasicStagesClient {
    public static UUID playerId;

    public static UUID getPlayerId() {
        if (playerId == null) {
            playerId = Minecraft.getInstance().player.getUUID();
        }
        return playerId;
    }

    public static void disconnect() {

    }
}
