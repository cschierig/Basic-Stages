package com.carlschierig.privileged.api.stage.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Event class inspired by fabric's events.
 *
 * @param <T> The type of event.
 */
public class Event<T> {
    private final List<T> listeners = new ArrayList<>();
    private final Function<List<T>, T> invokerFactory;

    public Event(Function<List<T>, T> invokerFactory) {
        this.invokerFactory = invokerFactory;
    }


    public final void register(T listener) {
        listeners.add(listener);
    }

    public final T invoker() {
        return invokerFactory.apply(listeners);
    }
}
