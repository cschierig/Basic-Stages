package com.carlschierig.privileged;

import com.carlschierig.privileged.api.advancement.criterion.PrivilegedCriteriaTriggers;
import com.carlschierig.privileged.api.privilege.PrivilegeTypes;
import com.carlschierig.privileged.api.stage.event.PrivilegedEvents;
import com.carlschierig.privileged.impl.network.S2CPackets;
import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagNetworkSerialization;

public class PrivilegedCommon {
    public static void init() {
        PrivilegeTypes.init();
        PrivilegedCriteriaTriggers.init();

        PrivilegedEvents.STAGE_GAINED.register((player, stage) -> {
                    if (player instanceof ServerPlayer serverPlayer) {
                        S2CPackets.INSTANCE.sendStageUpdate(serverPlayer, stage, true);
                        simulateReload(serverPlayer);
                    }
                }
        );
        PrivilegedEvents.STAGE_REMOVED.register((player, stage) -> {
                    if (player instanceof ServerPlayer serverPlayer) {
                        S2CPackets.INSTANCE.sendStageUpdate(serverPlayer, stage, false);
                        simulateReload(serverPlayer);
                    }
                }
        );

    }

    private static void simulateReload(ServerPlayer player) {
        player.connection.send(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(player.server.registries())));
        var clientboundUpdateRecipesPacket = new ClientboundUpdateRecipesPacket(player.server.getRecipeManager().getOrderedRecipes());

        player.connection.send(clientboundUpdateRecipesPacket);
        player.getRecipeBook().sendInitialRecipeBook(player);
    }
}
