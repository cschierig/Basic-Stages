package com.carlschierig.basicstages.api.privilege;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public abstract class PrivilegesManager {
    protected static PrivilegesManager INSTANCE;

    protected PrivilegesManager() {
        INSTANCE = this;
    }

    public static <K, V> PrivilegeMap<K, V> getMap(PrivilegeType<K, V> type) {
        return INSTANCE.getMapImpl(type);
    }

    protected abstract <K, V> PrivilegeMap<K, V> getMapImpl(PrivilegeType<K, V> type);

    public static <K, V> boolean canAccess(UUID player, PrivilegeType<K, V> type, K object) {
        return INSTANCE.canAccessImpl(player, type, object);
    }

    protected abstract <K, V> boolean canAccessImpl(UUID player, PrivilegeType<K, V> type, K object);

    public static void addPrivileges(Collection<Privilege<?, ?>> privileges) {
        INSTANCE.addPrivilegesImpl(privileges);
    }

    protected abstract void addPrivilegesImpl(Collection<Privilege<?, ?>> privileges);

    /**
     * Returns the privilege for the given type and object or null if there isn't any.
     *
     * @param type   the type of privilege which should be accessed.
     * @param object The resource location of the privilege.
     * @return the privilege for the given type and object or null if there isn't any.
     */
    public static <K, V> @Nullable Privilege<K, V> getPrivilege(PrivilegeType<K, V> type, K object) {
        return INSTANCE.getPrivilegeImpl(type, object);
    }

    protected abstract <K, V> Privilege<K, V> getPrivilegeImpl(PrivilegeType<K, V> type, K object);

    public static Map<PrivilegeType<?, ?>, PrivilegeMap<?, ?>> getPrivileges() {
        return INSTANCE.getPrivilegesImpl();
    }

    protected abstract Map<PrivilegeType<?, ?>, PrivilegeMap<?, ?>> getPrivilegesImpl();

    public static <K, V> Collection<Privilege<K, V>> getPrivileges(PrivilegeType<K, V> type) {
        return getMap(type).getPrivileges();
    }

    public static Collection<PrivilegeType<?, ?>> getTypes() {
        return INSTANCE.getTypesImpl();
    }

    protected abstract Collection<PrivilegeType<?, ?>> getTypesImpl();

    public static void clear() {
        INSTANCE.clearImpl();
    }

    protected abstract void clearImpl();
}
