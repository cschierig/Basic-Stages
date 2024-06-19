package com.carlschierig.privileged.impl.network.payloads;

import com.carlschierig.privileged.impl.util.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashSet;

public record PlayerStagesPayload(HashSet<String> stages) implements CustomPacketPayload {
    public static final Type<PlayerStagesPayload> TYPE = Util.getType("player_stages");
    public static final StreamCodec<ByteBuf, PlayerStagesPayload> STREAM_CODEC = ByteBufCodecs.collection(
            HashSet::new,
            ByteBufCodecs.STRING_UTF8
    ).map(PlayerStagesPayload::new, PlayerStagesPayload::stages);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
