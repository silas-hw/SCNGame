package com.mygdx.scngame.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.event.DialogEventListener;
import com.mygdx.scngame.event.GlobalEventBus;
import com.mygdx.scngame.settings.Controls;
import com.mygdx.scngame.settings.PrefSettings;
import com.mygdx.scngame.settings.Settings;
import com.mygdx.scngame.ui.TiledNinePatch;

// TODO: encapsulate dialog box UI into its own class

/**
 * Encapsulates the handling of dialog events, including capturing events, drawing dialog boxes, and
 * firing dialog end events.
 */
public class Dialog extends InputAdapter implements DialogEventListener {

    private boolean inFocus = false;

    private String message = "";

    private Viewport view;

    private static Skin skin = SCNGame.getAssetManager().get("skin/uiskin.json");

    private Label label;

    private Stage stage;

    private Table root;

    private Container<Table> container;
    private Container<Label> wrapper;
    private Table inside;

    private Image icon;

    private float WIDTH = 600f;
    private float HEIGHT = 150f;

    TiledNinePatch npatch;

    private float basePatchScale = 3f;

    private Settings settings;


    public Dialog(Settings settings) {
        this.settings = settings;
        stage = new Stage(new ScreenViewport());

        root = new Table();

        label = new Label("", skin);

        root.setFillParent(true);

        root.pad(20f);

        stage.addActor(root);

        wrapper = new Container<>(label);
        wrapper.center();
        wrapper.fill();

        Texture patchTexture = SCNGame.getAssetManager().get("sprites/patch.9.png", Texture.class);

        icon = new Image(patchTexture);
        icon.setAlign(Align.center);
        icon.setScaling(Scaling.fit);

        float scale = settings.getDialogScale();

        inside = new Table();
        inside.add(wrapper).grow().colspan(2);
        inside.add(icon).grow().colspan(1);

        container = new Container<>(inside);

        container.width(WIDTH*scale);
        container.height(HEIGHT*scale);
        container.fill();

        npatch = TiledNinePatch.getInstanceFromDot9(patchTexture);
        npatch.scale = basePatchScale;

        container.setBackground(npatch, true);

        label.setFontScale(scale);
        label.setWrap(true);
        label.setAlignment(Align.top | Align.left);

        root.add(container);

        root.bottom();

        stage.setDebugAll(true);

    }

    public void draw() {
        if(!inFocus) {
            return;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            WIDTH += 10;
        }

        float scale = settings.getDialogScale();

        /*
         *  Yeah, I know I *should* be doing root.getCell()... but that resizes the container and not the wrapper or
         * label. Even if you set the wrapper and label to fill parent they bloody dont! Or atleast not properly.
         * They either don't resize at all or resize and drift away from the position of the container.
         *
         * Not pleasant, but just leave it as is. This is really a problem with how much of a god shite mess
         * scene2d.ui is (probably, or I'm just a dumbass. Either could be true).
         */

        container.width(WIDTH * scale);
        container.height(HEIGHT * scale);

        label.setFontScale(scale);
        inside.getCell(icon).pad(5 * scale);
        wrapper.pad(5 * scale);

        stage.draw();
    }

    private float easeOutExpo(float x) {
        return x >= 1 ? x : 1 - (float) Math.pow(2, -10 * x);
    }

    public Viewport getViewport() {return this.stage.getViewport();}

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void onDialogStart(String id) {
        inFocus = true;

        switch(id) {
            case "test_dialog_1":
                message = "";

                message += "Well, the controls should be: ";
                for(Controls c : Controls.values()) {
                    message += c.toString() + ": ";
                    message += Input.Keys.toString(c.getKeycode()) + ", ";
                }
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
    public void onDialogEnd() {
        inFocus = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(!inFocus) {
            return false;
        }

        switch (keycode) {
            case Input.Keys.E:
                inFocus = false;
                GlobalEventBus.getInstance().endDialog();
                break;

        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return inFocus;
    }
}
