package com.carlschierig.privileged.api.privilege.provider;

import com.carlschierig.privileged.impl.registry.PrivilegedRegistryKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ProviderType<T extends PrivilegeProvider>(PrivilegeProvider.Serializer<T> serializer) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ProviderType<?>> STREAM_CODEC = ByteBufCodecs.registry(PrivilegedRegistryKeys.PROVIDER_TYPE);
}
