package com.carlschierig.privileged.impl.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public abstract class PrivilegedRegistriesImpl {
    public static PrivilegedRegistriesImpl INSTANCE;

    public PrivilegedRegistriesImpl() {
        INSTANCE = this;
    }

    public abstract <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey);

    public abstract <T> T registerImpl(Registry<? super T> registry, ResourceLocation id, T item);

    public static <T> T register(Registry<? super T> registry, ResourceLocation id, T item) {
        return INSTANCE.registerImpl(registry, id, item);
    }
}
