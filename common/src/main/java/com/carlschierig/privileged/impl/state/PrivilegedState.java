package com.carlschierig.privileged.impl.state;


import com.carlschierig.privileged.api.stage.StageMap;
import com.carlschierig.privileged.impl.util.Util;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class PrivilegedState extends SavedData {
    private static final Factory<PrivilegedState> FACTORY = new Factory<>(
            PrivilegedState::new,
            PrivilegedState::load,
            null
    );
    private static final String P_STATE = Util.MODID;

    private static PrivilegedState INSTANCE = new PrivilegedState();

    public static final Codec<PrivilegedState> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    StageMap.CODEC.fieldOf("stageMap").forGetter(state -> state.map)
            ).apply(instance, PrivilegedState::new)
    );

    private final StageMap map;

    private PrivilegedState() {
        this(new StageMap());
    }

    private PrivilegedState(StageMap map) {
        this.map = map;
    }


    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CODEC.encodeStart(NbtOps.INSTANCE, this).resultOrPartial(Util.LOG::error).ifPresent(result -> tag.put(P_STATE, result));
        return tag;
    }

    public static PrivilegedState load(CompoundTag tag, HolderLookup.Provider registries) {
        return CODEC.parse(NbtOps.INSTANCE, tag.get(P_STATE)).resultOrPartial(Util.LOG::error).orElseGet(() -> {
            Util.LOG.error("Could not read stage data. Resetting...");
            return new PrivilegedState();
        });
    }

    public static PrivilegedState getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isDirty() {
        // we don't save that much data, overhead should be negligible
        return true;
    }

    public static PrivilegedState setInstance(MinecraftServer server) {
        INSTANCE = server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(FACTORY, P_STATE);

        return INSTANCE;
    }

    public StageMap getMap() {
        return map;
    }
}
