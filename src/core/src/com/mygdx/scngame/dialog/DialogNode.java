package com.mygdx.scngame.dialog;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class DialogNode implements Iterable<DialogMessage>  {
    public final String id;
    public final Array<DialogMessage> messages = new Array<>();
    public final Array<DialogOption> options = new Array<>();

    public DialogNode(String id) {
        this.id = id;
    }

    @NotNull
    @Override
    public Iterator<DialogMessage> iterator() {
        return messages.iterator();
    }
}
