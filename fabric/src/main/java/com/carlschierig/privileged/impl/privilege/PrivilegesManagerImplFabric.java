package com.carlschierig.privileged.impl.privilege;

import com.carlschierig.privileged.impl.util.Util;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

public class PrivilegesManagerImplFabric extends PrivilegesManagerImpl implements IdentifiableResourceReloadListener {
    public PrivilegesManagerImplFabric(Supplier<HolderLookup.Provider> provider) {
        super(provider);
    }

    @Override
    public ResourceLocation getFabricId() {
        return Util.id("privileges_reloader");
    }

    @Override
    public Collection<ResourceLocation> getFabricDependencies() {
        return Set.of(ResourceReloadListenerKeys.TAGS);
    }
}
