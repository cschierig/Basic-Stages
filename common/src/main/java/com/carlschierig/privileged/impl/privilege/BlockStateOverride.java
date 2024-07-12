package com.carlschierig.privileged.impl.privilege;

import com.carlschierig.privileged.impl.util.Util;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public record BlockStateOverride(BlockState base, Set<String> overriddenProperties) {
    public static Codec<BlockStateOverride> CODEC = Codec.of(
            new Encoder<>() {
                @Override
                public <T> DataResult<T> encode(BlockStateOverride input, DynamicOps<T> ops, T prefix) {
                    return BlockState.CODEC.encode(input.base, ops, prefix);
                }
            },
            new Decoder<>() {
                @Override
                public <T> DataResult<Pair<BlockStateOverride, T>> decode(DynamicOps<T> ops, T input) {
                    var state = BlockState.CODEC.decode(ops, input);
                    var props = new BlockStatePropertiesCodec().decode(ops, input);
                    if (state.isError() || props.isError()) {
                        return DataResult.error(() -> "Could decode block state override. Block State: " + state.error().orElseThrow().message() + "Properties: " + props.error().orElseThrow().message());
                    }
                    return new DataResult.Success<>(Pair.of(new BlockStateOverride(state.getOrThrow().getFirst(), props.getOrThrow().getFirst()), ops.empty()), Lifecycle.stable());
                }
            }
    );


    public static StreamCodec<RegistryFriendlyByteBuf, BlockStateOverride> STREAM_CODEC = StreamCodec.composite(
            Util.BLOCK_STATE_STREAM_CODEC,
            BlockStateOverride::base,
            ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.STRING_UTF8),
            BlockStateOverride::overriddenProperties,
            BlockStateOverride::new
    );

    public static BlockStateOverride of(BlockState state) {
        return new BlockStateOverride(state, Set.of());
    }

    ;

    public boolean matches(BlockState other) {
        if (!other.is(base.getBlock())) {
            return false;
        }

        for (var property : base.getProperties()) {
            var otherValue = other.getOptionalValue(property);
            if (otherValue.isEmpty()
                    || (overriddenProperties.contains(property.getName()) && !base.getValue(property).equals(otherValue.get()))) {
                return false;
            }
        }
        return true;
    }
}
