package com.carlschierig.privileged.impl.network.payloads;

import com.carlschierig.privileged.impl.util.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record StageUpdatePayload(String stage, boolean added) implements CustomPacketPayload {
    public static final Type<StageUpdatePayload> TYPE = Util.getType("update_stage");

    public static final StreamCodec<ByteBuf, StageUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            StageUpdatePayload::stage,
            ByteBufCodecs.BOOL,
            StageUpdatePayload::added,
            StageUpdatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
