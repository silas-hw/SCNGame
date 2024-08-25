package com.mygdx.scngame.event;

import com.badlogic.gdx.Gdx;

public interface DialogEventBus {

    void addDialogListener(DialogEventListener listener);
    void removeDialogListener(DialogEventListener listener);
    void clearDialogListeners();

    void startDialog(String id);
    void endDialog();
}
