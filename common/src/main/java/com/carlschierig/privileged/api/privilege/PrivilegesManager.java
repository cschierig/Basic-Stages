package com.carlschierig.privileged.api.privilege;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public abstract class PrivilegesManager {
    protected static PrivilegesManager INSTANCE;

    protected PrivilegesManager() {
        INSTANCE = this;
    }

    public static <K, V> PrivilegeMap<K, V> getMap(PrivilegeType<K, V> type) {
        return INSTANCE.getMapImpl(type);
    }

    protected abstract <K, V> PrivilegeMap<K, V> getMapImpl(PrivilegeType<K, V> type);

    /**
     * Checks if the player can access the given object.
     * Always true if the player is in creative mode.
     *
     * @param player The {@link Player} whose privileges should be checked.
     * @param type   The type of privilege to check.
     * @param object The object whose access should be checked.
     * @return true if the player can access the given object or is in creative mode, false otherwise.
     */
    public static <K, V> boolean canAccess(Player player, PrivilegeType<K, V> type, K object) {
        // TODO: disable in creative
        return INSTANCE.canAccessImpl(player, type, object);
    }

    /**
     * Checks if the player can access the given object.
     *
     * @param player The {@link Player} whose privileges should be checked.
     * @param type   The type of privilege to check.
     * @param object The object whose access should be checked.
     * @return true if the player can access the given object, false otherwise.
     */
    protected abstract <K, V> boolean canAccessImpl(Player player, PrivilegeType<K, V> type, K object);

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
