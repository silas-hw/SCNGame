package com.mygdx.scngame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.scngame.SCNGame;

public class MainMenuScreen implements Screen {

    private Skin skin;
    private Label label;
    private Stage stage;

    SCNGame game;

    public MainMenuScreen(SCNGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        label = new Label(" ", skin);
        label.setText("Press E to Start");

        Container<Label> container = new Container<>(label);
        container.center();

        stage = new Stage();
        stage.setViewport(game.viewport);

        stage.addActor(container);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setScreen(new GameScreen(game));
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
        skin.dispose();
        stage.dispose();
    }

    @Override
    public void dispose() {

    }
}
