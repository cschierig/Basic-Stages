package com.carlschierig.basicstages;

import com.carlschierig.basicstages.api.privilege.PrivilegeTypes;

public class BasicStagesCommon {
    public static void init() {
        // Initialize registries
        PrivilegeTypes.init();
    }
}
