package com.carlschierig.privileged.mixin.common.item;

import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    public abstract void setPickUpDelay(int pickupDelay);

    @Shadow
    public abstract boolean hasPickUpDelay();

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void cancelPickup(Player player, CallbackInfo ci) {
        var item = getItem().getItem();
        if (player instanceof ServerPlayer && !PrivilegesManager.canAccess(player, PrivilegeTypes.ITEM, item) && !hasPickUpDelay()) {
            var replacement = PrivilegesManager.getPrivilege(PrivilegeTypes.ITEM, item);
            setPickUpDelay(5 * 20);
            player.sendSystemMessage(Component.literal("This ").append(replacement.replacement().getDefaultInstance().getDisplayName()).append(" feels strange, I should leave it there."));
            ci.cancel();
        }
    }
}
