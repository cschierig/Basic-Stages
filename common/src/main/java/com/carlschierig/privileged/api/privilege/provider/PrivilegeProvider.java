package com.carlschierig.privileged.api.privilege.provider;


import com.carlschierig.privileged.api.privilege.Privilege;
import com.carlschierig.privileged.impl.registry.PrivilegedRegistryKeys;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collection;

/**
 * A serializable provider for privileges
 */
public abstract class PrivilegeProvider {
    public static final StreamCodec<RegistryFriendlyByteBuf, PrivilegeProvider> STREAM_CODEC = ByteBufCodecs.registry(PrivilegedRegistryKeys.PROVIDER_TYPE)
            .dispatch(
                    PrivilegeProvider::getType,
                    type -> type.serializer().streamCodec()
            );

    private final String stage;

    public PrivilegeProvider(String stage) {
        this.stage = stage;
    }

    public String getStage() {
        return stage;
    }

    public abstract ProviderType<?> getType();

    public abstract Collection<Privilege<?, ?, ?>> getPrivileges();

    public record Serializer<T extends PrivilegeProvider>(MapCodec<T> codec,
                                                          StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
    }
}
