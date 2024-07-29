package com.mygdx.scngame.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.GameEventListener;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.settings.Settings;
import com.mygdx.scngame.ui.TiledNinePatch;

/**
 * Encapsulates the handling of dialog events, including capturing events, drawing dialog boxes, and
 * firing {@link DialogEnd} events.
 */
public class Dialog extends InputAdapter implements GameEventListener {

    private boolean inFocus = false;

    private String message = "";

    private Viewport view;

    private static Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

    private Label label = new Label(" ", skin);

    private Stage stage;

    private Table root;

    private Container<Container<?>> container;
    private Container<Label> wrapper;

    private float WIDTH = 600f;
    private float HEIGHT = 150f;

    TiledNinePatch npatch;

    private float basePatchScale = 2f;

    public Dialog() {
        stage = new Stage(new ScreenViewport());

        root = new Table();

        root.setFillParent(true);

        root.pad(20f);

        stage.addActor(root);

        wrapper = new Container<>(label);
        wrapper.center();

        wrapper.pad(15f);

        wrapper.fill();

        float scale = Settings.getDialogScale();

        container = new Container<>(wrapper);

        container.width(WIDTH*scale);
        container.height(HEIGHT*scale);

        npatch = new TiledNinePatch(new Texture(Gdx.files.internal("patch.png")), 8, 8, 8, 8);
        npatch.scale = basePatchScale;

        container.setBackground(npatch, true);

        label.setFillParent(true);
        label.setFontScale(scale);

        root.add(container);

        root.bottom();

        root.setDebug(true);
    }

    public void draw() {
        if(!inFocus) {
            return;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            WIDTH += 10;
        }

        float scale = Settings.getDialogScale();

        npatch.scale = basePatchScale * scale;

        container.width(WIDTH * scale);
        container.height(HEIGHT * scale);

        label.setFontScale(scale);

        stage.getCamera().update();
        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
    }

    public Viewport getViewport() {return this.stage.getViewport();}

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

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
                Global.bus.fire(new GameEvent(this, new DialogEnd()) );
                break;

        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return inFocus;
    }
}
