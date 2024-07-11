package com.carlschierig.privileged.api.privilege;

import com.carlschierig.privileged.api.stage.StageMap;
import com.carlschierig.privileged.impl.util.Util;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public final class PrivilegeMap<K, P, V> {
    private final Map<K, Privilege<K, P, V>> byId = new HashMap<>();

    /**
     * Returns whether the given player can access the given object.
     */
    public boolean canAccess(Player player, K key, P object) {
        var privilege = byId.get(key);
        return privilege == null
                || StageMap.getInstance().hasStage(player, privilege.stage())
                || !privilege.privilegePredicate().test(object);
    }

    /**
     * Returns the privilege for the given object or {@code null} if there isn't any.
     * Guaranteed to not be {@code null} if {@link PrivilegeMap#canAccess} returns false.
     *
     * @param object the object whose privilege should be returned.
     * @return the privilege for the given object or {@code null} if there isn't any.
     */
    public Privilege<K, P, V> getPrivilege(K object) {
        return byId.get(object);
    }

    public void addPrivilege(Privilege<K, P, V> privilege) {
        if (this.byId.containsKey(privilege.privilege())) {
            var old = this.byId.get(privilege.privilege());
            Util.LOG.warn("Overriding privilege {}", old);
        }
        this.byId.put(privilege.privilege(), privilege);
    }
}
