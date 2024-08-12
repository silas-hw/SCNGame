package com.mygdx.scngame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.settings.PrefSettings;
import com.mygdx.scngame.settings.Settings;

public class MainMenuScreen implements Screen {

    private Skin skin;
    private Label label;
    private Stage stage;

    Viewport viewport;
    OrthographicCamera camera;

    Game game;
    SpriteBatch batch;
    ShapeRenderer shape;

    Music bg;

    Settings settings;


    public MainMenuScreen(Game game, SpriteBatch batch, ShapeRenderer shape, Settings settings) {
        this.game = game;

        this.batch = batch;
        this.shape = shape;

        camera = new OrthographicCamera();
        viewport = new ScreenViewport();

        bg = SCNGame.getAssetManager().get("music/bgtest1.mp3", Music.class);

        this.settings = settings;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        label = new Label(" ", skin);
        label.setText("Press E to Start");

        Container<Label> container = new Container<>(label);
        container.center();

        stage = new Stage();
        stage.setViewport(viewport);

        stage.addActor(container);

        bg.setVolume(settings.getMusicVolume());
        bg.setLooping(true);
        bg.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setScreen(new GameScreen(game, batch, shape, settings));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        skin.dispose();
        stage.dispose();
        bg.stop();
    }

    @Override
    public void dispose() {

    }
}
