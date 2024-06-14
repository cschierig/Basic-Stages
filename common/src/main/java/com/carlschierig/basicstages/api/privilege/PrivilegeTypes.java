package com.carlschierig.basicstages.api.privilege;

import com.carlschierig.basicstages.api.registry.BSRegistries;
import com.carlschierig.basicstages.impl.BSUtil;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class PrivilegeTypes {
    private static final Registrar<PrivilegeType<?,?>>

    public static final PrivilegeType<Block, Block> BLOCK = register(
            BSUtil.id("block"),
            new Privilege.Serializer<>(
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.BLOCK::get, BuiltInRegistries.BLOCK::getKey),
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.BLOCK::get, BuiltInRegistries.BLOCK::getKey),
                    ByteBufCodecs.registry(Registries.BLOCK),
                    ByteBufCodecs.registry(Registries.BLOCK)
            )
    );

    private static <K, V> PrivilegeType<K, V> register(ResourceLocation id, Privilege.Serializer<K, V> serializer) {
        return Registry.register(BSRegistries.PRIVILEGE_TYPE, id, new PrivilegeType<>(serializer));
    }

    public static void init() {
    }

}
