package com.carlschierig.privileged.mixin.client.item;

import com.carlschierig.privileged.PrivilegedClient;
import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void replaceName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        if (!PrivilegesManager.canAccess(PrivilegedClient.getPlayer(), PrivilegeTypes.ITEM, stack.getItem())) {
            var value = cir.getReturnValue();
            cir.setReturnValue(MutableComponent.create(value.getContents()).withStyle(Style.EMPTY.withObfuscated(true)));
            cir.cancel();
        }
    }
}
