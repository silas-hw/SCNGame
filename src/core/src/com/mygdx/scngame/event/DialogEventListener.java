package com.mygdx.scngame.event;

public interface DialogEventListener {
    void onDialogStart(String dialogFile, String dialogID);
    void onDialogEnd();
}
