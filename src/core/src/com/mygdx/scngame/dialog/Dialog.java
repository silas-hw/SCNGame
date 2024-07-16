package com.mygdx.scngame.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.scngame.entity.player.PlayerStateChange;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.GameEventListener;

import javax.swing.text.View;

public class Dialog extends InputAdapter implements GameEventListener {

    private boolean inFocus = false;

    private String message = "";

    private static SpriteBatch batch = new SpriteBatch();
    private static Viewport view = new ScreenViewport();

    private static Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

    private Label label = new Label(" ", skin);

    private Stage stage;

    public Dialog() {
        stage = new Stage();
        stage.setViewport(this.getViewport());

        Container<Label> container = new Container<>(label);
        container.center();

        stage.addActor(container);
    }

    public void draw() {
        if(inFocus) {
            stage.draw();
        }
    }

    public Viewport getViewport() {return this.view;}

    @Override
    public void notify(GameEvent event) {

        if(event.getPayload() instanceof DialogEnd) {
            inFocus = false;
            return;
        }

        if(! (event.getPayload() instanceof DialogStart)) {
            return;
        }

        inFocus = true;
        DialogStart payload = (DialogStart) event.getPayload();

        switch(payload.id) {
            case "test_dialog_1":
                message = "Test Dialog 1";
                break;

            case "test_dialog_2":
                message = "Test Dialog 2";
                break;

            default:
                message = "Default Message";
                break;
        }

        label.setText(message);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(!inFocus) {
            return false;
        }

        switch (keycode) {
            case Input.Keys.E:
                inFocus = false;
                break;

        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return inFocus;
    }
}
