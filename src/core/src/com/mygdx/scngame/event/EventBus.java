package com.mygdx.scngame.event;

public interface EventBus<E> {
    void addListener(EventListener<E> listener);
    void removeListener(EventListener<E> listener);
    void clearListeners();

    void publish(E event);
}
