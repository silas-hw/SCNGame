package com.mygdx.scngame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.dialog.Dialog;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.settings.Settings;

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

    @Override
    public void show() {
        scene = new Scene(gameViewport, batch, shape, world);

        Box wall = new Box();
        wall.solid = true;
        wall.layer = (byte) 0b10000000;

        world.add(new Item<>(wall), 150, 150, 150, 150);

        DamageBox damage = new DamageBox(5f, DamageBox.DamageType.DEFAULT);
        damage.layer = (byte) 0b10000000;

        world.add(new Item<>(damage), -150, -150, 150, 150);

        scene.addEntity(player);

        scene.setWorld(world);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(dialog);
        multiplexer.addProcessor(scene);

        Gdx.input.setInputProcessor(multiplexer);

        Global.bus.addEventListener(dialog);
    }

    private float testScale = 1f;
    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());


        ScreenUtils.clear(Color.BLACK);

        scene.update(Gdx.graphics.getDeltaTime());

        camera.update();
        batch.setProjectionMatrix(camera.combined);

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
            testScale += 0.1f;
            Settings.setDialogScale(testScale);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            testScale -= 0.1f;
            Settings.setDialogScale(testScale);
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

        Global.bus.removeEventListener(dialog);

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }
}
