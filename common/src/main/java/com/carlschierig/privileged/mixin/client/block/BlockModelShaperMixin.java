package com.carlschierig.privileged.mixin.client.block;

import com.carlschierig.privileged.PrivilegedClient;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockModelShaper.class)
public abstract class BlockModelShaperMixin {

    @ModifyVariable(method = "getBlockModel", at = @At("HEAD"), argsOnly = true)
    private BlockState getBlockState(BlockState state) {
        return Util.replace(state, PrivilegedClient.getPlayer());
    }
}
