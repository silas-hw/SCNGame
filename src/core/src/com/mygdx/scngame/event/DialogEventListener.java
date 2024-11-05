package com.mygdx.scngame.event;

import com.mygdx.scngame.dialog.DialogNode;

public interface DialogEventListener {
    // TODO: change to handle concrete DialogNode objects instead of relying on the bus to get it itself
    // smell: primitive obsession
    void onDialogStart(DialogNode node);
    void onDialogEnd();
}
