package com.carlschierig.privileged.compat.jade;

import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.*;

@WailaPlugin
public class PrivilegedJadePlugin implements IWailaPlugin {

    private @Nullable ItemEntity cachedEntity;

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addRayTraceCallback((hitResult, accessor, accessor1) -> {
            if (accessor instanceof BlockAccessor block) {
                return registration.blockAccessor().from(block).blockState(Util.replace(block.getBlockState(), block.getPlayer())).build();
            } else if (accessor instanceof EntityAccessor entity) {
                if (entity.getEntity() instanceof ItemEntity item) {
                    var location = hitResult.getLocation();
                    var replacement = Util.replace(item.getItem().getItem(), entity.getPlayer());
                    var returnValue = replacement.equals(cachedEntity != null ? cachedEntity.getItem() : null)
                            ? cachedEntity
                            : new ItemEntity(accessor.getLevel(), location.x, location.y, location.z, replacement.getDefaultInstance());
                    returnValue.makeFakeItem();
                    return registration.entityAccessor().from(entity).entity(returnValue).build();
                }
            }
            return accessor;
        });
    }
}
