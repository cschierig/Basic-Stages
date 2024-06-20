package com.carlschierig.privileged.nf.impl.registry;

import com.carlschierig.privileged.impl.registry.PrivilegedRegistriesImpl;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivilegedRegistriesImplNeoforge extends PrivilegedRegistriesImpl {
    private final Map<ResourceKey<Registry<?>>, DeferredRegister<?>> registers = new HashMap<>();
    private final List<Registry<?>> registries = new ArrayList<>();
    private final IEventBus bus;

    public PrivilegedRegistriesImplNeoforge(IEventBus bus) {
        this.bus = bus;
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        var registry = new RegistryBuilder<>(registryKey).sync(true).create();
        registries.add(registry);
        return registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T registerImpl(Registry<? super T> registry, ResourceLocation id, T item) {
        var deferred = (DeferredRegister<T>) registers.computeIfAbsent((ResourceKey<Registry<?>>) registry.key(), key -> {
            var register = DeferredRegister.create(registry, Util.MODID);
            register.register(bus);
            return register;
        });
        deferred.register(id.getPath(), () -> item);
        return item;
    }

    @SubscribeEvent
    public void registerRegistries(NewRegistryEvent event) {
        for (var registry : registries) {
            event.register(registry);
        }
    }
}
