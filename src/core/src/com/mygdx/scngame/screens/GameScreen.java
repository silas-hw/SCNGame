package com.mygdx.scngame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.dialog.Dialog;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.GlobalEventBus;
import com.mygdx.scngame.event.MapChangeEventListener;
import com.mygdx.scngame.map.MapObjectLoader;
import com.mygdx.scngame.path.PathNodes;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;
import com.mygdx.scngame.physics.InteractBox;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.viewport.PixelFitScaling;

import java.util.Map;

public class GameScreen implements Screen, MapChangeEventListener {

    Game game;
    Scene scene;

    OrthographicCamera camera;
    ExtendViewport gameViewport;

    World<Box> world;

    Player player;

    Dialog dialog;

    TiledMapRenderer mapRenderer;
    TiledMap tiledMap;

    PathNodes pathNodes;

    ScreenData screenData;

    private int MAP_WIDTH;
    private int MAP_HEIGHT;

    final String mapPath;
    final String spawnID;

    ScreenViewport screenViewport;

    // want to avoid large constructors
    // maybe wrap arguments into a datastructure?

    // ScreenData: game, batch, shape, settings
    // GameScreenData: player, map id, spawn id
    public GameScreen(ScreenData screenData, Player player, String mapPath, String spawnID) {
        this.game = screenData.game();

        this.screenData = screenData;

        this.pathNodes = new PathNodes();

        this.player = player;

        camera = new OrthographicCamera();
        gameViewport = new ExtendViewport(200, 200, camera);
        gameViewport.setScaling(new PixelFitScaling());

        world = new World<Box>();

        dialog = new Dialog(screenData);

        this.mapPath = mapPath;
        this.spawnID = spawnID;

        screenViewport = new ScreenViewport();

    }

    public GameScreen(ScreenData screenData) {
        this(screenData, new Player(screenData.assets()),  "untitled.tmx", "test_spawn");
    }

    /*
    Load in any heavy resources here so they can be disposed of when hide is called.

    Also parse the world and add entities to the scene and physics objects to the world
     */

    // TODO: update docs to describe PathNode map object

    Music bg;

    @Override
    public void show() {
        bg = screenData.assets().get("music/bgtest2.mp3", Music.class);
        bg.setLooping(true);
        bg.play();

        scene = new Scene(gameViewport, screenData.batch(), screenData.shapeRenderer(), world);

        Gdx.app.log("GameScreen", "setting map to tilemaps/" + mapPath);
        tiledMap = screenData.assets().get("tilemaps/" + mapPath);
        this.mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f, screenData.batch());

        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);
        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);

        MapObjectLoader mapObjects = new MapObjectLoader(tiledMap, this.world, this.scene, this.pathNodes);

        Map<String, Vector2> spawnLocations = mapObjects.getSpawnLocations();

        Vector2 spawnLocation = spawnLocations.get(spawnID);

        if(spawnLocation != null) {
            player.position.x = spawnLocation.x;
            player.position.y = spawnLocation.y;
        } else {
            player.position.x = 0;
            player.position.y = 0;
        }


        scene.addEntity(player);

        scene.setWorld(world);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(dialog);
        multiplexer.addProcessor(scene);

        Gdx.input.setInputProcessor(multiplexer);

        GlobalEventBus.getInstance().addDialogListener(dialog);
        GlobalEventBus.getInstance().addMapChangeListener(this);
    }

    float transitionAlpha = 1f;
    boolean fadeIn = true;

    float waitTime = 0.3f;

    String nextMap = "";
    String nextSpawn = "";


    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bg.setVolume(screenData.settings().getMusicVolume());

        // give some waiting time before doing anything.
        if(waitTime > 0f) {
            waitTime -= delta;
            return;
        }

        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());

        if(fadeIn) scene.update(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));


        float worldWidth = gameViewport.getWorldWidth();
        float worldHeight = gameViewport.getWorldHeight();

        float widthLimit = Math.max(MAP_WIDTH - worldWidth/2, worldWidth/2);
        float heightLimit = Math.max(MAP_HEIGHT - worldHeight/2, worldHeight/2);

        camera.position.x = MathUtils.clamp(player.position.x + player.WIDTH/2f,
                worldWidth/2, widthLimit);
        camera.position.y = MathUtils.clamp(player.position.y + player.HEIGHT/2f,
                worldHeight/2, heightLimit);

        camera.update();

        gameViewport.apply();

        mapRenderer.setView(camera);

        mapRenderer.render();
        scene.draw();
        dialog.draw();

        ShapeRenderer shape = screenData.shapeRenderer();

        if(transitionAlpha > 0f && fadeIn) {
            transitionAlpha -= delta * 2;
        } else if(transitionAlpha < 1f && !fadeIn) {
            transitionAlpha += delta * 2;
        }

        // if a next map has been set and we've finished fading out, switch screen
        if(!nextMap.isEmpty() && transitionAlpha >=  1f && !fadeIn) {
            game.setScreen(new GameScreen(screenData, player, nextMap, nextSpawn));
        }

        screenViewport.apply();
        shape.setProjectionMatrix(screenViewport.getCamera().combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_BLEND);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, transitionAlpha);
        shape.rect(0, 0, screenViewport.getScreenWidth(), screenViewport.getScreenHeight());

        shape.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);


        if(!Boolean.getBoolean("debugRender")) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_BLEND);

        gameViewport.apply();

        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        for(Item item : world.getItems()) {
            Rect rec = world.getRect(item);

            if(item.userData instanceof InteractBox) {
                shape.setColor(255/256f, 140/256f, 0, 0.4f);
            } else if(item.userData instanceof HitBox) {
                shape.setColor(36/256f, 122/256f, 227/256f, 0.4f);
            } else if(item.userData instanceof DamageBox) {
                shape.setColor(227/256f, 36/256f, 36/256f, 0.4f);
            } else {
                shape.setColor(1, 1, 1, 0.4f);
            }

            shape.rect(rec.x, rec.y, rec.w, rec.h);
        }

        shape.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            GlobalEventBus.getInstance().changeMap("tilemaps/untitled2.tmx", "test_spawn");
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            screenData.settings().setDialogScale(screenData.settings().getDialogScale() + 0.1f);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            screenData.settings().setDialogScale(screenData.settings().getDialogScale() - 0.1f);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            screenData.settings().setMusicVolume(0f);
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        screenViewport.update(width, height, true);
        dialog.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // remove the player, so it doesn't get disposed of
        // when disposing of the scene

        scene.removeEntity(player);

        // note to self: hide can be called in the middle of a render call (in fact, it almost always will)
        // so disposing of things being currently rendered is a *bad* idea

        // with everything using an asset manager now, it shouldn't be that bad however
        scene.dispose();

        bg.stop();

        GlobalEventBus.getInstance().removeDialogListener(dialog);
        GlobalEventBus.getInstance().removeMapChangeListener(this);

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onMapChange(String mapPath, String spawnID) {
        if(mapPath == this.mapPath) return;

        Gdx.app.log("GameScreen", "Changing map to: " + mapPath + " with spawnID: " + spawnID);

        nextMap = mapPath;
        nextSpawn = spawnID;
        fadeIn = false;
    }
}
