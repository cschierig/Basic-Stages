package com.carlschierig.basicstages.impl.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryBuilderFabric extends RegistryBuilder {
    @Override
    public <T> Registry<T> create(ResourceKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }
}
