package com.mygdx.scngame.event;

import com.badlogic.gdx.utils.Array;

public class Global implements GameEventBus {

    private final Array<GameEventListener> listeners = new Array<>();

    public static Global bus = new Global();
    private Global() {

    }


    @Override
    public void fire(GameEvent event) {
        System.out.println("Firing " + event);
        for(GameEventListener listener : listeners) {
            listener.notify(event);
        }
    }

    @Override
    public void addEventListener(GameEventListener listener) {
        listeners.add(listener);
        System.out.println("Listener added: " + listener);
    }

    @Override
    public void removeEventListener(GameEventListener listener) {
        listeners.removeValue(listener, false);
    }

    @Override
    public void clearEventListeners() {
        listeners.clear();
    }
}
