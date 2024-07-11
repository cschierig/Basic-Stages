package com.carlschierig.privileged.mixin.common.item;

import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeCraftingHolder.class)
public interface RecipeCraftingHolderMixin {

    @Inject(method = "setRecipeUsed(Lnet/minecraft/world/level/Level;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/crafting/RecipeHolder;)Z", at = @At("HEAD"), cancellable = true)
    private void setRecipeUsed(Level level, ServerPlayer players, RecipeHolder<?> recipe, CallbackInfoReturnable<Boolean> cir) {
        var recipeValue = recipe.value();
        if (!PrivilegesManager.canAccess(players, PrivilegeTypes.ITEM, recipeValue.getResultItem(level.registryAccess()).getItem())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
        if (recipeValue.getIngredients().stream()
                .filter(ingredient -> ingredient.getItems().length == 1)
                .map(ingredient -> ingredient.getItems()[0])
                .anyMatch(stack -> !PrivilegesManager.canAccess(players, PrivilegeTypes.ITEM, stack.getItem()))
        ) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
