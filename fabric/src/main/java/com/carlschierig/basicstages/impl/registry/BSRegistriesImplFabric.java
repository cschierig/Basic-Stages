package com.carlschierig.basicstages.impl.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class BSRegistriesImplFabric extends BSRegistriesImpl {
    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }

    @Override
    public <T> T registerImpl(Registry<? super T> registry, ResourceLocation id, T item) {
        return Registry.register(registry, id, item);
    }
}
