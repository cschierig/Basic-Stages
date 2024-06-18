package com.carlschierig.basicstages;

import com.carlschierig.basicstages.api.stage.event.BSEvents;
import com.carlschierig.basicstages.impl.mixin.ScheduleChunkRebuild;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.player.Player;

public class BasicStagesClient {
    private static Minecraft minecraft;
    private static boolean needsRebuild = false;

    public static Player getPlayer() {
        if (minecraft == null) {
            minecraft = Minecraft.getInstance();
        }
        return minecraft.player;
    }

    public static void init() {
        BSEvents.STAGE_GAINED.register((player, stage) -> {
            needsRebuild = true;
        });
        BSEvents.STAGE_REMOVED.register((player, stage) -> {
            needsRebuild = true;
        });
    }

    public static void rebuild(LevelRenderer level) {
        if (needsRebuild && level instanceof ScheduleChunkRebuild rebuild) {
            rebuild.basic_stages$rebuild();
        }
        needsRebuild = false;
    }

    public static boolean debugUngrabMouse() {
        Minecraft.getInstance().mouseHandler.releaseMouse();
        return true;
    }
}
