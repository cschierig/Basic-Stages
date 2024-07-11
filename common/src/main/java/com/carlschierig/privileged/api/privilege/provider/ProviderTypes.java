package com.carlschierig.privileged.api.privilege.provider;

import com.carlschierig.privileged.api.registry.PrivilegedRegistries;
import com.carlschierig.privileged.impl.registry.PrivilegedRegistriesImpl;
import com.carlschierig.privileged.impl.util.Util;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public final class ProviderTypes {
    public static final ProviderType<BlockProvider> BLOCK = register(
            Util.id("block"),
            BlockProvider.CODEC,
            BlockProvider.STREAM_CODEC
    );

    public static final ProviderType<BlockStateProvider> BLOCK_STATE = register(
            Util.id("block_state"),
            BlockStateProvider.CODEC,
            BlockStateProvider.STREAM_CODEC
    );

    public static final ProviderType<ItemProvider> ITEM = register(
            Util.id("item"),
            ItemProvider.CODEC,
            ItemProvider.STREAM_CODEC
    );

    public static <T extends PrivilegeProvider> ProviderType<T> register(ResourceLocation id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return PrivilegedRegistriesImpl.register(PrivilegedRegistries.PROVIDER_TYPE, id, new ProviderType<>(new PrivilegeProvider.Serializer<>(codec, streamCodec)));
    }

    public static void init() {
    }
}
