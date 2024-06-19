package com.carlschierig.privileged.mixin.client.block;

import com.carlschierig.privileged.impl.mixin.ScheduleChunkRebuild;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements ScheduleChunkRebuild {

    @Shadow
    private @Nullable ClientLevel level;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void setSectionDirty(int sectionX, int sectionY, int sectionZ, boolean reRenderOnMainThread);

    @Override
    public void privileged$rebuild() {
        // from https://github.com/steves-underwater-paradise/simple-seasons/blob/812884e1ae81f3e2082c71a65b782d97a19a1562/src/main/java/io/github/steveplays28/simpleseasons/client/util/rendering/RenderingUtil.java
        var player = minecraft.player;
        if (player == null || level == null) {
            return;
        }
        var playerChunkPos = player.chunkPosition();
        var clampedViewDistance = Minecraft.getInstance().options.getEffectiveRenderDistance();
        for (int chunkX = playerChunkPos.x - clampedViewDistance; chunkX < playerChunkPos.x + clampedViewDistance; chunkX++) {
            for (int chunkZ = playerChunkPos.z - clampedViewDistance; chunkZ < playerChunkPos.z + clampedViewDistance; chunkZ++) {
                for (int chunkY = level.getMinBuildHeight() >> 4; chunkY < level.getMaxBuildHeight() >> 4; chunkY++) {
                    setSectionDirty(chunkX, chunkY, chunkZ, true);
                }
            }
        }
    }
}
