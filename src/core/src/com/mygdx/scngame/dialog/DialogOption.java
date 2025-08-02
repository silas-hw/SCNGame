package com.mygdx.scngame.dialog;

import java.util.Optional;

/***
 * Represents a point of diversion within a dialog tree. Provides the text to be displayed to
 * the user alongside what DialogNode this choice leads to.
 ***/
public class DialogOption {
    private final String text;
    private final DialogNode next;

    public DialogOption(String text) {
        this(text, null);
    }

    public DialogOption(String text, DialogNode next) {
        this.text = text;
        this.next = next;
    }

    public Optional<DialogNode> nextNode() {
        return Optional.ofNullable(next);
    }

    public String getText() {
        return text;
    }
}
