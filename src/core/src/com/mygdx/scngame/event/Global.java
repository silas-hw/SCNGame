package com.mygdx.scngame.event;

import com.badlogic.gdx.utils.Array;

public class Global implements GameEventBus {

    private final Array<GameEventListener> listeners = new Array<>();

    public static Global bus = new Global();
    private Global() {

    }


    @Override
    public void fire(GameEvent event) {
        for(GameEventListener listener : listeners) {
            if(listener != event.getSource()) listener.notify(event);
        }
    }

    @Override
    public void addEventListener(GameEventListener listener) {
        listeners.add(listener);
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
