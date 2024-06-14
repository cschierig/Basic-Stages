package com.carlschierig.basicstages.mixin.client;

import com.carlschierig.basicstages.BasicStagesClient;
import com.carlschierig.basicstages.api.privilege.PrivilegeTypes;
import com.carlschierig.basicstages.api.privilege.PrivilegesManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderChunk.class)
public class RenderChunkMixin {
    @Redirect(method = "getBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/PalettedContainer;get(III)Ljava/lang/Object;"))
    private Object replaceSections(PalettedContainer<BlockState> container, int x, int y, int z) {
        var state = container.get(x, y, z);

        if (!PrivilegesManager.canAccess(BasicStagesClient.playerId, PrivilegeTypes.BLOCK, state.getBlock())) {
            var privilege = PrivilegesManager.getPrivilege(PrivilegeTypes.BLOCK, state.getBlock());

            return privilege.replacement().defaultBlockState();
        }
        return state;
    }
}
