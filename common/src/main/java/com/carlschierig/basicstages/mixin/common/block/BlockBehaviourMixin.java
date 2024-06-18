package com.carlschierig.basicstages.mixin.common.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.carlschierig.basicstages.impl.util.BSUtil.replace;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @ModifyVariable(method = "getDestroyProgress", at = @At("HEAD"), argsOnly = true)
    private BlockState getDestroyProgress(BlockState state, BlockState s2, Player player) {
        return replace(state, player);
    }

    @ModifyVariable(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/storage/loot/LootParams$Builder;)Ljava/util/List;", at = @At("HEAD"), argsOnly = true)
    private BlockState getDrops(BlockState state, BlockState s2, LootParams.Builder params) {
        var entity = params.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof Player player) {
            return replace(state, player);
        }

        return state;
    }

    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/storage/loot/LootParams$Builder;)Ljava/util/List;", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour;getLootTable()Lnet/minecraft/resources/ResourceKey;", shift = At.Shift.AFTER))
    private void getDrops(BlockState state, LootParams.Builder params, CallbackInfoReturnable<List<ItemStack>> cir, @Local LocalRef<ResourceKey<LootTable>> localRef) {
        var entity = params.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof Player player) {
            localRef.set(replace(state, player).getBlock().getLootTable());
        }
    }
}
