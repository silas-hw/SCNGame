package com.mygdx.scngame.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;

public class SaveFile {
    public String map;
    public String spawnLocation;
    public String displayName;
    public long saveDateEpoch;
    public FileHandle file;

    public boolean persist = true;

    public SaveFile(
            String map,
            String spawnLocation,
            String displayName,
            long saveDate,
            FileHandle file
    ) { {
        this.map = map;
        this.spawnLocation = spawnLocation;
        this.displayName = displayName;
        this.saveDateEpoch = saveDate;
        this.file = file;
    }}

    /*
    Conventional OOP wisdom may tell you to separate these write and read methods to their own
    reader and writer classes to separate them from the actual SaveFile.

    However, since I'm only using XML for saves, the locality of the methods is much more beneficial
    over separation of concerns.

    If I ultimately end up actually needing a separate reader, it wouldn't be too hard to make it. You'd basically
    just end up copy and pasting this method into its own class.

    9/8/2024 10:33pm
     */

    public void writeToXML() {
        if(!persist) return;

        XmlWriter writer = new XmlWriter(file.writer(false));

        try {
            writer.element("save")
                    .element("meta")
                        .element("displayName", displayName)
                        .element("saveDateEpoch", saveDateEpoch)
                    .pop()

                    .element("map")
                        .element("mapPath", map)
                        .element("spawnLocation", spawnLocation)
                    .pop()
            .pop();

            writer.flush();

        } catch(IOException e) {
            System.out.println("Failed to save");
        }

    }

    @Override
    public String toString() {

        return "map:\t" + map + "\n" +
                "spawnLocation:\t" + spawnLocation + "\n" +
                "displayName:\t" + displayName + "\n" +
                "saveDateEpoch:\t" + saveDateEpoch + "\n" +
                "file:\t" + file.name() + "\n";
    }

    public static SaveFile loadXMLSaveFile(String fileName) throws InvalidSaveFileException {
        return SaveFile.loadXMLSaveFile(Gdx.files.local(fileName));
    }

    public static SaveFile loadXMLSaveFile(FileHandle file) throws InvalidSaveFileException {
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(file);


        XmlReader.Element meta = root.getChildByName("meta");

        if(meta == null) throw new InvalidSaveFileException("meta tag missing");
        if(!(meta.hasChild("displayName") &&
             meta.hasChild("saveDateEpoch"))) throw new InvalidSaveFileException("meta tag missing content");


        String displayName = meta.getChildByName("displayName").getText();
        String strSaveDateEpoch = meta.getChildByName("saveDateEpoch").getText();

        long saveDateEpoch = 0;
        try {
             saveDateEpoch = Long.valueOf(strSaveDateEpoch, 10);
        } catch (NumberFormatException e) {
            Gdx.app.error("SaveFile", "Invalid save date epoch: " +
                                                    strSaveDateEpoch + " for save file "
                                                    + file.name());
        }

        XmlReader.Element map = root.getChildByName("map");

        if(map == null) throw new InvalidSaveFileException("map tag missing");
        if(!(map.hasChild("mapPath")
             && map.hasChild("spawnLocation"))) throw new InvalidSaveFileException("map tag missing content");

        String mapPath = map.getChildByName("mapPath").getText();
        String spawnLocation = map.getChildByName("spawnLocation").getText();

        // temp
        return new SaveFile(mapPath, spawnLocation,
                displayName, saveDateEpoch,
                file);

    }

    public static class InvalidSaveFileException extends Exception {
        public InvalidSaveFileException() {
            super("Invalid save file");
        }

        public InvalidSaveFileException(String message) {
            super("Invalid save file, " + message);
        }
    }
}
