package com.carlschierig.privileged.api.privilege;

import com.carlschierig.privileged.api.registry.PrivilegedRegistries;
import com.carlschierig.privileged.impl.registry.PrivilegedRegistriesImpl;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class PrivilegeTypes {
    public static final PrivilegeType<Block, Block> BLOCK = register(
            Util.id("block"),
            new Privilege.Serializer<>(
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.BLOCK::get, BuiltInRegistries.BLOCK::getKey),
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.BLOCK::get, BuiltInRegistries.BLOCK::getKey),
                    ByteBufCodecs.registry(Registries.BLOCK),
                    ByteBufCodecs.registry(Registries.BLOCK)
            )
    );

    public static final PrivilegeType<Item, Item> ITEM = register(
            Util.id("item"),
            new Privilege.Serializer<>(
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.ITEM::get, BuiltInRegistries.ITEM::getKey),
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.ITEM::get, BuiltInRegistries.ITEM::getKey),
                    ByteBufCodecs.registry(Registries.ITEM),
                    ByteBufCodecs.registry(Registries.ITEM)
            )
    );

    public static <K, V> PrivilegeType<K, V> register(ResourceLocation id, Privilege.Serializer<K, V> serializer) {
        return PrivilegedRegistriesImpl.register(PrivilegedRegistries.PRIVILEGE_TYPE, id, new PrivilegeType<>(serializer));
    }

    public static void init() {
    }
}
