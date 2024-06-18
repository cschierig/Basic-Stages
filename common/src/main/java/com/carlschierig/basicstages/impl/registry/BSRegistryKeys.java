package com.carlschierig.basicstages.impl.registry;

import com.carlschierig.basicstages.api.privilege.PrivilegeType;
import com.carlschierig.basicstages.impl.util.BSUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BSRegistryKeys {
    public static final ResourceKey<Registry<PrivilegeType<?, ?>>> PRIVILEGE_TYPE = createRegistryKey("privilege_type");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(BSUtil.id(path));
    }
}
