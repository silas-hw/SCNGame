package com.mygdx.scngame.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class GlobalEventBus {
    private final String tag = this.getClass().getSimpleName();

    private GlobalEventBus() {}

    private static GlobalEventBus bus;

    public static GlobalEventBus getInstance() {
        if(bus == null) {
            bus = new GlobalEventBus();
        }
        return bus;
    }

    private final Array<DialogEventListener> dialogListeners = new Array<>();

    public void addDialogListener(DialogEventListener listener) {
        Gdx.app.log(tag, "Adding dialog listener: " + listener);
        dialogListeners.add(listener);
    }

    public void removeDialogListener(DialogEventListener listener) {
        Gdx.app.log(tag, "Removing dialog listener: " + listener);
        dialogListeners.removeValue(listener, true);
    }

    public void startDialog(String id) {
        Gdx.app.log(tag, "Starting dialog: <" + id + ">");
        for(DialogEventListener listener : dialogListeners) {
            listener.onDialogStart(id);
        }
    }

    public void endDialog() {
        Gdx.app.log(tag, "Ending dialog");
        for(DialogEventListener listener : dialogListeners) {
            listener.onDialogEnd();
        }
    }

    private final Array<MapChangeEventListener> mapChangeListeners = new Array<>();

    public void addMapChangeListener(MapChangeEventListener listener) {
        Gdx.app.log(tag, "Adding map listener: " + listener);
        mapChangeListeners.add(listener);
    }

    public void removeMapChangeListener(MapChangeEventListener listener) {
        Gdx.app.log(tag, "Removing map listener: " + listener);
        mapChangeListeners.removeValue(listener, true);
    }

    public void changeMap(String mapPath, String spawnID) {
        for(MapChangeEventListener listener : mapChangeListeners) {
            listener.onMapChange(mapPath, spawnID);
        }
    }
}
