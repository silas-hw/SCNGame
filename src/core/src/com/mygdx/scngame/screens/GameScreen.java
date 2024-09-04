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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.dialog.DialogView;
import com.mygdx.scngame.entity.component.HealthComponent;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.MapChangeEventBus;
import com.mygdx.scngame.event.MapChangeEventListener;
import com.mygdx.scngame.event.SaveEventBus;
import com.mygdx.scngame.hud.HUD;
import com.mygdx.scngame.map.MapObjectLoader;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.save.SaveFile;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.settings.SettingsMenu;
import com.mygdx.scngame.viewport.PixelFitScaling;

import java.time.Instant;
import java.util.Map;

public class GameScreen implements Screen, MapChangeEventBus, SaveEventBus, HealthComponent.DeathListener {
    Game game;
    Scene scene;

    OrthographicCamera camera;
    ExtendViewport gameViewport;

    World<Box> world;

    Player player;

    DialogView dialogView;
    SettingsMenu settingsMenu;
    HUD hud;

    TiledMapRenderer mapRenderer;
    TiledMap tiledMap;

    ScreenData screenData;

    private int MAP_WIDTH;
    private int MAP_HEIGHT;

    String spawnID;

    ScreenViewport screenViewport;

    SaveFile saveFile;

    public GameScreen(ScreenData screenData, SaveFile save) {
        this.game = screenData.game();

        this.saveFile = save;

        this.screenData = screenData;

        this.player = new Player(screenData.assets());
        player.health.addDeathListener(this);

        camera = new OrthographicCamera();

        gameViewport = new ExtendViewport(200, 200, camera);
        gameViewport.setScaling(new PixelFitScaling());

        world = new World<>();

        scene = new Scene(gameViewport, screenData.batch(), screenData.shapeRenderer(), world);

        dialogView = new DialogView(screenData);
        settingsMenu = new SettingsMenu(screenData, Controls.getInstance());
        hud = new HUD(screenData, player.health.getMaxHealth());
        player.health.addHealthDamageListener(hud);

        this.tiledMap = screenData.assets().get("tilemaps/" + save.map);
        this.spawnID = save.spawnLocation;

        screenViewport = new ScreenViewport();

        bg = screenData.assets().get("music/bgtest2.mp3", Music.class);
        bg.setLooping(true);

        Controls.getInstance().addActionListener(settingsMenu);
        Controls.getInstance().addActionListener(dialogView);
        Controls.getInstance().addActionListener(scene);
    }

    public GameScreen(ScreenData screenData) {
        // internally name the save as the length of the number of saves+1
        this(screenData, new SaveFile("untitled.tmx", "test_spawn",
                "ermm", Instant.now().toEpochMilli(),
                Gdx.files.local("/save/" + (Gdx.files.local("/save/").list().length+1) + ".save")));
    }

    Music bg;

    @Override
    public void show() {

        gameViewport.setCamera(camera);

        world.reset();

        scene.clearEntities();
        scene.setWorld(world);
        scene.setViewport(gameViewport);

        dialogView.clearDialogListeners();
        dialogView.addDialogListener(scene);

        this.mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f, screenData.batch());

        String bgPath = tiledMap.getProperties().get("bg", String.class);
        Music newBg = screenData.assets().get("music/" + bgPath, Music.class);

