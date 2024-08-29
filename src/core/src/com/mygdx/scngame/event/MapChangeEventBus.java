package com.mygdx.scngame.event;

import com.badlogic.gdx.maps.tiled.TiledMap;

public interface MapChangeEventBus {

    void addMapChangeListener(MapChangeEventListener listener);
    void removeMapChangeListener(MapChangeEventListener listener);
    void clearMapChangeListener();


    void changeMap(TiledMap map, String spawnID);
}
