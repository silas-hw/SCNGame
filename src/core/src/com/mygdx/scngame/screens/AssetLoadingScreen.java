package com.mygdx.scngame.screens;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
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
        assetManager = screenData.assets();

        loadAssets(assetManager, "sprites/", spriteExtensions, Texture.class);
        loadAssets(assetManager, "skin/", skinExtensions, Skin.class);
        loadAssets(assetManager, "music/", audioExtensions, Music.class);

        assetManager.setLoader(TiledMap.class, new MyTmxMapLoader(new InternalFileHandleResolver()));
        loadAssets(assetManager, "tilemaps/", tilemapExtensions, TiledMap.class);

        Gdx.app.log(logTag, "All assets set to load!");

    }

    // used to load all the assets in a given directory matching a set of extensions into an asset manager
    // as a specific class
    private void loadAssets(AssetManager assetManager, String directory, Set<String> extensions, Class<?> clazz) {
        assert assetManager != null : "assetManager is null. Can't load any assets without an asset manager, dummy :p";
        assert directory != null : "directory is null. Can't load any assets without a directory to load them from.";
        assert clazz != null : "clazz is null. Need a class to load the asset as!!!";

        Gdx.app.log(logTag, "Loading " + clazz.getSimpleName() + "s in directory <" + directory + ">");

        /*
        This is really not fucking ideal. So classpath directories simply cannot be listed by libgdx. And
        internal files when packed to a JAR (such as when you build with desktop:dist) end up being classpath files!
        So instead we have to do some butt fuckery to dynamically find out what assets are available to load
        by getting the source jar and unzipping it. Good job this is only done once! I would seriously
        consider some other approach in some more demanding game for which assets need to be hotswapped between
        screens
         */

        // TODO: clean this mess up
        boolean classpathFile = false;
        try {
            CodeSource src = SCNGame.class.getProtectionDomain().getCodeSource();
            if(src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                    String name = entry.getName();
                    if(name.startsWith(directory)) {
                        FileHandle file = Gdx.files.internal(name);

                        if(file.extension() == "") continue;

                        if(extensions != null && !extensions.contains(file.extension())) continue;

                        Gdx.app.log(logTag, "Loading asset: " + file.path() + ", " + file.extension());

                        assetManager.load(file.path(), clazz);

                        classpathFile = true;
                    }
                }
            }
        } catch(Exception e) {

        }

        if(classpathFile) {
            return;
        }

        Array<FileHandle> files = new Array<>(Gdx.files.internal(directory).list());
        Gdx.app.log(logTag, "" + files.size + " files to load");
        while(files.notEmpty()) {
            FileHandle file = files.pop();
            if(file.isDirectory()) {
                files.addAll(file.list());
                continue;
            }

            // leaving extensions null will simply not check for extensions
            if(extensions != null && !extensions.contains(file.extension())) {
                Gdx.app.log(logTag, "Ignored asset: " + file.path());
                continue;
            }

            Gdx.app.log(logTag, "Loading asset: " + file.path());

            assetManager.load(file.path(), clazz);
        }
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
