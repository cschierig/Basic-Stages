package com.carlschierig.basicstages;

import com.carlschierig.basicstages.impl.network.BSMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BasicStagesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BasicStagesClient.init();
        BSMessages.registerClientReceivers();

        ClientTickEvents.END_CLIENT_TICK.register(client ->
                BasicStagesClient.rebuild(client.levelRenderer));
    }
}
