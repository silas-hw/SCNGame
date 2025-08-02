package com.mygdx.scngame.event;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseEventBus<E> implements EventBus<E> {
    protected final Set<EventListener<E>> listeners = new HashSet<>(10);

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
}
