package com.mygdx.scngame.save;

import com.badlogic.gdx.files.FileHandle;

import java.time.Instant;

public class SaveSystem {
    private final SaveFile saveFile;

    public SaveSystem(SaveFile saveFile) {
        this.saveFile = saveFile;
    }

    public void save(String mapPath, String spawnLocation, String displayName) {
        saveFile.map = mapPath;
        saveFile.saveDateEpoch = Instant.now().getEpochSecond();
        saveFile.displayName = displayName;
        saveFile.spawnLocation = spawnLocation;

        saveFile.writeToXML();
    }
}
