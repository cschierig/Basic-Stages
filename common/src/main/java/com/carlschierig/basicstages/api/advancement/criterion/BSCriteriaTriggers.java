package com.carlschierig.basicstages.api.advancement.criterion;

import com.carlschierig.basicstages.impl.registry.BSRegistriesImpl;
import com.carlschierig.basicstages.impl.util.BSUtil;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;

public class BSCriteriaTriggers {
    public static final StageGainedTrigger GAINED_TRIGGER = register("stage_gained", new StageGainedTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return BSRegistriesImpl.register(BuiltInRegistries.TRIGGER_TYPES, BSUtil.id(name), trigger);
    }

    public static void init() {

    }
}
