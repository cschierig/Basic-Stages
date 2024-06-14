package com.carlschierig.basicstages.api.privilege;

import com.carlschierig.basicstages.api.stage.StageMap;
import com.carlschierig.basicstages.impl.BSUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PrivilegeMap<K, V> {
    private final Map<K, Privilege<K, V>> byId = new HashMap<>();

    /**
     * Returns whether the given player can access the given object.
     */
    public boolean canAccess(UUID player, K object) {
        var privilege = byId.get(object);
        return privilege == null || StageMap.hasStage(player, privilege.stage());
    }

    /**
     * Returns the privilege for the given object or {@code null} if there isn't any.
     * Guaranteed to not be {@code null} if {@link PrivilegeMap#canAccess} returns false.
     *
     * @param object the object whose privilege should be returned.
     * @return the privilege for the given object or {@code null} if there isn't any.
     */
    public Privilege<K, V> getPrivilege(K object) {
        return byId.get(object);
    }

    public void addPrivilege(Privilege<K, V> privilege) {
        if (this.byId.containsKey(privilege.privilege())) {
            var old = this.byId.get(privilege.privilege());
            BSUtil.LOG.warn("Overriding privilege {} -> {}", old.privilege(), old.replacement());
        }
        this.byId.put(privilege.privilege(), privilege);
    }

    public Collection<Privilege<K, V>> getPrivileges() {
        return byId.values();
    }
}
