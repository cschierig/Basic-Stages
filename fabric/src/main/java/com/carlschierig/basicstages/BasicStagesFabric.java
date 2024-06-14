package com.carlschierig.basicstages;

import com.carlschierig.basicstages.impl.network.BSMessages;
import com.carlschierig.basicstages.impl.network.S2CPackets;
import com.carlschierig.basicstages.impl.network.S2CPacketsFabric;
import com.carlschierig.basicstages.impl.privilege.PrivilegesManagerImplFabric;
import com.carlschierig.basicstages.impl.registry.RegistryBuilderFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;

public class BasicStagesFabric implements ModInitializer {
    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        new S2CPacketsFabric();
        new RegistryBuilderFabric();

        BasicStagesCommon.init();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new PrivilegesManagerImplFabric());

        BSMessages.registerPayloadsS2C();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            S2CPackets.INSTANCE.sendStages(handler.player);
            S2CPackets.INSTANCE.clearPrivileges(handler.player);
            S2CPackets.INSTANCE.sendPrivileges(handler.player);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> BasicStagesFabric.server = server);
    }
}
