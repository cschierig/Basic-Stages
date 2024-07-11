package com.carlschierig.privileged.impl.registry;

import com.carlschierig.privileged.api.privilege.PrivilegeType;
import com.carlschierig.privileged.api.privilege.provider.ProviderType;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class PrivilegedRegistryKeys {
    public static final ResourceKey<Registry<ProviderType<?>>> PROVIDER_TYPE = createRegistryKey("provider_type");
    public static final ResourceKey<Registry<PrivilegeType<?, ?, ?>>> PRIVILEGE_TYPE = createRegistryKey("privilege_type");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(Util.id(path));
    }
}
