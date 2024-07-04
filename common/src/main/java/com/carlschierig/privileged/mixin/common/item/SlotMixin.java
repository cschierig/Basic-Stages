package com.carlschierig.privileged.mixin.common.item;

import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {

    @Shadow
    @Final
    public Container container;

    @Shadow
    @Final
    private int slot;

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void mayPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!PrivilegesManager.canAccess(player, PrivilegeTypes.ITEM, container.getItem(slot).getItem())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
