package com.carlschierig.basicstages.impl.state;


import com.carlschierig.basicstages.api.stage.StageMap;
import com.carlschierig.basicstages.impl.util.BSUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class BSState extends SavedData {
    private static final Factory<BSState> FACTORY = new Factory<>(
            BSState::new,
            BSState::load,
            null
    );
    private static final String BS_STATE = BSUtil.MODID;

    private static BSState INSTANCE;

    public static final Codec<BSState> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    StageMap.CODEC.fieldOf("stageMap").forGetter(state -> state.map)
            ).apply(instance, BSState::new)
    );

    private final StageMap map;

    private BSState() {
        this(new StageMap());
    }

    private BSState(StageMap map) {
        this.map = map;
    }


    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CODEC.encodeStart(NbtOps.INSTANCE, this).resultOrPartial(BSUtil.LOG::error).ifPresent(result -> tag.put(BS_STATE, result));
        return tag;
    }

    public static BSState load(CompoundTag tag, HolderLookup.Provider registries) {
        return CODEC.parse(NbtOps.INSTANCE, tag.get(BS_STATE)).resultOrPartial(BSUtil.LOG::error).orElseGet(() -> {
            BSUtil.LOG.error("Could not read stage data. Resetting...");
            return new BSState();
        });
    }

    public static BSState getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isDirty() {
        // we don't save that much data, overhead should be negligible
        return true;
    }

    public static BSState setInstance(MinecraftServer server) {
        INSTANCE = server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(FACTORY, BS_STATE);

        return INSTANCE;
    }

    public StageMap getMap() {
        return map;
    }
}
