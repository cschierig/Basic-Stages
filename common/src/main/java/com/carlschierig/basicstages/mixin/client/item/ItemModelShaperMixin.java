package com.carlschierig.basicstages.mixin.client.item;

import com.carlschierig.basicstages.BasicStagesClient;
import com.carlschierig.basicstages.impl.util.BSUtil;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelShaper.class)
public class ItemModelShaperMixin {
    @ModifyVariable(method = "getItemModel(Lnet/minecraft/world/item/Item;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At("HEAD"), argsOnly = true)
    private Item getBlockState(Item item) {
        return BSUtil.replace(item, BasicStagesClient.getPlayer());
    }
}
