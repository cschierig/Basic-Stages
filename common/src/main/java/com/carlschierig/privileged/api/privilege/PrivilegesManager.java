package com.carlschierig.privileged.api.privilege;

import com.carlschierig.privileged.api.privilege.provider.PrivilegeProvider;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class PrivilegesManager {
    private static Map<PrivilegeType<?, ?, ?>, PrivilegeMap<?, ?, ?>> privileges = new HashMap<>();
    private static final List<PrivilegeProvider> newProviders = new ArrayList<>();
    private static final List<PrivilegeProvider> allProviders = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private static <K, P, V> PrivilegeMap<K, P, V> getMap(PrivilegeType<K, P, V> type) {
        return (PrivilegeMap<K, P, V>) privileges.get(type);
    }

    /**
     * Checks if the player can access the given object.
     * Always true if the player is in creative mode.
     *
     * @param player The {@link Player} whose privileges should be checked.
     * @param type   The type of privilege to check.
     * @param object The object whose access should be checked.
     * @return true if the player can access the given object or is in creative mode, false otherwise.
     */
    @SuppressWarnings("unchecked")
    public static <K, P, V> boolean canAccess(Player player, PrivilegeType<K, P, V> type, P object) {
        evaluateProviders();
        // TODO: disable in creative
        var map = (PrivilegeMap<K, P, V>) privileges.get(type);
        return map == null || map.canAccess(player, type.keySupplier().apply(object), object);
    }

    private static void addPrivileges(Collection<Privilege<?, ?, ?>> privileges) {
        for (var privilege : privileges) {
            addPrivilege(privilege);
        }
    }

    private static <K, P, V> void addPrivilege(Privilege<K, P, V> privilege) {
        var type = privilege.type();
        privileges.putIfAbsent(privilege.type(), new PrivilegeMap<>());
        getMap(type).addPrivilege(privilege);
    }

    /**
     * Returns the privilege for the given type and object or null if there isn't any.
     *
     * @param type   the type of privilege which should be accessed.
     * @param object The resource location of the privilege.
     * @return the privilege for the given type and object or null if there isn't any.
     */
    @SuppressWarnings("unchecked")
    public static <K, P, V> @Nullable Privilege<K, P, V> getPrivilege(PrivilegeType<K, P, V> type, K object) {
        evaluateProviders();
        return ((PrivilegeMap<K, P, V>) privileges.get(type)).getPrivilege(object);
    }

    public static void addProvider(PrivilegeProvider provider) {
        newProviders.add(provider);
        allProviders.add(provider);
    }

    public static void addProviders(Collection<PrivilegeProvider> providers) {
        newProviders.addAll(providers);
        allProviders.addAll(providers);
    }

    public static List<PrivilegeProvider> getProviders() {
        return allProviders;
    }

    private static void evaluateProviders() {
        if (newProviders.isEmpty()) {
            return;
        }
        for (int i = newProviders.size() - 1; i >= 0; i--) {
            var provider = newProviders.get(i);
            addPrivileges(provider.getPrivileges());
            newProviders.remove(i);
        }
    }

    public static void clear() {
        privileges.clear();
        newProviders.clear();
        allProviders.clear();
    }
}
