package com.carlschierig.basicstages.api.registry;

import com.carlschierig.basicstages.api.privilege.PrivilegeType;
import com.carlschierig.basicstages.impl.registry.BSRegistriesImpl;
import com.carlschierig.basicstages.impl.registry.BSRegistryKeys;
import net.minecraft.core.Registry;

public class BSRegistries {
    public static final Registry<PrivilegeType<?, ?>> PRIVILEGE_TYPE = BSRegistriesImpl.INSTANCE.createRegistry(BSRegistryKeys.PRIVILEGE_TYPE);
}
