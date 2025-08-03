package com.mygdx.scngame.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.scngame.controls.ActionListener;
import com.mygdx.scngame.event.EventBus;
import com.mygdx.scngame.event.EventListener;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.settings.Settings;
import com.mygdx.scngame.ui.TiledNinePatch;
import com.mygdx.scngame.ui.TruetypeLabel;

import java.util.Iterator;

/**
 * Encapsulates the handling of dialog events, including capturing events, drawing dialog boxes, and
 * firing dialog end events.
 */
public class DialogView implements ActionListener, EventListener<DialogEvent>, InputProcessor {
    private boolean inFocus = false;

    private final FreeTypeFontGenerator fontGenerator;

    private final Skin skin;
    private final Label messageLabel;
    private final Stage stage;

    private final Container<Table> dialogWrapper;
    private final Table dialogContainer;

    private final Table optionsContainer;
    private Image icon;

    TiledNinePatch npatch;

    private final Settings settings;

    AssetManager assets;
    Iterator<DialogMessage> currentMessages;
    DialogNode defaultDialog;

    EventBus<DialogEvent> eventBus;

    public DialogView(ScreenData screenData, EventBus<DialogEvent> eventBus) {
        this.eventBus = eventBus;

        this.settings = screenData.settings();
        this.assets = screenData.assets();
        this.skin = screenData.assets().get("skin/uiskin2.json", Skin.class);

        this.dialogSound = Gdx.audio.newSound(Gdx.files.internal("sfx/blipc5.mp3"));

        DialogMessage defaultMessage = new DialogMessage();
        defaultMessage.speaker = "Error...";
        defaultMessage.message = "Invalid dialog file provided.";
        defaultMessage.icon = assets.get("sprites/sign.png", Texture.class);

        defaultDialog = new DialogNode();
        defaultDialog.messages.add(defaultMessage);

        currentMessages = defaultDialog.iterator();

        float scale = settings.getUIScale();

        stage = new Stage(new ScreenViewport());

        Table root = new Table();
        root.setFillParent(true);
        root.pad(20f);
        stage.addActor(root);

        fontGenerator = screenData.assets().get("skin/MyFont2.ttf", FreeTypeFontGenerator.class);
        messageLabel = new TruetypeLabel(fontGenerator, 20);

        messageLabel.setFontScale(scale);
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.top | Align.left);

        Container<Label> messageWrapper = new Container<>(messageLabel);
        messageWrapper.center();
        messageWrapper.fill();

        Texture patchTexture = screenData.assets().get("sprites/patch.9.png", Texture.class);

        icon = new Image(patchTexture);
        icon.setAlign(Align.center);
        icon.setScaling(Scaling.stretch);
        icon.setScale(settings.getUIScale());

        // container for dialog options
        optionsContainer = new Table();

        // colspan didn't work, so just manually setting the width of the icon and scaling it is best
        dialogContainer = new Table();
        dialogContainer.add(messageWrapper).grow();
        dialogContainer.add(icon).growY().width(Value.percentWidth(0.3f, dialogContainer));

        dialogContainer.row();
        dialogContainer.add(optionsContainer).growX().height(Value.percentHeight(0.2f, dialogContainer));

        dialogContainer.layout();

        dialogWrapper = new Container<>(dialogContainer);

        dialogWrapper.fill();

        npatch = TiledNinePatch.getInstanceFromDot9(patchTexture);

        dialogWrapper.setBackground(npatch, false);

        root.add(dialogWrapper);

        root.bottom();

