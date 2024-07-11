package com.carlschierig.privileged.api.privilege;

import java.util.function.Function;

/**
 * @param <K> The type of the key used for the privilege
 * @param <P> The type of the privilege which is passed for evaluation
 * @param <V> The type of the returned value
 */
public record PrivilegeType<K, P, V>(Function<P, K> keySupplier) {
}
