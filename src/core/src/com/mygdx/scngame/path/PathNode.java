package com.mygdx.scngame.path;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PathNode {
    public Array<PathNode> neighbours = new Array<>();
    private String id;

    public final Vector2 position = new Vector2();

    public PathNode(String id, Vector2 position) {
        this.id = id;
        this.position.set(position);
    }

    public PathNode(String id, float x, float y) {
        this.id = id;
        this.position.set(x, y);
    }

    public PathNode(String id, PathNode[] neighbours, float x, float y) {
        this.id = id;

        for (PathNode node : neighbours) {
            this.neighbours.add(node);
        }

        this.position.set(x, y);
    }

    public String getID() {return this.id;}
}
