package com.mygdx.scngame.path;

import com.badlogic.gdx.math.Vector2;

public class PathNode {
    private PathNode[] neighbours;
    private String id;

    public final Vector2 position;

    public PathNode(String id, PathNode[] neighbours, float x, float y) {
        this.id = id;
        this.neighbours = neighbours;

        this.position = new Vector2(x, y);
    }

    public String getID() {return this.id;}

    public PathNode getNeighbour(int index) {
        return neighbours[index];
    }

    // package private :3 so only PathNodes can set neighbours when building up a path node
    // from a map object
    void setNeighbours(PathNode[] neighbours) {
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return this.id + ": " + this.neighbours[0].getID();
    }
}
