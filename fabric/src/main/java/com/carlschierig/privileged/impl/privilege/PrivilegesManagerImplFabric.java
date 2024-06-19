package com.carlschierig.privileged.impl.privilege;

import com.carlschierig.privileged.impl.util.Util;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class PrivilegesManagerImplFabric extends PrivilegesManagerImpl implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return Util.id("privileges_reloader");
    }
}
