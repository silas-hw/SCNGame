package com.mygdx.scngame.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TiledNinePatch implements Drawable {
    Texture texture;

    TextureRegion topLeft;
    TextureRegion topRight;
    TextureRegion bottomLeft;
    TextureRegion bottomRight;
    TextureRegion top;
    TextureRegion bottom;
    TextureRegion left;
    TextureRegion right;

    int minWidth;
    int minHeight;

    int height;
    int width;

    public float scale = 1f;

    Vector2 position = new Vector2();

    /**
     * All coordinates are top-down, as according to how {@link TextureRegion} works
     *
     * @param texture the nine patch image
     * @param x the x coordinate of the center patch
     * @param y the y coordinate of the center patch
     * @param width the width of the center patch
     * @param height the height of the center patch
     *
     * @author Silas Hayes-Williams
     */
    public TiledNinePatch(Texture texture, int x, int y, int width, int height) {
        this.texture = texture;

        this.width = minWidth = texture.getWidth() - width;
        this.height = minHeight = texture.getHeight() - height;

        topLeft = new TextureRegion(texture, 0, 0, x, y);

        left = new TextureRegion(texture, 0, y, x, height);
        bottomLeft = new TextureRegion(texture, 0, y + height, x, texture.getHeight() -  y - height);

        top = new TextureRegion(texture, x, 0, width, y);
        bottom = new TextureRegion(texture, x, y + height, width, texture.getHeight() - y - height);

        topRight = new TextureRegion(texture, x + width, 0, texture.getWidth() -  x - width, y);
        right = new TextureRegion(texture, x + width, y, texture.getWidth() - x - width, height);

        bottomRight = new TextureRegion(texture, x + width, y + height, texture.getWidth() -  x - width, texture.getHeight() - y - height);
    }


    public void draw(Batch batch) {

        batch.draw(bottomLeft, position.x, position.y, 0, 0,
                bottomLeft.getRegionWidth(), bottomLeft.getRegionHeight(), scale,
                scale, 0f);

        int leftHeight = height - bottomLeft.getRegionHeight() - topLeft.getRegionHeight();
        int leftCount = 0;
        for(; leftCount < (leftHeight/left.getRegionHeight()); leftCount++) {
            batch.draw(left, position.x, position.y+bottomLeft.getRegionHeight() * scale
                            + left.getRegionHeight() * leftCount * scale,
                    0, 0, left.getRegionWidth(), left.getRegionHeight(),
                    scale, scale, 0f);
        }

        // draw remaining height with fraction of tile

        int leftMod = leftHeight%left.getRegionHeight();

        if(leftMod > 0) {
            TextureRegion _temp = new TextureRegion(left, 0, left.getRegionHeight()-leftMod, left.getRegionWidth(), leftMod);
            batch.draw(_temp, position.x,
                    position.y + bottomLeft.getRegionHeight() * scale + left.getRegionHeight() * leftCount * scale,
                    0, 0, _temp.getRegionWidth(),_temp.getRegionHeight(), scale, scale, 0f);
        }

        batch.draw(topLeft, position.x,
                position.y + bottomLeft.getRegionHeight() * scale + left.getRegionHeight() * leftCount * scale + leftMod * scale,
                0, 0, topLeft.getRegionWidth(), topLeft.getRegionHeight(), scale, scale, 0f);

        int bottomWidth = width - bottomLeft.getRegionWidth() - bottomRight.getRegionWidth();

        int bottomCount;
        for(bottomCount = 0; bottomCount < (bottomWidth / bottom.getRegionWidth()); bottomCount++) {
            batch.draw(bottom, position.x + bottomLeft.getRegionWidth() * scale + bottom.getRegionWidth() * bottomCount * scale,
                    position.y, 0, 0, bottom.getRegionWidth(), bottom.getRegionHeight(), scale, scale, 0f);
        }

        int bottomMod = bottomWidth%bottom.getRegionWidth();

        if(bottomMod > 0) {
            TextureRegion _temp = new TextureRegion(bottom, 0, 0, bottomMod, bottom.getRegionHeight());
            batch.draw(_temp, position.x + bottomLeft.getRegionWidth() * scale + bottom.getRegionWidth() * bottomCount * scale,
                    position.y, 0, 0, _temp.getRegionWidth(), _temp.getRegionHeight(), scale, scale, 0f);
        }

        batch.draw(bottomRight,
                position.x + bottomLeft.getRegionWidth() * scale + bottom.getRegionWidth() * bottomCount * scale + bottomMod * scale,
                position.y, 0, 0, bottomRight.getRegionWidth(), bottomRight.getRegionHeight(), scale, scale, 0f);

        int topWidth = width - topLeft.getRegionWidth() - topRight.getRegionWidth();

        int topCount;
        for(topCount = 0; topCount < (topWidth / bottom.getRegionWidth()); topCount++) {
            batch.draw(top, position.x + topLeft.getRegionWidth() * scale + top.getRegionWidth() * scale * topCount,
                    position.y + height * scale - top.getRegionHeight() * scale, 0, 0, top.getRegionWidth(), top.getRegionHeight(), scale, scale, 0f);
        }

        int topMod = topWidth%top.getRegionWidth();

        if(topMod > 0) {
            TextureRegion _temp = new TextureRegion(top, 0, 0, topMod, top.getRegionHeight());
            batch.draw(_temp, position.x + topLeft.getRegionWidth() * scale + top.getRegionWidth() * scale * topCount,
                    position.y + height * scale - top.getRegionHeight() * scale, 0, 0, _temp.getRegionWidth(),
                    _temp.getRegionHeight(), scale,
                    scale, 0f);
        }

        batch.draw(topRight, position.x + width * scale - topRight.getRegionWidth() * scale,
                position.y + height * scale - topRight.getRegionHeight() * scale, 0, 0, topRight.getRegionWidth(),
                topRight.getRegionHeight(), scale, scale, 0f);

        int rightHeight = height - topRight.getRegionHeight() - bottomRight.getRegionHeight();

        int rightCount;
        for(rightCount = 0; rightCount < (rightHeight / bottom.getRegionHeight()); rightCount++) {
            batch.draw(right, position.x + width * scale - right.getRegionWidth() * scale,
                    position.y + bottomRight.getRegionHeight() * scale + right.getRegionHeight() * scale * rightCount,
                    0, 0, right.getRegionWidth(), right.getRegionHeight(), scale, scale, 0f);
        }

        int rightMod = rightHeight%right.getRegionHeight();
        if(rightMod > 0) {
            TextureRegion _temp = new TextureRegion(right, 0, 0, right.getRegionWidth(), rightMod);
            batch.draw(_temp, position.x + width * scale - right.getRegionWidth() * scale,
                    position.y + bottomRight.getRegionHeight() * scale + right.getRegionHeight() * scale * rightCount,
                    0, 0, _temp.getRegionWidth(), _temp.getRegionHeight(), scale, scale,0f);

        }
    }

    /**
     *
     * @param width the width respective to the scale. For example, if the scale is 2 and the width is set to
     *              300, the displayed size when drawn will be 600.
     *
     * @author Silas Hayes-Williams
     */
    public void setWidth(int width) {
        this.width = Math.max(minWidth, width);
    }

    /**
     *
     * @param height the height respective to the scale. For example, if the scale is 2 and the height is set to 300,
     *               the displayed size when drawn will be 600.
     *
     * @author Silas Hayes-Willams
     */
    public void setHeight(int height)   {
        this.height = Math.max(minHeight, height);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**
     * Draws the nine patch at a true width and height. The tiles will be scaled according to the scale and the
     * width and height set such that the nine patch encompasses the width and height provided.
     *
     * <p>
     *     If you set the scale to 2, and call this method with a width of 200 and height of 400, then the tiles
     *     will be doubled in size but the nine patch itself will be drawn with a width of 200 by 400 pixels.
     * </p>
     *
     * <p>
     *     Calling this method will change the width, height, and position of the ninepatch.
     * </p>
     *
     * @param batch
     * @param x
     * @param y
     * @param width the true width to draw the nine patch at
     * @param height the true height to draw the nine patch at
     */
    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        setWidth((int) (width / scale));
        setHeight((int) (height / scale));

        position.x = x;
        position.y = y;

        draw(batch);
    }

    @Override
    public float getLeftWidth() {
        return left.getRegionWidth() * scale;
    }

    @Override
    public void setLeftWidth(float v) {
        // bugger off!
    }

    @Override
    public float getRightWidth() {
        return right.getRegionWidth() * scale;
    }

    @Override
    public void setRightWidth(float v) {
        // bugger off!
    }

    @Override
    public float getTopHeight() {
        return top.getRegionHeight() * scale;
    }

    @Override
    public void setTopHeight(float v) {
        // bugger off!
    }

    @Override
    public float getBottomHeight() {
        return bottom.getRegionHeight() * scale;
    }

    @Override
    public void setBottomHeight(float v) {
        // bugger off!
    }

    @Override
    public float getMinWidth() {
        return minWidth;
    }

    @Override
    public void setMinWidth(float v) {
        // bugger off!
    }

    @Override
    public float getMinHeight() {
        return minHeight;
    }

    @Override
    public void setMinHeight(float v) {
        // bugger off!
    }
}
