package com.mygdx.scngame.event;

public interface GameEventBus {
    void fire(GameEvent event);
    void addEventListener(GameEventListener listener);
    void removeEventListener(GameEventListener listener);
    void clearEventListeners();
}
