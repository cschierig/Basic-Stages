package com.carlschierig.privileged.nf;

import com.carlschierig.privileged.PrivilegedCommon;
import com.carlschierig.privileged.impl.command.BSCommands;
import com.carlschierig.privileged.impl.network.S2CPackets;
import com.carlschierig.privileged.impl.privilege.PrivilegesManagerImpl;
import com.carlschierig.privileged.impl.state.PrivilegedState;
import com.carlschierig.privileged.impl.util.Util;
import com.carlschierig.privileged.nf.impl.network.PrivilegedMessages;
import com.carlschierig.privileged.nf.impl.network.S2CPacketsNeoforge;
import com.carlschierig.privileged.nf.impl.registry.PrivilegedRegistriesImplNeoforge;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Util.MODID)
public class PrivilegedNeoforge {
    public PrivilegedNeoforge(IEventBus bus) {
        NeoForge.EVENT_BUS.register(this);
        bus.register(PrivilegedMessages.class);

        new S2CPacketsNeoforge();
        bus.register(new PrivilegedRegistriesImplNeoforge(bus));

        PrivilegedCommon.init();
    }

    @SubscribeEvent
    public void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new PrivilegesManagerImpl());
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        BSCommands.register(dispatcher);
    }

    @SubscribeEvent
    public void playerJoined(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            S2CPackets.INSTANCE.sendStages(player);
            S2CPackets.INSTANCE.clearPrivileges(player);
            S2CPackets.INSTANCE.sendPrivileges(player);
        }
    }

    @SubscribeEvent
    public void loadWorld(ServerStartingEvent event) {
        PrivilegedState.setInstance(event.getServer());
    }

}
