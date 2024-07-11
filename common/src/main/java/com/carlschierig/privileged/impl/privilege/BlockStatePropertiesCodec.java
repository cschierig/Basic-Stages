package com.carlschierig.privileged.impl.privilege;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A codec which retrieves the overridden block state properties from a serialized block state.
 */
public class BlockStatePropertiesCodec implements Codec<Set<String>> {
    @Override
    public <T> DataResult<Pair<Set<String>, T>> decode(DynamicOps<T> ops, T input) {
        var properties = ops.get(input, "Properties");
        if (properties.isError()) {
            return DataResult.success(Pair.of(Set.of(), input));
        }

        return properties.flatMap(ops::getMap)
                .map(map -> map.entries().map(pair -> ops.getStringValue(pair.getFirst()).getOrThrow()).collect(Collectors.toSet()))
                .map(set -> Pair.of(set, input));
    }

    @Override
    public <T> DataResult<T> encode(Set<String> input, DynamicOps<T> ops, T prefix) {
        return DataResult.success(ops.empty());
    }
}
