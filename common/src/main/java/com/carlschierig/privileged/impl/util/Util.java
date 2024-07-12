package com.carlschierig.privileged.impl.util;

import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.ByteBuf;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
    public static final String MODID = "privileged";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType(String value) {
        return new CustomPacketPayload.Type<>(id(value));
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.tryBuild(MODID, name);
    }

    public static final StreamCodec<ByteBuf, BlockState> BLOCK_STATE_STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
            string -> {
                try {
                    return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), string, true).blockState();
                } catch (CommandSyntaxException e) {
                    LOG.error("Error decoding block state.", e);
                    throw new RuntimeException();
                }
            },
            BlockStateParser::serialize
    );

    public static BlockState replace(BlockState state, Player uuid) {
        if (uuid != null && !PrivilegesManager.canAccess(uuid, PrivilegeTypes.BLOCK, state)) {
            var privilege = PrivilegesManager.getPrivilege(PrivilegeTypes.BLOCK, state.getBlock());

            var replacement = privilege.replacement();
            var newState = replacement.base();
            var properties = replacement.overriddenProperties();
            for (var entry : state.getValues().entrySet()) {
                var key = entry.getKey();
                if (newState.hasProperty(key) && !properties.contains(key.getName())) {
                    newState = withValue(newState, key, entry.getValue());
                }
            }
            return newState;
        }
        return state;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> BlockState withValue(BlockState state, Property<?> property, Comparable<?> value) {
        return state.setValue((Property<T>) property, (T) value);
    }

    public static Item replace(Item item, Player uuid) {
        if (uuid != null && !PrivilegesManager.canAccess(uuid, PrivilegeTypes.ITEM, item)) {
            var privilege = PrivilegesManager.getPrivilege(PrivilegeTypes.ITEM, item);

            return privilege.replacement().getItem();
        }
        return item;
    }

    public static ItemStack replace(ItemStack stack, Player uuid) {
        if (uuid != null && !PrivilegesManager.canAccess(uuid, PrivilegeTypes.ITEM, stack.getItem())) {
            var privilege = PrivilegesManager.getPrivilege(PrivilegeTypes.ITEM, stack.getItem());

            return privilege.replacement().copy();
        }
        return stack;
    }
}
