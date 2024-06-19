package com.carlschierig.privileged;

import com.carlschierig.privileged.api.stage.event.PrivilegedEvents;
import com.carlschierig.privileged.impl.mixin.ScheduleChunkRebuild;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.player.Player;

public class PrivilegedClient {
    private static Minecraft minecraft;
    private static boolean needsRebuild = false;

    public static Player getPlayer() {
        if (minecraft == null) {
            minecraft = Minecraft.getInstance();
        }
        return minecraft.player;
    }

    public static void init() {
        PrivilegedEvents.STAGE_GAINED.register((player, stage) -> {
            needsRebuild = true;
        });
        PrivilegedEvents.STAGE_REMOVED.register((player, stage) -> {
            needsRebuild = true;
        });
    }

    public static void rebuild(LevelRenderer level) {
        if (needsRebuild && level instanceof ScheduleChunkRebuild rebuild) {
            rebuild.privileged$rebuild();
        }
        needsRebuild = false;
    }

    public static boolean debugUngrabMouse() {
        Minecraft.getInstance().mouseHandler.releaseMouse();
        return true;
    }
}
