package com.carlschierig.basicstages.impl.network;

import net.minecraft.server.level.ServerPlayer;

public abstract class S2CPackets {
    public static S2CPackets INSTANCE;

    public S2CPackets() {
        INSTANCE = this;
    }

    public abstract void sendStages(ServerPlayer player);

    public abstract void sendPrivileges();

    public abstract void clearPrivileges();

    public abstract void sendPrivileges(ServerPlayer player);

    public abstract void clearPrivileges(ServerPlayer player);
}
