package com.carlschierig.basicstages.api.privilege;

import com.carlschierig.basicstages.api.registry.BSRegistries;
import com.carlschierig.basicstages.impl.registry.BSRegistriesImpl;
import com.carlschierig.basicstages.impl.util.BSUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class PrivilegeTypes {
    public static final PrivilegeType<Block, Block> BLOCK = register(
            BSUtil.id("block"),
            new Privilege.Serializer<>(
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.BLOCK::get, BuiltInRegistries.BLOCK::getKey),
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.BLOCK::get, BuiltInRegistries.BLOCK::getKey),
                    ByteBufCodecs.registry(Registries.BLOCK),
                    ByteBufCodecs.registry(Registries.BLOCK)
            )
    );

    public static final PrivilegeType<Item, Item> ITEM = register(
            BSUtil.id("item"),
            new Privilege.Serializer<>(
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.ITEM::get, BuiltInRegistries.ITEM::getKey),
                    ResourceLocation.CODEC.xmap(BuiltInRegistries.ITEM::get, BuiltInRegistries.ITEM::getKey),
                    ByteBufCodecs.registry(Registries.ITEM),
                    ByteBufCodecs.registry(Registries.ITEM)
            )
    );

    public static <K, V> PrivilegeType<K, V> register(ResourceLocation id, Privilege.Serializer<K, V> serializer) {
        return BSRegistriesImpl.register(BSRegistries.PRIVILEGE_TYPE, id, new PrivilegeType<>(serializer));
    }

    public static void init() {
    }
}
