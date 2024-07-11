package com.carlschierig.privileged.api.privilege;

import com.carlschierig.privileged.api.registry.PrivilegedRegistries;
import com.carlschierig.privileged.impl.privilege.BlockStateOverride;
import com.carlschierig.privileged.impl.registry.PrivilegedRegistriesImpl;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public final class PrivilegeTypes {
    public static final PrivilegeType<Block, BlockState, BlockStateOverride> BLOCK = register(Util.id("block"), BlockBehaviour.BlockStateBase::getBlock);

    public static final PrivilegeType<Item, Item, ItemStack> ITEM = register(Util.id("item"), Function.identity());

    public static <K, P, V> PrivilegeType<K, P, V> register(ResourceLocation id, Function<P, K> keySupplier) {
        return PrivilegedRegistriesImpl.register(PrivilegedRegistries.PRIVILEGE_TYPE, id, new PrivilegeType<>(keySupplier));
    }

    public static void init() {
    }
}
