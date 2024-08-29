package com.mygdx.scngame.event;

import com.badlogic.gdx.maps.tiled.TiledMap;

public interface MapChangeEventListener {
    void onMapChange(TiledMap map, String spawnID);
}
