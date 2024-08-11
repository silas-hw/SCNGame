package com.mygdx.scngame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.scngame.SCNGame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// TODO: load animations and sound files

public class AssetLoadingScreen implements Screen {

    private static final String logTag = "AssetLoadingScreen";

    Game game;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    public AssetLoadingScreen(Game game, SpriteBatch batch, ShapeRenderer shapeRenderer) {
        this.game = game;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
    }

    AssetManager assetManager;


    // this feels bad. Initialising a whole array just to dump it into a hash set? alas it may be the best
    // Perhaps doing this in the constructor would be better so the
    // references to these arrays can be lost and cleaned up by bin man (Java GC)
    String[] _spriteExtensions = {
            "png", "jpg", "jpeg", "gif"
    };

    String[] _tilemapExtensions = {
            "tmx"
    };

    String[] _skinExtensions = {
            "json"
    };

    Set<String> spriteExtensions = new HashSet<>(Arrays.asList(_spriteExtensions));
    Set<String> tilemapExtensions = new HashSet<>(Arrays.asList(_tilemapExtensions));
    Set<String> skinExtensions = new HashSet<>(Arrays.asList(_skinExtensions));

    @Override
    public void show() {
        assetManager = SCNGame.getAssetManager();

        loadAssets(assetManager, "sprites/", spriteExtensions, Texture.class);
        loadAssets(assetManager, "skin/", skinExtensions, Skin.class);

        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        loadAssets(assetManager, "tilemaps/", tilemapExtensions, TiledMap.class);

        Gdx.app.log(logTag, "Assets set to load!");

    }

    // used to load all the assets in a given directory matching a set of extensions into an asset manager
    // as a specific class
    private void loadAssets(AssetManager assetManager, String directory, Set<String> extensions, Class<?> clazz) {
        assert assetManager != null : "assetManager is null. Can't load any assets without an asset manager, dummy :p";
        assert directory != null : "directory is null. Can't load any assets without a directory to load them from.";
        assert clazz != null : "clazz is null. Need a class to load the asset as!!!";

        Gdx.app.log(logTag, "Loading " + clazz.getSimpleName() + "s in directory <" + directory + ">");

        Array<FileHandle> files = new Array<>(Gdx.files.internal(directory).list());
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
            game.setScreen(new MainMenuScreen(game, batch, shapeRenderer));
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
