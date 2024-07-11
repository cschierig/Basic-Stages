package com.carlschierig.privileged.api.privilege;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @param privilege          The privilege which is granted when the player has reached the stage.
 * @param privilegePredicate A predicate which is used
 * @param replacement        The with which is used instead of the privilege as long as a player doesn't
 *                           have access to it.
 */
public record Privilege<K, P, V>(PrivilegeType<K, P, V> type, String stage, @NotNull K privilege,
                                 @NotNull Predicate<P> privilegePredicate, @NotNull V replacement) {

    @Override
    public String toString() {
        return "<Privilege: " + privilege + " -> " + replacement + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Privilege<?, ?, ?> privilege1 = (Privilege<?, ?, ?>) o;
        return Objects.equals(privilege, privilege1.privilege) && Objects.equals(stage, privilege1.stage) && Objects.equals(replacement, privilege1.replacement) && Objects.equals(type, privilege1.type) && Objects.equals(privilegePredicate, privilege1.privilegePredicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, stage, privilege, privilegePredicate, replacement);
    }
}
