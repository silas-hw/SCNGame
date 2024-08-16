package com.mygdx.scngame.screens;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.map.MyTmxMapLoader;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.settings.Settings;

import java.net.URL;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

// TODO: load animations and sound files

public class AssetLoadingScreen implements Screen {

    private static final String logTag = "AssetLoadingScreen";
    private Array<String> assetMap = new Array<>();

    Game game;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    Settings settings;

    ScreenData screenData;

    public AssetLoadingScreen(ScreenData args) {
        this.game = args.game();
        this.batch = args.batch();
        this.shapeRenderer = args.shapeRenderer();

        this.settings = args.settings();
        this.screenData = args;

        assetManager = screenData.assets();

        FileHandle assetsFile = Gdx.files.internal("assets.txt");
        String assetMapString = assetsFile.readString();

        StringBuffer buffer = new StringBuffer();
        for(char c : assetMapString.toCharArray()) {
            if(c == '\0') {
                assetMap.add(buffer.toString());
                buffer.delete(0, buffer.length());
                continue;
            }

            buffer.append(c);
        }
    }

    AssetManager assetManager;


    // this feels very hacky
    final static String[] _spriteExtensions = {
            "png", "jpg", "jpeg", "gif"
    };

    final static String[] _tilemapExtensions = {
            "tmx"
    };

    final static String[] _skinExtensions = {
            "json"
    };

    final static String[] _audioExtensions = {
            "mp3", "wav"
    };

    final static Set<String> spriteExtensions = new HashSet<>(Arrays.asList(_spriteExtensions));
    final static Set<String> tilemapExtensions = new HashSet<>(Arrays.asList(_tilemapExtensions));
    final static Set<String> skinExtensions = new HashSet<>(Arrays.asList(_skinExtensions));
    final static Set<String> audioExtensions = new HashSet<>(Arrays.asList(_audioExtensions));

    @Override
    public void show() {
        assetManager.setLoader(TiledMap.class, new MyTmxMapLoader(new InternalFileHandleResolver()));

        for(String filePath : assetMap) {
            String extension = Gdx.files.internal(filePath).extension();

            if(filePath.startsWith("sprites/") && spriteExtensions.contains(extension)) {
                assetManager.load(filePath, Texture.class);
                Gdx.app.log(logTag, filePath + " set to load as Sprite");
            } else if(filePath.startsWith("tilemaps/") && tilemapExtensions.contains(extension)) {
                assetManager.load(filePath, TiledMap.class);
                Gdx.app.log(logTag, filePath + " set to load as TiledMap (tmx)");
            } else if(filePath.startsWith("skin/") && skinExtensions.contains(extension)) {
                assetManager.load(filePath, Skin.class);
                Gdx.app.log(logTag, filePath + " set to load as Skin");
            } else if(filePath.startsWith("music/") && audioExtensions.contains(extension)) {
                assetManager.load(filePath, Music.class);
                Gdx.app.log(logTag, filePath + " set to load as Music");
            } else if(filePath.startsWith("sfx/") && audioExtensions.contains(extension)) {
                assetManager.load(filePath, Sound.class);
                Gdx.app.log(logTag, filePath + " set to load as Sound");
            } else {
                Gdx.app.log(logTag, filePath + " IGNORED");
                Gdx.app.debug(logTag, "Warning: file may have been placed in incorrect directory");
            }
        }

        Gdx.app.log(logTag, "All assets set to load!");
    }

    // TODO: inform user of asset loading graphically

    @Override
    public void render(float delta) {
        Gdx.app.log(logTag, "Loading assets... " + assetManager.getProgress()*100 + "%");

        if(assetManager.isFinished()) {
            game.setScreen(new MainMenuScreen(screenData));
            return;
        }

        assetManager.update();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
