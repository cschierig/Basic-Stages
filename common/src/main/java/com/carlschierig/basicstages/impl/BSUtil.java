package com.carlschierig.basicstages.impl;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
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
}
