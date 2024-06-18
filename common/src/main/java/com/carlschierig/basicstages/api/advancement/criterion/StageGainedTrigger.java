package com.carlschierig.basicstages.api.advancement.criterion;

import com.carlschierig.basicstages.api.stage.StageMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class StageGainedTrigger extends SimpleCriterionTrigger<StageGainedTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, String stage) {
        trigger(player, instance -> instance.matches(player, stage));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  String stage) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        Codec.STRING.fieldOf("stage").forGetter(TriggerInstance::stage)
                ).apply(instance, TriggerInstance::new)
        );

        public boolean matches(ServerPlayer player, String stage) {
            return StageMap.getInstance().hasStage(player, stage);
        }
    }
}
