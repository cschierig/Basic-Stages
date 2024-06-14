package com.carlschierig.basicstages.impl.privilege;

import com.carlschierig.basicstages.impl.BSUtil;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class PrivilegesManagerImplFabric extends PrivilegesManagerImpl implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return BSUtil.id("privileges_reloader");
    }
}
