package com.carlschierig.privileged.nf.impl.network;

import com.carlschierig.privileged.impl.network.payloads.ClearPrivilegesPayload;
import com.carlschierig.privileged.impl.network.payloads.PlayerStagesPayload;
import com.carlschierig.privileged.impl.network.payloads.PrivilegePayload;
import com.carlschierig.privileged.impl.network.payloads.StageUpdatePayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PrivilegedMessages {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(PlayerStagesPayload.TYPE, PlayerStagesPayload.STREAM_CODEC, ClientPacketReceiver::receiveStages);
        registrar.playToClient(StageUpdatePayload.TYPE, StageUpdatePayload.STREAM_CODEC, ClientPacketReceiver::receiveStageUpdate);
        registrar.playToClient(PrivilegePayload.TYPE, PrivilegePayload.STREAM_CODEC, ClientPacketReceiver::receivePrivileges);
        registrar.playToClient(ClearPrivilegesPayload.TYPE, ClearPrivilegesPayload.STREAM_CODEC, ClientPacketReceiver::receiveClear);
    }
}
