package com.carlschierig.basicstages.api.privilege;

import com.carlschierig.basicstages.impl.registry.BSRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

/**
 * @param privilege   The privilege which is granted when the player has reached the stage.
 * @param replacement The replacement which is used instead of the privilege as long as a player doesn't
 *                    have access to it.
 */
public record Privilege<K, V>(PrivilegeType<K, V> type, String stage, @NotNull K privilege, @NotNull V replacement) {
    public static final StreamCodec<RegistryFriendlyByteBuf, Privilege<?, ?>> STREAM_CODEC = ByteBufCodecs.registry(BSRegistryKeys.PRIVILEGE_TYPE).dispatch(
            Privilege::type, pt -> pt.serializer().STREAM_CODEC
    );

    public static final class Serializer<K, V> {
        public final MapCodec<Privilege<K, V>> CODEC;
        public final StreamCodec<RegistryFriendlyByteBuf, Privilege<K, V>> STREAM_CODEC;
        public final StreamCodec<RegistryFriendlyByteBuf, K> privilegeStreamCodec;
        public final StreamCodec<RegistryFriendlyByteBuf, V> replacementStreamCodec;

        public Serializer(
                Codec<K> privilegeCodec, Codec<V> valueCodec, StreamCodec<RegistryFriendlyByteBuf, K> privilegeStreamCodec, StreamCodec<RegistryFriendlyByteBuf, V> replacementStreamCodec) {
            this.privilegeStreamCodec = privilegeStreamCodec;
            this.replacementStreamCodec = replacementStreamCodec;

            CODEC = RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            PrivilegeType.CODEC.fieldOf("type").forGetter(Privilege::type),
                            Codec.STRING.fieldOf("stage").forGetter(Privilege::stage),
                            privilegeCodec.fieldOf("privilege").forGetter(Privilege::privilege),
                            valueCodec.fieldOf("replacement").forGetter(Privilege::replacement)
                    ).apply(instance, (type, stage, k, v) -> new Privilege<>((PrivilegeType<K, V>) type, stage, k, v))
            );

            STREAM_CODEC = StreamCodec.composite(
                    PrivilegeType.STREAM_CODEC,
                    Privilege::type,
                    ByteBufCodecs.STRING_UTF8,
                    Privilege::stage,
                    privilegeStreamCodec,
                    Privilege::privilege,
                    replacementStreamCodec,
                    Privilege::replacement,
                    (type, stage, k, v) -> new Privilege<>((PrivilegeType<K, V>) type, stage, k, v)
            );
        }
    }

    @Override
    public String toString() {
        return "<Privilege: " + privilege + " -> " + replacement + ">";
    }
}
