package com.mygdx.scngame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.dialog.Dialog;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.entity.sprite.SpriteEntity;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.settings.Settings;

import java.util.Iterator;

public class GameScreen implements Screen {

    Game game;
    Scene scene;

    OrthographicCamera camera;
    Viewport gameViewport;

    SpriteBatch batch;
    ShapeRenderer shape;

    World<Box> world;

    Player player;

    Dialog dialog;

    TiledMapRenderer mapRenderer;
    TiledMap tiledMap;

    public GameScreen(Game game, SpriteBatch batch, ShapeRenderer shape, Player player) {
        this.game = game;
        this.batch = batch;
        this.shape = shape;

        this.player = player;

        camera = new OrthographicCamera();
        gameViewport = new ExtendViewport(400, 400, camera);

        world = new World<Box>();
        scene = new Scene(gameViewport, batch, shape, world);

        dialog = new Dialog();
    }

    public GameScreen(Game game, SpriteBatch batch, ShapeRenderer shape) {
        this.game = game;
        this.batch = batch;
        this.shape = shape;

        camera = new OrthographicCamera();
        gameViewport = new ExtendViewport(400, 400, camera);

        world = new World<Box>();

        this.player = new Player();

        dialog = new Dialog();
    }

    /*
    Load in any heavy resources here so they can be disposed of when hide is called.

    Also parse the world and add entities to the scene and physics objects to the world
     */

    // TODO: create documentation about what different map objects are available and what they do

    @Override
    public void show() {
        scene = new Scene(gameViewport, batch, shape, world);

        tiledMap = new TmxMapLoader().load("tilemaps/testmap1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f, this.batch);

        // extract objects and parse them
        for(MapLayer layer : tiledMap.getLayers()) {
            if(layer instanceof TiledMapTileLayer) parseTileLayer((TiledMapTileLayer) layer);
            else parseObjectLayer(layer);
        }

        Box wall = new Box();
        wall.solid = true;
        wall.layer = 0b10000000;

        world.add(new Item<>(wall), 150, 150, 150, 150);

        DamageBox damage = new DamageBox(5f, DamageBox.DamageType.DEFAULT);
        damage.layer = 0b10000000;

        world.add(new Item<>(damage), -150, -150, 150, 150);

        scene.addEntity(player);

        scene.setWorld(world);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(dialog);
        multiplexer.addProcessor(scene);

        Gdx.input.setInputProcessor(multiplexer);

        Global.bus.addEventListener(dialog);
    }

    private void parseTileLayer(TiledMapTileLayer layer) {
        int width = layer.getTileWidth();
        int height = layer.getTileHeight();

        for(int x = 0; x<layer.getWidth(); x++) {
            for(int y = 0 ; y<layer.getHeight(); y++) {

                // the x and y of objects within a tile layer or held by a tile are relative to that tile
                parseTile(layer.getCell(x, y).getTile(), x * width, y * height);
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
            newWall.layer = Integer.parseInt(properties.get("CollisionLayer", String.class), 2);

            world.add(new Item<>(newWall), x + offsetX, y + offsetY, width, height);
        } else if(type.equals("DamageWall")) {
            String dtype = properties.get("DamageType", String.class);
            float damage = properties.get("damage", float.class);
            DamageBox newDamage = new DamageBox(damage, DamageBox.DamageType.valueOf(dtype));

            newDamage.layer = Integer.parseInt(properties.get("CollisionLayer", String.class), 2);
            newDamage.solid = false;

            world.add(new Item<>(newDamage), x + offsetX, y + offsetY, width, height);
        } else if(obj instanceof TiledMapTileMapObject && type.equals("")) {
            TextureRegion texture = ((TextureMapObject) obj).getTextureRegion();
            scene.addEntity(new SpriteEntity(texture, x, y));

            // parse the tile held by the tiled map tile object - getting any child objects of that tile
            parseTile(((TiledMapTileMapObject) obj).getTile(), x,  y);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());


        ScreenUtils.clear(Color.BLACK);

        scene.update(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));

        camera.update();
        mapRenderer.setView(camera);

        mapRenderer.render();
        scene.draw();
        dialog.draw();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_BLEND);

        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        for(Item item : world.getItems()) {
            Rect rec = world.getRect(item);

            if(item.userData instanceof Entity) {
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
            game.setScreen(new MainMenuScreen(game, batch, shape));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            Settings.setDialogScale(Settings.getDialogScale() + 0.1f);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            Settings.setDialogScale(Settings.getDialogScale() - 0.1f);
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

        Global.bus.removeEventListener(dialog);

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }
}
