package com.mygdx.scngame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.save.SaveFile;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.ui.TruetypeLabel;

public class GameOverScreen implements Screen {


    ScreenData screenData;
    Player player;
    TiledMapRenderer mapRenderer;
    Scene scene;

    Viewport gameViewport;
    Viewport screenViewport;

    SaveFile saveFile;

    Stage stage;

    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    Game game;

    public GameOverScreen(ScreenData screenData, Player player,
                          TiledMapRenderer mapRenderer, Viewport gameViewport,
                          SaveFile save, Game game, Scene gameScene) {

        this.game = game;
        this.screenData = screenData;
        this.player = player;
        this.mapRenderer = mapRenderer;
        this.gameViewport = gameViewport;
        this.saveFile = save;

        this.scene = gameScene;

        this.batch = screenData.batch();
        this.shapeRenderer = screenData.shapeRenderer();

        screenViewport = new ScreenViewport();

        stage = new Stage(screenViewport, screenData.batch());

        FreeTypeFontGenerator font = screenData.assets().get("skin/MyFont2.ttf", FreeTypeFontGenerator.class);

        TruetypeLabel label = new TruetypeLabel(font, 20);

        label.setFontScale(screenData.settings().getUIScale());

        label.setText("GAME OVER\n Press E to return to previous save (temp key)");
        label.setAlignment(Align.center);

        Table root = new Table();
        root.setFillParent(true);
        root.padTop(50f);

        root.add(label).top().expandX();

        stage.addActor(root);
    }

    @Override
    public void show() {

    }

    float alpha = 0f;

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        gameViewport.getCamera().update();
        gameViewport.apply();

        mapRenderer.setView((OrthographicCamera) gameViewport.getCamera());
        mapRenderer.render();

        scene.draw();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(0, 0, screenViewport.getScreenWidth(), screenViewport.getScreenHeight());

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        this.batch.setProjectionMatrix(gameViewport.getCamera().combined);

        this.batch.begin();
        player.draw(this.batch, this.shapeRenderer, 1f);
        this.batch.end();

        stage.act();
        stage.draw();

        alpha = Math.min(1f, alpha+delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setScreen(new GameScreen(this.screenData, this.saveFile));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        gameViewport.update(width, height);
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
