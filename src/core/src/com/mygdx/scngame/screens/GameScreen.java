package com.mygdx.scngame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.dialog.Dialog;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.entity.sprite.AnimatedSpriteEntity;
import com.mygdx.scngame.entity.sprite.SpriteEntity;
import com.mygdx.scngame.event.GlobalEventBus;
import com.mygdx.scngame.path.PathNodes;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;
import com.mygdx.scngame.physics.InteractBox;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.screens.data.ScreenData;

import java.util.Iterator;

public class GameScreen implements Screen {

    Game game;
    Scene scene;

    OrthographicCamera camera;
    Viewport gameViewport;

    World<Box> world;

    Player player;

    Dialog dialog;

    TiledMapRenderer mapRenderer;
    TiledMap tiledMap;

    PathNodes pathNodes;

    ScreenData screenData;

    private int MAP_WIDTH;
    private int MAP_HEIGHT;

    // want to avoid large constructors
    // maybe wrap arguments into a datastructure?

    // ScreenData: game, batch, shape, settings
    // GameScreenData: player, map id, spawn id
    public GameScreen(ScreenData screenData, Player player) {
        this.game = screenData.game();

        this.screenData = screenData;

        this.pathNodes = new PathNodes();

        this.player = player;

        camera = new OrthographicCamera();
        gameViewport = new ExtendViewport(200, 200, camera);

        world = new World<Box>();

        dialog = new Dialog(screenData);

    }

    public GameScreen(ScreenData screenData) {
        this(screenData, new Player(screenData.assets()));
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

        tiledMap = screenData.assets().get("tilemaps/untitled.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f, screenData.batch());

        MAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);
        MAP_WIDTH = tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);

        // extract objects and parse them
        for(MapLayer layer : tiledMap.getLayers()) {
            if(layer instanceof TiledMapTileLayer) parseTileLayer((TiledMapTileLayer) layer);
            else parseObjectLayer(layer);
        }

        scene.addEntity(player);

        scene.setWorld(world);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(dialog);
        multiplexer.addProcessor(scene);

        Gdx.input.setInputProcessor(multiplexer);

        GlobalEventBus.getInstance().addDialogListener(dialog);
    }

    private void parseTileLayer(TiledMapTileLayer layer) {
        int width = layer.getTileWidth();
        int height = layer.getTileHeight();

        for(int x = 0; x<layer.getWidth(); x++) {
            for(int y = 0 ; y<layer.getHeight(); y++) {

                // the x and y of objects within a tile layer or held by a tile are relative to that tile
                // also, if a tile is empty then the cell is null!
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if(cell != null) parseTile(cell.getTile(), x * width, y * height);
            }
        }
    }

    /**
     *
     * @param tile
     * @param x the x position of the tile in pixels
     * @param y the y position of the tile in pixels
     */
    public void parseTile(TiledMapTile tile, float x, float y) {
        for(MapObject obj : tile.getObjects()) {
            // offset the object by the tiles position
            // this is done because the objects x and y position are stored relative to the tile
            // but we want to add it to the world at its absolute position

            parseMapObject(obj, x, y);
        }
    }

    private void parseObjectLayer(MapLayer layer) {
        for(MapObject obj : layer.getObjects()){

            // x and y properties in object layers are absolute
            parseMapObject(obj, 0, 0);
        }
    }

    private void parseMapObject(MapObject obj, float offsetX, float offsetY) {
        MapProperties properties = obj.getProperties();

        float x = properties.get("x", float.class);
        float y = properties.get("y", float.class);
        float width = properties.get("width", float.class);
        float height = properties.get("height", float.class);

        String type = obj.getProperties().get("type", String.class);
        if(type == null) type = "";

        if(type.equals("Wall")) {
            Box newWall = new Box();
            newWall.solid = true;

            MapProperties collisionLayer = obj.getProperties().get("CollisionLayer", MapProperties.class);
            setBoxCollisionLayers(collisionLayer, newWall);


            world.add(new Item<>(newWall), x + offsetX, y + offsetY, width, height);
        } else if(type.equals("DamageWall")) {
            String dtype = properties.get("DamageType", String.class);
            float damage = properties.get("damage", float.class);
            DamageBox newDamage = new DamageBox(damage, DamageBox.DamageType.valueOf(dtype));

            newDamage.solid = false;

            MapProperties collisionLayer = obj.getProperties().get("CollisionLayer", MapProperties.class);
            setBoxCollisionLayers(collisionLayer, newDamage);

            world.add(new Item<>(newDamage), x + offsetX, y + offsetY, width, height);
        } else if(type.equals("Sign")) {
            String dialogID = properties.get("DialogID", String.class);

            InteractBox box = new InteractBox() {
                @Override
                public void interact() {
                    GlobalEventBus.getInstance().startDialog(dialogID);
                }
            };

            world.add(new Item<>(box), x + offsetX, y + offsetY, width, height);
        } else if(type.equals("PathNode")) {
            pathNodes.put(obj);
        } else if(obj instanceof TiledMapTileMapObject && type.equals("")) {
            TiledMapTile tile = ((TiledMapTileMapObject) obj).getTile();

            if(tile instanceof AnimatedTiledMapTile) {
                float interval = (float) ((AnimatedTiledMapTile) tile).getAnimationIntervals()[0]/1000f;
                scene.addEntity(new AnimatedSpriteEntity(((AnimatedTiledMapTile) tile).getFrameTiles(), interval, x, y));

            } else {
                TextureRegion texture = ((TextureMapObject) obj).getTextureRegion();
                scene.addEntity(new SpriteEntity(texture, x, y));
            }


            // parse the tile held by the tiled map tile object - getting any child objects of that tile
            parseTile(tile, x,  y);
        }
    }

    /**
     * Sets the collision layer of a box based on a CollisionLayer map property
     */
    private void setBoxCollisionLayers(MapProperties collisionLayer, Box box) {
        for (Iterator<String> it = collisionLayer.getKeys(); it.hasNext(); ) {
            String key = it.next();

            if("type".equals(key)) continue;

            boolean set = collisionLayer.get(key, Boolean.class);
            box.setLayer(Integer.parseInt(key, 10), set);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());

        bg.setVolume(screenData.settings().getMusicVolume());


        ScreenUtils.clear(Color.BLACK);

        scene.update(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));

        float worldWidth = gameViewport.getWorldWidth();
        float worldHeight = gameViewport.getWorldHeight();

        float widthLimit = Math.max(MAP_WIDTH - worldWidth/2, worldWidth/2);
        float heightLimit = Math.max(MAP_HEIGHT - worldHeight/2, worldHeight/2);

        camera.position.x = MathUtils.clamp(player.position.x + player.WIDTH/2,
                worldWidth/2, widthLimit);
        camera.position.y = MathUtils.clamp(player.position.y + player.HEIGHT/2,
                worldHeight/2, heightLimit);

        camera.update();
        mapRenderer.setView(camera);

        mapRenderer.render();
        scene.draw();
        dialog.draw();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_BLEND);

        ShapeRenderer shape = screenData.shapeRenderer();
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
            game.setScreen(new MainMenuScreen(screenData));
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
        scene.dispose();

        tiledMap.dispose();
        mapRenderer = null;

        bg.stop();



        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }
}
