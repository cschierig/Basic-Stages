package com.carlschierig.basicstages.mixin.common.block;

import com.carlschierig.basicstages.impl.util.BSUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {


    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "hasCorrectToolForDrops", at = @At("HEAD"), argsOnly = true)
    private BlockState hasCorrectTool(BlockState state) {
        return BSUtil.replace(state, (Player) (Object) this);
    }
}
