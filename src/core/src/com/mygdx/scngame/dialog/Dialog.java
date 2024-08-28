package com.mygdx.scngame.dialog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.scngame.controls.ActionListener;
import com.mygdx.scngame.event.DialogEventBus;
import com.mygdx.scngame.event.DialogEventListener;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.settings.Settings;
import com.mygdx.scngame.ui.TiledNinePatch;
import com.mygdx.scngame.ui.TruetypeLabel;

// TODO: encapsulate dialog box UI into its own class

/**
 * Encapsulates the handling of dialog events, including capturing events, drawing dialog boxes, and
 * firing dialog end events.
 */
public class Dialog implements DialogEventListener, ActionListener, DialogEventBus {

    private boolean inFocus = false;

    private String message = "";

    private Viewport view;

    private Skin skin;

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

    private final Settings settings;


    public Dialog(ScreenData screenData) {
        this.settings = screenData.settings();
        this.skin = screenData.assets().get("skin/uiskin2.json", Skin.class);

        float scale = settings.getUIScale();

        stage = new Stage(new ScreenViewport());

        root = new Table();

        root.setFillParent(true);

        root.pad(20f);

        stage.addActor(root);

        FreeTypeFontGenerator fontGenerator = screenData.assets().get("skin/MyFont2.ttf", FreeTypeFontGenerator.class);
        label = new TruetypeLabel(fontGenerator, 20);

        label.setFontScale(scale);
        label.setWrap(true);
        label.setAlignment(Align.top | Align.left);

        wrapper = new Container<>(label);
        wrapper.center();
        wrapper.fill();

        Texture patchTexture = screenData.assets().get("sprites/patch.9.png", Texture.class);

        icon = new Image(patchTexture);
        icon.setAlign(Align.center);
        icon.setScaling(Scaling.fit);

        inside = new Table();
        inside.add(wrapper).grow().colspan(2);
        inside.add(icon).grow().colspan(1);

        container = new Container<>(inside);

        container.width(WIDTH*scale);
        container.height(HEIGHT*scale);
        container.fill();

        npatch = TiledNinePatch.getInstanceFromDot9(patchTexture);

        container.setBackground(npatch, false);


        root.add(container);

        root.bottom();

        stage.setDebugAll(true);

    }

    public void draw() {
        if(!inFocus) {
            return;
        }

        float scale = settings.getUIScale();

        /*
         *  Yeah, I know I *should* be doing root.getCell()... but that resizes the container and not the wrapper or
         * label. Even if you set the wrapper and label to fill parent they bloody dont! Or atleast not properly.
         * They either don't resize at all or resize and drift away from the position of the container.
         *
         * Not pleasant, but just leave it as is. This is really a problem with how much of a god shite mess
         * scene2d.ui is (probably, or I'm just a dumbass. Either could be true).
         *
         * Also drawables don't have their scale set by the thing thats drawing them??? So if you want the drawable
         * to be scaled or anything (if the drawable in question even supports that) you have to either set some world
         * height/width on the drawable manually and have that scaled to the required width/height, or set some scale
         * on the drawable manually.
         *
         * It also seems widgets containing a drawable don't re-calculate their padding if the pad sizes provided by
         * their drawable change.
         *
         * Failed refactoring attempts: 2
         */

        container.width(WIDTH * scale);
        container.height(HEIGHT * scale);
        npatch.scale = 3 * scale;
        container.pad(npatch.getTopHeight(), npatch.getLeftWidth(), npatch.getBottomHeight(), npatch.getRightWidth());

        label.setFontScale(scale);

        inside.getCell(icon).pad(5 * scale);

        stage.getViewport().apply();
        stage.act();
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
                for(Controls.Actions c : Controls.Actions.values()) {
                    message += c.toString() + ": ";
                    message += Input.Keys.toString(c.getKeycode()) + " OR " + c.getControllerButton().getDisplayText() + ", ";
                }
                break;

            case "test_dialog_2":
                message = "casper is stinkyyyyyy";
                break;

            default:
                message = "Default Message";
                break;
        }

        label.setText(message);
    }

    @Override
    public void onDialogEnd() {}


    @Override
    public boolean actionDown(Controls.Actions action) {
        if(!inFocus) return false;

        switch(action) {
            case INTERACT:
                inFocus = false;
                this.endDialog();
        }

        return true;
    }

    @Override
    public boolean actionUp(Controls.Actions action) {
        if(!inFocus) return false;

        return true;
    }

    private Array<DialogEventListener> listeners = new Array<>();

    @Override
    public void addDialogListener(DialogEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeDialogListener(DialogEventListener listener) {
        listeners.removeValue(listener, true);
    }

    @Override
    public void clearDialogListeners() {
        listeners.clear();
    }

    @Override
    public void startDialog(String id) {
        this.onDialogStart(id);

        for(DialogEventListener listener : listeners) {
            listener.onDialogStart(id);
        }
    }

    @Override
    public void endDialog() {
        this.onDialogEnd();

        for(DialogEventListener listener : listeners) {
            listener.onDialogEnd();
        }
    }
}
