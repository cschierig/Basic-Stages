package com.carlschierig.basicstages.api.privilege;

import com.carlschierig.basicstages.api.registry.BSRegistries;
import com.carlschierig.basicstages.impl.registry.BSRegistryKeys;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PrivilegeType<K, V>(Privilege.Serializer<K, V> serializer) {
    public static final Codec<PrivilegeType<?, ?>> CODEC = BSRegistries.PRIVILEGE_TYPE.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, PrivilegeType<?, ?>> STREAM_CODEC = ByteBufCodecs.registry(BSRegistryKeys.PRIVILEGE_TYPE);
}
