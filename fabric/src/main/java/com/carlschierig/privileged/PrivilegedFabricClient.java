package com.carlschierig.privileged;

import com.carlschierig.privileged.impl.network.PrivilegedMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class PrivilegedFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PrivilegedClient.init();
        PrivilegedMessages.registerClientReceivers();

        ClientTickEvents.END_CLIENT_TICK.register(client ->
                PrivilegedClient.rebuild(client.levelRenderer));
    }
}