        if(newBg != bg && newBg != null) {
            bg.stop();
            bg = newBg;

            bg.setLooping(true);
        }

        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);
        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);

        MapObjectLoader mapObjects = new MapObjectLoader(tiledMap, this.world, this.scene,
                                                         screenData.assets(), this.dialogView, this, this);

        Map<String, Vector2> spawnLocations = mapObjects.getSpawnLocations();

        Vector2 spawnLocation = spawnLocations.get(spawnID);

        if(spawnLocation != null) {
            player.position.x = spawnLocation.x;
            player.position.y = spawnLocation.y;
        } else {
            player.position.x = 0;
            player.position.y = 0;
        }

        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float worldWidth = gameViewport.getWorldWidth();
        float worldHeight = gameViewport.getWorldHeight();

        float widthLimit = Math.max(MAP_WIDTH - worldWidth/2, worldWidth/2);
        float heightLimit = Math.max(MAP_HEIGHT - worldHeight/2, worldHeight/2);

        camera.position.x = MathUtils.clamp(player.position.x + player.WIDTH/2f,
                worldWidth/2 + 32, widthLimit - 32);
        camera.position.y = MathUtils.clamp(player.position.y + player.HEIGHT/2f,
                worldHeight/2 + 32, heightLimit - 32);

        player.resetState();
        scene.addEntity(player);
        scene.setWorld(world);
    }

    float transitionAlpha = 1f;
    boolean fadeIn = true;

    float waitTime = 0.5f;

    TiledMap nextMap = null;
    String nextSpawn = "";


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bg.setVolume(screenData.settings().getTrueMusicVolume());

        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            game.setScreen(new MainMenuScreen(this.screenData));
        }

        // give some waiting time before doing anything.
        if(waitTime > 0f) {
            waitTime -= delta;

            // if waiting has finished turn the tunes up!!!
            if(waitTime <= 0f) {
                bg.play();
            }
            return;
        }

        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());

        if(fadeIn) scene.update(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));

        float worldWidth = gameViewport.getWorldWidth();
        float worldHeight = gameViewport.getWorldHeight();

        float widthLimit = Math.max(MAP_WIDTH - worldWidth/2, worldWidth/2);
        float heightLimit = Math.max(MAP_HEIGHT - worldHeight/2, worldHeight/2);

        float targetX = MathUtils.clamp(player.position.x + player.WIDTH/2f,
                worldWidth/2 + 32, widthLimit - 32);
        float targetY = MathUtils.clamp(player.position.y + player.HEIGHT/2f,
                worldHeight/2 + 32, heightLimit - 32);

        camera.position.x = MathUtils.lerp(camera.position.x, targetX, 4f * delta);
        camera.position.y = MathUtils.lerp(camera.position.y, targetY, 4f * delta);

        camera.update();

        gameViewport.apply();

        scene.drawWaterReflection();

        gameViewport.apply();
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        scene.draw();

        hud.draw();
        dialogView.draw(delta);
        settingsMenu.draw();

        ShapeRenderer shape = screenData.shapeRenderer();

        if(transitionAlpha > 0f && fadeIn) {
            transitionAlpha -= delta * 2;
        } else if(transitionAlpha < 1f && !fadeIn) {
            transitionAlpha += delta * 2;
        }

        // if a next map has been set and we've finished fading out, switch map
        if(nextMap != null && transitionAlpha >=  1f && !fadeIn) {
            this.tiledMap = nextMap;
            this.spawnID = nextSpawn;

            this.fadeIn = true;
            this.waitTime = 0.5f;

            this.nextMap = null;
            this.nextSpawn = "";

            // show loads the map and map renderer again
            // resetting and then repopulating the world and scene
            this.show();
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

        drawWorld(ShapeRenderer.ShapeType.Filled, shape, 0.6f);
        drawWorld(ShapeRenderer.ShapeType.Line, shape, 1f);

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawWorld(ShapeRenderer.ShapeType shapeType, ShapeRenderer shape, float alpha) {
        shape.setProjectionMatrix(camera.combined);
        shape.begin(shapeType);

        for(Item item : world.getItems()) {
            Rect rec = world.getRect(item);
            Color col = Color.WHITE;

            if(item.userData instanceof Box) col = ((Box) item.userData).getDebugColor();
            shape.setColor(col.r, col.g, col.b, alpha);
            shape.rect(rec.x, rec.y, rec.w, rec.h);
        }

        shape.end();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        screenViewport.update(width, height, true);
        dialogView.resize(width, height);
        settingsMenu.resize(width, height);
        scene.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        bg.stop();

        Controls.getInstance().removeActionListener(dialogView);
        Controls.getInstance().removeActionListener(scene);
    }

    @Override
    public void dispose() {}

    private Array<MapChangeEventListener> listeners = new Array<>();

    @Override
    public void addMapChangeListener(MapChangeEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeMapChangeListener(MapChangeEventListener listener) {
        listeners.removeValue(listener, true);
    }

    @Override
    public void clearMapChangeListener() {
        listeners.clear();
    }

    @Override
    public void changeMap(TiledMap map, String spawnID) {
        if(map == tiledMap) return;

        Gdx.app.log("GameScreen", "Changing map to: " + map + " with spawnID: " + spawnID);

        nextMap = map;
        nextSpawn = spawnID;
        fadeIn = false;

        for(MapChangeEventListener listener : listeners) {
            listener.onMapChange(map, spawnID);
        }
    }

    @Override
    public void save(String mapPath, String spawnLocation, String displayName) {
        saveFile.spawnLocation = spawnLocation;
        saveFile.map = mapPath;
        saveFile.saveDateEpoch = Instant.now().getEpochSecond();
        saveFile.displayName = displayName;

        saveFile.writeToXML();
    }

    @Override
    public void onDeath() {
        game.setScreen(new GameOverScreen(screenData, player, mapRenderer, gameViewport, saveFile, game, scene));
    }
}