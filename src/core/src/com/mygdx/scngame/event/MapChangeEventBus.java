package com.mygdx.scngame.event;

public interface MapChangeEventBus {

    void addMapChangeListener(MapChangeEventListener listener);
    void removeMapChangeListener(MapChangeEventListener listener);
    void clearMapChangeListener();

    void changeMap(String mapPath, String spawnID);
}
