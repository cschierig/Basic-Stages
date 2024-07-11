package com.carlschierig.privileged.api.registry;

import com.carlschierig.privileged.api.privilege.PrivilegeType;
import com.carlschierig.privileged.api.privilege.provider.ProviderType;
import com.carlschierig.privileged.impl.registry.PrivilegedRegistriesImpl;
import com.carlschierig.privileged.impl.registry.PrivilegedRegistryKeys;
import net.minecraft.core.Registry;

public class PrivilegedRegistries {
    public static final Registry<ProviderType<?>> PROVIDER_TYPE = PrivilegedRegistriesImpl.INSTANCE.createRegistry(PrivilegedRegistryKeys.PROVIDER_TYPE);
    public static final Registry<PrivilegeType<?, ?, ?>> PRIVILEGE_TYPE = PrivilegedRegistriesImpl.INSTANCE.createRegistry(PrivilegedRegistryKeys.PRIVILEGE_TYPE);
}
