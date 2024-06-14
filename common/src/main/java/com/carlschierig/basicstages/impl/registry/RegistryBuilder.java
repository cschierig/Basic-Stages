package com.carlschierig.basicstages.impl.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public abstract class RegistryBuilder {
    public static RegistryBuilder INSTANCE;

    public RegistryBuilder() {
        INSTANCE = this;
    }

    public abstract <T> Registry<T> create(ResourceKey<Registry<T>> registryKey);
}
