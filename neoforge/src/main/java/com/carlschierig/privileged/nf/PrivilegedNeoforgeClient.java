package com.carlschierig.privileged.nf;

import com.carlschierig.privileged.PrivilegedClient;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Util.MODID, dist = Dist.CLIENT)
public class PrivilegedNeoforgeClient {
    public PrivilegedNeoforgeClient() {
        NeoForge.EVENT_BUS.register(this);
        PrivilegedClient.init();
    }

    @SubscribeEvent
    public void loadWorld(ClientTickEvent.Post event) {
        PrivilegedClient.rebuild(Minecraft.getInstance().levelRenderer);
    }
}
