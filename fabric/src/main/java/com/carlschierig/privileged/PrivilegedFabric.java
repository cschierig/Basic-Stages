package com.carlschierig.privileged;

import com.carlschierig.privileged.impl.command.BSCommands;
import com.carlschierig.privileged.impl.network.PrivilegedMessages;
import com.carlschierig.privileged.impl.network.S2CPackets;
import com.carlschierig.privileged.impl.network.S2CPacketsFabric;
import com.carlschierig.privileged.impl.privilege.PrivilegesManagerImplFabric;
import com.carlschierig.privileged.impl.registry.PrivilegedRegistriesImplFabric;
import com.carlschierig.privileged.impl.state.PrivilegedState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;

public class PrivilegedFabric implements ModInitializer {
    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        new S2CPacketsFabric();
        new PrivilegedRegistriesImplFabric();

        PrivilegedCommon.init();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new PrivilegesManagerImplFabric());

        PrivilegedMessages.registerPayloadsS2C();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                BSCommands.register(dispatcher)
        );

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            S2CPackets.INSTANCE.sendStages(handler.player);
            S2CPackets.INSTANCE.clearPrivileges(handler.player);
            S2CPackets.INSTANCE.sendPrivileges(handler.player);
        });

        ServerLifecycleEvents.SERVER_STARTING.register(server -> PrivilegedFabric.server = server);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> PrivilegedFabric.server = null);

        ServerWorldEvents.LOAD.register((server, world) -> PrivilegedState.setInstance(server));
    }
}
