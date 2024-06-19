package com.carlschierig.privileged.mixin.common.item;

import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract List<Component> getTooltipLines(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag);

    @Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true)
    private void onTick(Level level, Entity entity, int inventorySlot, boolean isCurrentItem, CallbackInfo ci) {
        if (level.isClientSide()) {
            return;
        }
        var stack = (ItemStack) (Object) (this);
        if (entity instanceof ServerPlayer player) {
            if (!stack.isEmpty() && !PrivilegesManager.canAccess(player, PrivilegeTypes.ITEM, stack.getItem())) {
                var replacement = PrivilegesManager.getPrivilege(PrivilegeTypes.ITEM, stack.getItem());
                player.sendSystemMessage(Component.literal("This ").append(replacement.replacement().getDefaultInstance().getDisplayName()).append(" feels strange, probably best to throw it away."));
                player.drop(stack.copy(), false, false);
                player.getInventory().removeItem(inventorySlot, stack.getCount());
                ci.cancel();
            }
        }
    }

    @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
    private void replaceTooltip(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        var stack = (ItemStack) (Object) (this);
        if (!PrivilegesManager.canAccess(player, PrivilegeTypes.ITEM, stack.getItem())) {
            var replacement = PrivilegesManager.getPrivilege(PrivilegeTypes.ITEM, stack.getItem());
            cir.setReturnValue(replacement.replacement().getDefaultInstance().getTooltipLines(tooltipContext, player, tooltipFlag));
            cir.cancel();
        }
    }
}
