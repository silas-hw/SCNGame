package com.mygdx.scngame.event;

public interface DialogEventListener {
    // TODO: change to handle concrete DialogNode objects instead of relying on the bus to get it itself
    // smell: primitive obsession
    void onDialogStart(String dialogFile, String dialogID);
    void onDialogEnd();
}
