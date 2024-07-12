package com.carlschierig.privileged.api.privilege.provider;

import com.carlschierig.privileged.api.privilege.Privilege;
import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class ItemProvider extends PrivilegeProvider {
    private final HolderSet<Item> privileges;
    private final Either<ItemStack, ResourceLocationPatch> replacement;

    public ItemProvider(String stage,
                        HolderSet<Item> privileges,
                        Either<ItemStack, ResourceLocationPatch> replacement
    ) {
        super(stage);
        this.privileges = privileges;
        this.replacement = replacement;
    }

    @Override
    public ProviderType<?> getType() {
        return ProviderTypes.ITEM;
    }

    @Override
    public Collection<Privilege<?, ?, ?>> getPrivileges() {
        List<Privilege<?, ?, ?>> list = new ArrayList<>();
        if (replacement.left().isPresent()) {
            var stack = replacement.left().orElseThrow();
            for (var privilege : privileges) {
                list.add(
                        new Privilege<>(PrivilegeTypes.ITEM, getStage(), privilege.unwrap().map(BuiltInRegistries.ITEM::get, Function.identity()), p -> true, stack)
                );
            }
        } else {
            var patch = replacement.right().orElseThrow();
            for (var privilege : privileges) {
                var item = privilege.unwrap().map(BuiltInRegistries.ITEM::get, Function.identity());
                var oldId = BuiltInRegistries.ITEM.getKey(item);
                var newId = patch.replace(oldId);
                if (!newId.equals(oldId)) {
                    var newItem = BuiltInRegistries.ITEM.getOptional(newId);
                    if (newItem.isPresent()) {
                        list.add(
                                new Privilege<>(PrivilegeTypes.ITEM, getStage(), item, p -> true, newItem.get().getDefaultInstance())
                        );
                    } else {
                        Util.LOG.warn("Could not patch resource location '{}'. The new resource location '{}' isn't an item", oldId, newId);
                    }
                }
            }
        }

        return list;
    }

    public static final MapCodec<ItemProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.fieldOf("stage").forGetter(ItemProvider::getStage),
                    RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("privilege").forGetter(p -> p.privileges),
                    Codec.either(
                            Codec.withAlternative(
                                    ItemStack.CODEC,
                                    ResourceLocation.CODEC.xmap(
                                            id -> BuiltInRegistries.ITEM.get(id).getDefaultInstance(),
                                            item -> BuiltInRegistries.ITEM.getKey(item.getItem())
                                    )
                            ),
                            ResourceLocationPatch.CODEC
                    ).fieldOf("replacement").forGetter(p -> p.replacement)
            ).apply(instance, ItemProvider::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemProvider> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ItemProvider::getStage,
            ByteBufCodecs.holderSet(Registries.ITEM),
            p -> p.privileges,
            ByteBufCodecs.either(ItemStack.STREAM_CODEC, ResourceLocationPatch.STREAM_CODEC),
            p -> p.replacement,
            ItemProvider::new
    );
}
