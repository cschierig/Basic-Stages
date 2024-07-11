package com.carlschierig.privileged.mixin.common;

import com.carlschierig.privileged.PrivilegedCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.ServerAdvancementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void getRegistryAccess(HolderLookup.Provider registries, CallbackInfo ci) {
        PrivilegedCommon.registryLookup = registries;
    }

}
