package com.carlschierig.privileged.impl.network.payloads;

import com.carlschierig.privileged.api.privilege.provider.PrivilegeProvider;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record ProviderPayload(List<PrivilegeProvider> providers) implements CustomPacketPayload {
    public static final Type<ProviderPayload> TYPE = Util.getType("providers");

    public static final StreamCodec<RegistryFriendlyByteBuf, ProviderPayload> STREAM_CODEC = StreamCodec.composite(
            PrivilegeProvider.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ProviderPayload::providers,
            ProviderPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
