package com.carlschierig.basicstages.api.registry;

import com.carlschierig.basicstages.api.privilege.PrivilegeType;
import com.carlschierig.basicstages.impl.registry.BSRegistryKeys;
import com.carlschierig.basicstages.impl.registry.RegistryBuilder;
import net.minecraft.core.Registry;

public class BSRegistries {
    
    public static final Registry<PrivilegeType<?, ?>> PRIVILEGE_TYPE = RegistryBuilder.create(BSRegistryKeys.PRIVILEGE_TYPE);
}
