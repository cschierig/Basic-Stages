package com.carlschierig.privileged.api.privilege.provider;


import com.carlschierig.privileged.api.privilege.Privilege;
import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.impl.privilege.BlockStateOverride;
import com.carlschierig.privileged.impl.privilege.ResourceLocationPatch;
import com.carlschierig.privileged.impl.util.Util;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class BlockProvider extends PrivilegeProvider {
    private final HolderSet<Block> privileges;
    private final Either<BlockStateOverride, ResourceLocationPatch> replacement;
    private final boolean replaceItems;

    public BlockProvider(String stage,
                         HolderSet<Block> privileges,
                         Either<BlockStateOverride, ResourceLocationPatch> replacement,
                         boolean replaceItems
    ) {
        super(stage);
        this.privileges = privileges;
        this.replacement = replacement;
        this.replaceItems = replaceItems;
    }

    @Override
    public ProviderType<?> getType() {
        return ProviderTypes.BLOCK;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Privilege<?, ?, ?>> getPrivileges() {
        List<Privilege<?, ?, ?>> list = new ArrayList<>();

        if (replacement.left().isPresent()) {
            var override = replacement.left().get();
            for (var privilege : privileges) {
                list.add(
                        new Privilege<>(PrivilegeTypes.BLOCK, getStage(), privilege.unwrap().map(BuiltInRegistries.BLOCK::get, block -> block), p -> true, override)
                );
            }
        } else {
            var patch = replacement.right().orElseThrow();
            for (var privilege : privileges) {
                var block = privilege.unwrap().map(BuiltInRegistries.BLOCK::get, Function.identity());
                var oldId = BuiltInRegistries.BLOCK.getKey(block);
                var newId = patch.replace(oldId);
                var newBlock = BuiltInRegistries.BLOCK.getOptional(newId);
                if (newBlock.isPresent()) {
                    list.add(
                            new Privilege<>(PrivilegeTypes.BLOCK, getStage(), block, p -> true, new BlockStateOverride(newBlock.get().defaultBlockState(), Set.of()))
                    );
                } else {
                    Util.LOG.warn("Could not patch resource location '{}'. The new resource location '{}' isn't an item", oldId, newId);
                }
            }
        }
        if (replaceItems) {
            for (int i = 0, listSize = list.size(); i < listSize; i++) {
                var privilege = list.get(i);
                var cast = (Privilege<Block, BlockState, BlockStateOverride>) privilege;
                list.add(
                        new Privilege<>(PrivilegeTypes.ITEM, getStage(), cast.privilege().asItem(), p -> true, cast.replacement().base().getBlock().asItem().getDefaultInstance())
                );
            }
        }
        return list;
    }

    public static final MapCodec<BlockProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.fieldOf("stage").forGetter(BlockProvider::getStage),
                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("privileges").forGetter(p -> p.privileges),
                    Codec.either(BlockStateOverride.CODEC, ResourceLocationPatch.CODEC).fieldOf("replacement").forGetter(p -> p.replacement),
                    Codec.BOOL.optionalFieldOf("replaceItems", true).forGetter(p -> p.replaceItems)
            ).apply(instance, BlockProvider::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BlockProvider> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BlockProvider::getStage,
            ByteBufCodecs.holderSet(Registries.BLOCK),
            p -> p.privileges,
            ByteBufCodecs.either(
                    BlockStateOverride.STREAM_CODEC,
                    ResourceLocationPatch.STREAM_CODEC
            ),
            p -> p.replacement,
            ByteBufCodecs.BOOL,
            p -> p.replaceItems,
            BlockProvider::new
    );
}
