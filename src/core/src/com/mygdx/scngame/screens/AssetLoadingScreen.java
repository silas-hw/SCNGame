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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.map.MyTmxMapLoader;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.settings.Settings;
import com.ray3k.stripe.FreeTypeSkinLoader;

import java.net.URL;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
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

    ScreenViewport viewport;

    public AssetLoadingScreen(ScreenData args) {
        this.game = args.game();
        this.batch = args.batch();
        this.shapeRenderer = args.shapeRenderer();

        this.settings = args.settings();
        this.screenData = args;

        this.viewport = new ScreenViewport();

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
        InternalFileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(TiledMap.class, new MyTmxMapLoader(resolver));
        assetManager.setLoader(Skin.class, new FreeTypeSkinLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));

        for(String filePath : assetMap) {
            String extension = Gdx.files.internal(filePath).extension();

            if (filePath.startsWith("sprites/") && spriteExtensions.contains(extension)) {
                assetManager.load(filePath, Texture.class);
                Gdx.app.log(logTag, filePath + " set to load as Sprite");
            } else if (filePath.startsWith("tilemaps/") && tilemapExtensions.contains(extension)) {
                assetManager.load(filePath, TiledMap.class);
                Gdx.app.log(logTag, filePath + " set to load as TiledMap (tmx)");
            } else if (filePath.startsWith("skin/") && skinExtensions.contains(extension)) {
                assetManager.load(filePath, Skin.class);
                Gdx.app.log(logTag, filePath + " set to load as Skin");
            } else if (filePath.startsWith("music/") && audioExtensions.contains(extension)) {
                assetManager.load(filePath, Music.class);
                Gdx.app.log(logTag, filePath + " set to load as Music");
            } else if (filePath.startsWith("sfx/") && audioExtensions.contains(extension)) {
                assetManager.load(filePath, Sound.class);
                Gdx.app.log(logTag, filePath + " set to load as Sound");
            } else if (filePath.startsWith("animations/") && extension.equals("atlas")) {
                assetManager.load(filePath, TextureAtlas.class);
                Gdx.app.log(logTag, filePath + "set to load as TextureAtlas");
            } else if (extension.equals("ttf")) {
                assetManager.load(filePath, FreeTypeFontGenerator.class);
                Gdx.app.log(logTag, filePath + " set to load as FreeTypeFontGenerator");
            } else {
                Gdx.app.log(logTag, filePath + " IGNORED");
                Gdx.app.debug(logTag, "Warning: file may have been placed in incorrect directory");
            }
        }

        Gdx.app.log(logTag, "All assets set to load!");
    }

    // TODO: inform user of asset loading graphically

    private float progressBarWidthProportion = 0.7f;
    private float progressBarHeightProportion = 0.025f;

    private float progress = 0f;

    @Override
    public void render(float delta) {
        Gdx.app.log(logTag, "Loading assets... " + assetManager.getProgress()*100 + "%");

        viewport.getCamera().update();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        float width = progressBarWidthProportion * viewport.getScreenWidth();
        float height = progressBarHeightProportion * viewport.getScreenHeight();

        float x = (viewport.getScreenWidth() - width)/2f;
        float y = (viewport.getScreenHeight() - height)/2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.LIGHT_GRAY);

        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.setColor(Color.RED);

        progress = MathUtils.lerp(progress, assetManager.getProgress(), 0.1f);

        shapeRenderer.rect(x, y, width*progress, height);

        shapeRenderer.end();

        if(assetManager.isFinished()) {
            game.setScreen(new MainMenuScreen(screenData));
            return;
        }

        assetManager.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
