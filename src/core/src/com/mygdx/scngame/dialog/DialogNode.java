package com.mygdx.scngame.dialog;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class DialogNode implements Iterable<DialogMessage>  {
    public String id;
    public final Array<DialogMessage> messages = new Array<>();
    public final Array<DialogOption> options = new Array<>();

    public DialogNode() {
        // TODO: remove test option
        options.add(new DialogOption("Goodbye!"));
        options.add(new DialogOption("Let's do that again!", this));
    }

    @NotNull
    @Override
    public Iterator<DialogMessage> iterator() {
        return messages.iterator();
    }
}
