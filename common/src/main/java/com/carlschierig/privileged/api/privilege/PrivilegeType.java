package com.carlschierig.privileged.api.privilege;

import com.carlschierig.privileged.api.registry.PrivilegedRegistries;
import com.carlschierig.privileged.impl.registry.PrivilegedRegistryKeys;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PrivilegeType<K, V>(Privilege.Serializer<K, V> serializer) {
    public static final Codec<PrivilegeType<?, ?>> CODEC = PrivilegedRegistries.PRIVILEGE_TYPE.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, PrivilegeType<?, ?>> STREAM_CODEC = ByteBufCodecs.registry(PrivilegedRegistryKeys.PRIVILEGE_TYPE);
}
