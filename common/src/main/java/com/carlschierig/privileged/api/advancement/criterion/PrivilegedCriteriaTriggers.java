package com.carlschierig.privileged.api.advancement.criterion;

import com.carlschierig.privileged.impl.registry.PrivilegedRegistriesImpl;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;

public class PrivilegedCriteriaTriggers {
    public static final StageGainedTrigger GAINED_TRIGGER = register("stage_gained", new StageGainedTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return PrivilegedRegistriesImpl.register(BuiltInRegistries.TRIGGER_TYPES, Util.id(name), trigger);
    }

    public static void init() {

    }
}
