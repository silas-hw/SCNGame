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
public class SyncEventBus<E> extends BaseEventBus<E> {
    @Override
    public void publish(E event) {
        for (EventListener<E> listener : listeners) {
            listener.onEvent(event);
        }
    }
}
