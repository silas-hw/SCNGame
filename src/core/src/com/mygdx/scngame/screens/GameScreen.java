package com.mygdx.scngame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;
import com.mygdx.scngame.scene.Scene;

public class GameScreen implements Screen {

    SCNGame game;
    Scene scene;

    public GameScreen(SCNGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        scene = new Scene(game.viewport, game.batch, game.shape, game.world);

        game.scene.clearEntities();
        game.world.reset();

        Box wall = new Box();
        wall.solid = true;
        wall.layer = (byte) 0b10000000;

        game.world.add(new Item<>(wall), 150, 150, 150, 150);

        DamageBox damage = new DamageBox(5f, DamageBox.DamageType.DEFAULT);
        damage.layer = (byte) 0b10000000;

        game.world.add(new Item<>(damage), -150, -150, 150, 150);

        scene.addEntity(game.player);

        scene.setWorld(game.world);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game.dialog);
        multiplexer.addProcessor(scene);

        Gdx.input.setInputProcessor(multiplexer);
    }

    private float testScale = 1f;
    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());


        ScreenUtils.clear(Color.BLACK);

        scene.update(Gdx.graphics.getDeltaTime());

        game.cam.update();
        game.batch.setProjectionMatrix(game.cam.combined);

        scene.draw();
        game.dialog.draw();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_BLEND);

        game.shape.setProjectionMatrix(game.cam.combined);
        game.shape.begin(ShapeRenderer.ShapeType.Filled);

        for(Item item : game.world.getItems()) {
            Rect rec = game.world.getRect(item);

            if(item.userData instanceof Entity) {
                game.shape.setColor(255/256f, 140/256f, 0, 0.4f);
            } else if(item.userData instanceof HitBox) {
                game.shape.setColor(36/256f, 122/256f, 227/256f, 0.4f);
            } else if(item.userData instanceof DamageBox) {
                game.shape.setColor(227/256f, 36/256f, 36/256f, 0.4f);
            } else {
                game.shape.setColor(1, 1, 1, 0.4f);
            }

            game.shape.rect(rec.x, rec.y, rec.w, rec.h);
        }

        game.shape.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            game.setScreen(new MainMenuScreen(game));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            testScale += 0.1f;
            game.dialog.setScale(testScale);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            testScale -= 0.1f;
            game.dialog.setScale(testScale);
        }
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
        scene.clearEntities();
        scene.dispose();
        game.world.reset();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }
}
