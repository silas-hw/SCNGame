package com.mygdx.scngame.event;

import com.badlogic.gdx.Gdx;
import com.mygdx.scngame.dialog.DialogNode;

public interface DialogEventBus {

    void addDialogListener(DialogEventListener listener);
    void removeDialogListener(DialogEventListener listener);
    void clearDialogListeners();

    void startDialog(DialogNode dialog);
    void endDialog();
}
