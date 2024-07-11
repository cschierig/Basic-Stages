package com.carlschierig.privileged.impl.privilege;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record ResourceLocationPatch(Optional<Patch> namespace, Optional<Patch> path) {
    public static final Codec<ResourceLocationPatch> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Patch.CODEC.optionalFieldOf("namespace").forGetter(ResourceLocationPatch::namespace),
                    Patch.CODEC.optionalFieldOf("path").forGetter(ResourceLocationPatch::path)
            ).apply(instance, ResourceLocationPatch::new)
    );

    public static final StreamCodec<ByteBuf, ResourceLocationPatch> STREAM_CODEC = StreamCodec.composite(
            Patch.STREAM_CODEC.apply(ByteBufCodecs::optional),
            ResourceLocationPatch::namespace,
            Patch.STREAM_CODEC.apply(ByteBufCodecs::optional),
            ResourceLocationPatch::path,
            ResourceLocationPatch::new
    );

    public ResourceLocation replace(ResourceLocation location) {
        return ResourceLocation.tryBuild(
                namespace.map(patch -> patch.replace(location.getNamespace())).orElseGet(location::getNamespace),
                path.map(patch -> patch.replace(location.getPath())).orElseGet(location::getPath)
        );
    }

    public record Patch(String matching, String with) {
        public static final Codec<Patch> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.STRING.fieldOf("matching").forGetter(Patch::matching),
                        Codec.STRING.fieldOf("with").forGetter(Patch::with)
                ).apply(instance, Patch::new)
        );

        public static final StreamCodec<ByteBuf, Patch> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                Patch::matching,
                ByteBufCodecs.STRING_UTF8,
                Patch::with,
                Patch::new
        );

        public String replace(String old) {
            return old.replaceFirst(matching, with);
        }
    }

}
