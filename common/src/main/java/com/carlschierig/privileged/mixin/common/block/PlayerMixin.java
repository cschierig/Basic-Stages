package com.carlschierig.privileged.mixin.common.block;

import com.carlschierig.privileged.impl.util.Util;
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
        return Util.replace(state, (Player) (Object) this);
    }
}
