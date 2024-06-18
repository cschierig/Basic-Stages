package com.carlschierig.basicstages.impl.util;

import com.carlschierig.basicstages.api.privilege.PrivilegeTypes;
import com.carlschierig.basicstages.api.privilege.PrivilegesManager;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BSUtil {
    public static final String MODID = "basic_stages";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType(String value) {
        return CustomPacketPayload.createType(MODID + ":" + value);
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }

    public static BlockState replace(BlockState state, Player uuid) {
        if (uuid != null && !PrivilegesManager.canAccess(uuid, PrivilegeTypes.BLOCK, state.getBlock())) {
            var privilege = PrivilegesManager.getPrivilege(PrivilegeTypes.BLOCK, state.getBlock());

            return privilege.replacement().defaultBlockState();
        }
        return state;
    }

    public static Item replace(Item item, Player uuid) {
        if (uuid != null && !PrivilegesManager.canAccess(uuid, PrivilegeTypes.ITEM, item)) {
            var privilege = PrivilegesManager.getPrivilege(PrivilegeTypes.ITEM, item);

            return privilege.replacement();
        }
        return item;
    }
}
