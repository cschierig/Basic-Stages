package com.carlschierig.privileged.api.privilege.provider;

import com.carlschierig.privileged.api.privilege.Privilege;
import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.impl.privilege.BlockStateOverride;
import com.carlschierig.privileged.impl.privilege.BlockStatePropertiesCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BlockStateProvider extends PrivilegeProvider {
    private final BlockStateOverride privilege;
    private final BlockStateOverride replacement;
    private final boolean replaceItem;

    public BlockStateProvider(String stage,
                              BlockState privilege,
                              Set<String> privilegeProperties,
                              BlockState replacement,
                              Set<String> replacementProperties,
                              boolean replaceItem
    ) {
        this(stage, new BlockStateOverride(privilege, privilegeProperties), new BlockStateOverride(replacement, replacementProperties), replaceItem);
    }

    public BlockStateProvider(String stage,
                              BlockStateOverride privilege,
                              BlockStateOverride replacement,
                              boolean replaceItem
    ) {
        super(stage);
        this.privilege = privilege;
        this.replacement = replacement;
        this.replaceItem = replaceItem;
    }

    @Override
    public ProviderType<?> getType() {
        return ProviderTypes.BLOCK_STATE;
    }

    @Override
    public Collection<Privilege<?, ?, ?>> getPrivileges() {
        List<Privilege<?, ?, ?>> list = new ArrayList<>();

        list.add(
                new Privilege<>(PrivilegeTypes.BLOCK, getStage(), privilege.base().getBlock(), privilege::matches, replacement)
        );

        if (replaceItem) {
            list.add(
                    new Privilege<>(PrivilegeTypes.ITEM, getStage(), privilege.base().getBlock().asItem(), p -> true, replacement.base().getBlock().asItem().getDefaultInstance())
            );
        }
        return list;
    }

    public static final MapCodec<BlockStateProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.fieldOf("stage").forGetter(BlockStateProvider::getStage),
                    BlockState.CODEC.fieldOf("privilege").forGetter(p -> p.privilege.base()),
                    new BlockStatePropertiesCodec().fieldOf("privilege").forGetter(p -> p.privilege.overriddenProperties()),
                    BlockState.CODEC.fieldOf("replacement").forGetter(p -> p.replacement.base()),
                    new BlockStatePropertiesCodec().fieldOf("replacement").forGetter(p -> p.replacement.overriddenProperties()),
                    Codec.BOOL.optionalFieldOf("replaceItem", true).forGetter(p -> p.replaceItem)
            ).apply(instance, BlockStateProvider::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BlockStateProvider> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BlockStateProvider::getStage,
            BlockStateOverride.STREAM_CODEC,
            p -> p.privilege,
            BlockStateOverride.STREAM_CODEC,
            p -> p.replacement,
            ByteBufCodecs.BOOL,
            p -> p.replaceItem,
            BlockStateProvider::new
    );
}
