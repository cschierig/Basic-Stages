package com.carlschierig.basicstages;

import com.carlschierig.basicstages.impl.network.BSMessages;
import net.fabricmc.api.ClientModInitializer;

public class BasicStagesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BSMessages.registerClientReceivers();
    }
}