        stage.setDebugAll(true);

    }

    public void draw(float delta) {
        // constants
        final float CONTAINER_WIDTH = 600f;
        final float CONTAINER_HEIGHT = 150f;
        final float basePatchScale = 3f;

        if(!inFocus) {
            return;
        }

        tickCharacter(delta);

        float scale = settings.getUIScale();

        stage.act();

        messageLabel.setFontScale(scale);

        dialogWrapper.width(CONTAINER_WIDTH * scale);
        dialogWrapper.height(CONTAINER_HEIGHT * scale);

        npatch.scale = basePatchScale * scale;
        dialogWrapper.pad(npatch.getTopHeight(), npatch.getLeftWidth(), npatch.getBottomHeight(), npatch.getRightWidth());

        dialogWrapper.layout();
        dialogContainer.layout();

        stage.getViewport().apply();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    private DialogNode node;

    public void onDialogStart(DialogNode dialogNode) {
        inFocus = true;
        currentMessages = dialogNode.iterator();
        node = dialogNode;
        this.nextMessage();
    }

    String currentMessage = "";
    int currentMessageIndex = 0;

    float charTimer = 0f;
    float baseCharTime = 0.07f;
    float charTime = baseCharTime;

    float nextMessageCooldown = 0.1f;
    final float messageCooldownTime = 0.2f;

    Sound dialogSound;
    float pitch = 1f;

    // tick to the next character if enough time has passed
    void tickCharacter(float delta) {
        charTimer += delta;

        // scrolls through text one character at a time
        if(charTimer >= charTime && currentMessageIndex < currentMessage.length()) {
            charTimer = 0f;
            char currentChar = currentMessage.charAt(currentMessageIndex);
            messageLabel.setText(messageLabel.getText().append(currentChar).toString());
            messageLabel.invalidate();
            currentMessageIndex++;

            if(currentChar != ' ') {
                dialogSound.play(0.2f, pitch, 0f);
            }

            if(currentChar == '.') {
                charTime = baseCharTime * 2;
            } else {
                charTime = baseCharTime;
            }

            nextMessageCooldown = messageCooldownTime;
        }

        if(currentMessageIndex >= currentMessage.length()-1) {
            optionsContainer.setTouchable(Touchable.enabled);
        }

        nextMessageCooldown -= delta;
    }

    // skips all the remaining character ticks, filling the display text with the full message
    void skipCharacterTicks() {
        String slice = currentMessage.substring(currentMessageIndex);
        messageLabel.setText(messageLabel.getText().append(slice).toString());
        messageLabel.invalidate();

        currentMessageIndex = currentMessage.length();
        optionsContainer.setTouchable(Touchable.enabled);
    }

    /**
        Move to the next message in the current dialogNode. If it happens to be the final
        message, display dialog options if any are present but keep the buttons disabled (they
        are enabled once the full message has been displayed, either by `tickCharacter` finishing or
        `skipCharacterTicks()`).
        <p>
        If there are no messages left and there are no dialog options for the current node, this ends
        the dialog sequence by creating a dialog end event.
     */
    void nextMessage() {
        if(currentMessages.hasNext()) {
            DialogMessage msg = currentMessages.next();

            currentMessage = msg.message;
            currentMessageIndex = 0;

            dialogSound = msg.sound;
            pitch = msg.pitch;

            messageLabel.setText(msg.speaker + ": \n ");

            Image icon = new Image(msg.icon);
            icon.setScaling(Scaling.fit);

            dialogContainer.getCell(this.icon).setActor(icon);

            this.icon = icon;

            if (!node.options.isEmpty() && !currentMessages.hasNext()) {
                optionsContainer.clear();
                optionsContainer.setTouchable(Touchable.disabled);
                for(DialogOption option : node.options) {
                    TruetypeLabel label = new TruetypeLabel(fontGenerator, 20);
                    label.setText(option.getText());

                    Button butt = new Button(skin);
                    butt.add(label);

                    butt.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            optionsContainer.clear();
                            if(option.nextNode().isEmpty()) {
                                eventBus.publish(new DialogEvent(node, DialogEvent.EventType.DIALOG_END));
                                return;
                            }

                            onDialogStart(option.nextNode().get());
                        }
                    });

                    optionsContainer.add(butt);
                }
        } else if(node.options.isEmpty())
            eventBus.publish(new DialogEvent(node, DialogEvent.EventType.DIALOG_END));
        }
    }

    public void onDialogEnd() {
        inFocus = false;
        currentMessages = defaultDialog.iterator();
        node = defaultDialog;
    }


    @Override
    public boolean actionDown(Controls.Actions action) {
        if(!inFocus) return false;

        if(action == Controls.Actions.INTERACT) {
            if(this.currentMessageIndex < currentMessage.length()) {
                this.skipCharacterTicks();
            } else if(nextMessageCooldown <= 0f) {
                this.nextMessage();
            }
        }

        return true;
    }

    @Override
    public boolean actionUp(Controls.Actions action) {
        return inFocus;
    }

    public void onEvent(DialogEvent event) {
        switch(event.eventType) {
            case DIALOG_END:
                onDialogEnd();
                break;

            case DIALOG_START:
                onDialogStart(event.dialog);
                break;
        }
    }

    // InputProcessor logic. Passes all to stage but blocks if not in focus

    @Override
    public boolean keyDown(int keycode) {
        if(!inFocus) return false;

        return stage.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if(!inFocus) return false;

        return stage.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if(!inFocus) return false;

        return stage.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!inFocus) return false;

        return stage.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!inFocus) return false;

        return stage.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        if(!inFocus) return false;

        return stage.touchCancelled(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!inFocus) return false;

        return stage.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(!inFocus) return false;

        return stage.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if(!inFocus) return false;

        return stage.scrolled(amountX, amountY);
    }
}