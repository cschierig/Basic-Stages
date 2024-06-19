package com.carlschierig.privileged.impl.network.payloads;


import com.carlschierig.privileged.api.privilege.Privilege;
import com.carlschierig.privileged.api.privilege.PrivilegeType;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record PrivilegePayload(PrivilegeType<?, ?> privilegeType,
                               List<Privilege<?, ?>> privileges) implements CustomPacketPayload {
    public static final Type<PrivilegePayload> TYPE = Util.getType("privileges");

    public static final StreamCodec<RegistryFriendlyByteBuf, PrivilegePayload> STREAM_CODEC = StreamCodec.composite(
            PrivilegeType.STREAM_CODEC,
            PrivilegePayload::privilegeType,
            Privilege.STREAM_CODEC.apply(ByteBufCodecs.list()),
            PrivilegePayload::privileges,
            PrivilegePayload::new
    );

    @Override
    public Type<PrivilegePayload> type() {
        return TYPE;
    }
}
