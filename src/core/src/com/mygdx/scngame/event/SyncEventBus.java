package com.mygdx.scngame.event;

import java.util.HashSet;
import java.util.Set;

/*
A form of `EventBus` that acts synchronously, meaning events are sent
to listeners immediately upon publishing. This is ideal for things
that don't happen too often and don't have a large number of listeners.

If an event is more dependent on game stat, it's best to use an asynchronous bus that
queues events.
 */
public class SyncEventBus<E> implements EventBus<E> {
    private final Set<EventListener<E>> listeners = new HashSet<>(10);

    @Override
    public void addListener(EventListener<E> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener<E> listener) {
        listeners.remove(listener);
    }

    @Override
    public void clearListeners() {
        listeners.clear();
    }

    @Override
    public void publish(E event) {
        for (EventListener<E> listener : listeners) {
            listener.onEvent(event);
        }
    }
}
