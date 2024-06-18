package com.carlschierig.basicstages.mixin.client.block;

import com.carlschierig.basicstages.BasicStagesClient;
import com.carlschierig.basicstages.impl.util.BSUtil;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockModelShaper.class)
public abstract class BlockModelShaperMixin {

    @ModifyVariable(method = "getBlockModel", at = @At("HEAD"), argsOnly = true)
    private BlockState getBlockState(BlockState state) {
        return BSUtil.replace(state, BasicStagesClient.getPlayer());
    }
}
