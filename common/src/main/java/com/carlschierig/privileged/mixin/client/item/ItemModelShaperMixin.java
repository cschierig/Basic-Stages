package com.carlschierig.privileged.mixin.client.item;

import com.carlschierig.privileged.PrivilegedClient;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelShaper.class)
public class ItemModelShaperMixin {
    @ModifyVariable(method = "getItemModel(Lnet/minecraft/world/item/Item;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At("HEAD"), argsOnly = true)
    private Item getBlockState(Item item) {
        return Util.replace(item, PrivilegedClient.getPlayer());
    }
}
