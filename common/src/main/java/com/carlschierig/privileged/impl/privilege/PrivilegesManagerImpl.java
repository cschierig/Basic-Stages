package com.carlschierig.privileged.impl.privilege;

import com.carlschierig.privileged.api.privilege.PrivilegesManager;
import com.carlschierig.privileged.api.privilege.provider.PrivilegeProvider;
import com.carlschierig.privileged.api.registry.PrivilegedRegistries;
import com.carlschierig.privileged.impl.network.S2CPackets;
import com.carlschierig.privileged.impl.util.Util;
import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PrivilegesManagerImpl extends SimpleJsonResourceReloadListener {
    private static final Codec<List<PrivilegeProvider>> CODEC = Codec.list(PrivilegedRegistries.PROVIDER_TYPE.byNameCodec().dispatch(PrivilegeProvider::getType, type -> type.serializer().codec()));
    private static final Gson GSON = new GsonBuilder().create();

    private final Supplier<HolderLookup.Provider> provider;

    public PrivilegesManagerImpl(Supplier<HolderLookup.Provider> provider) {
        super(GSON, "privilege");
        this.provider = provider;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager manager, ProfilerFiller profiler) {
        PrivilegesManager.clear();
        var count = 0;

        var ops = provider.get().createSerializationContext(JsonOps.INSTANCE);

        for (var entry : object.entrySet()) {
            var id = entry.getKey();
            var json = entry.getValue();

            try {
                var providers = CODEC.parse(ops, json).getOrThrow(JsonParseException::new);
                for (var provider : providers) {
                    PrivilegesManager.addProvider(provider);
                    count++;
                }
            } catch (JsonSyntaxException exception) {
                Util.LOG.error("Could not parse '{}' privilege syntax: {}", id, exception.getMessage());
                throw exception;
            }
        }

        Util.LOG.info("Loaded {} privilege providers.", count);

        S2CPackets.INSTANCE.clearPrivileges();
        S2CPackets.INSTANCE.sendProviders();
    }
}
