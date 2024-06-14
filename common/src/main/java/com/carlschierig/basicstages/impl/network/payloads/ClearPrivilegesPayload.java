package com.carlschierig.basicstages.impl.network.payloads;

import com.carlschierig.basicstages.impl.BSUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClearPrivilegesPayload() implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, ClearPrivilegesPayload> STREAM_CODEC = StreamCodec.unit(new ClearPrivilegesPayload());

    public static final Type<ClearPrivilegesPayload> TYPE = BSUtil.getType("clear_privileges");

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
