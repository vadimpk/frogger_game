package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;


public class Tile{

    // TODO: add different textures
    private static final Texture GRASS_TILE_TEXTURE_1 = new Texture(Gdx.files.internal("objects/tiles/grass-tile.png"));
    private static final Texture GRASS_TILE_TEXTURE_2 = new Texture(Gdx.files.internal("objects/tiles/grass-tile-2.png"));
    private static final Texture TREE_TILE_TEXTURE = new Texture(Gdx.files.internal("objects/tiles/tree-tile.png"));
    private static final Texture WATER_TILE_TEXTURE = new Texture(Gdx.files.internal("objects/tiles/water-tile.png"));
    private static final Texture ROAD_TILE_TEXTURE = new Texture(Gdx.files.internal("objects/tiles/road-tile.png"));
    private static final Texture RAIL_ROAD_TILE_TEXTURE = new Texture(Gdx.files.internal("objects/tiles/rail-road-tile.png"));
    private static final Texture FINISH_TILE_TEXTURE = new Texture(Gdx.files.internal("objects/tiles/finish-tile.png"));
    private static final Texture LILY_PAD_TEXTURE = new Texture(Gdx.files.internal("objects/tiles/lily-pad.png"));
    private static final Texture LILY_PAD_SMALL_TEXTURE = new Texture(Gdx.files.internal("objects/tiles/lily-pad-small.png"));

    /** initialize tile attributes */
    private final int ROW;
    private final int COLUMN;
    private final float X, Y;
    private final float SIZE;


    private boolean transparent;
    private boolean safe;
    private boolean isScore;
    private boolean finish;
    private Texture texture;

    Tile(int numberOfColumns, float screenWidth, float screenHeight, int row, int column) {

        this.ROW = row;
        this.COLUMN = column;

        // size of a game batch is 90% of whole screen height
        // to calculate size of a single tile size of a batch is divided by number of tiles per row
        SIZE = (float) 0.9 * screenHeight / numberOfColumns;

        // x coordinate is:
        // x of first column + size of every tile in a row before this one
        X = (float) (screenWidth - 0.9 * screenHeight) / 2 + SIZE * this.COLUMN;

        // y coordinate is:
        // starting point (5% of height) + size of every tile in a column below this one
        Y = (float) (0.05 * screenHeight) + SIZE * this.ROW;

        // set texture
        Random random = new Random();
        if (random.nextBoolean()) texture = GRASS_TILE_TEXTURE_1;
        else texture = GRASS_TILE_TEXTURE_2;
        transparent = true;
        finish = false;
        safe = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, X, Y,SIZE,SIZE);
    }


    public static void dispose() {
        GRASS_TILE_TEXTURE_1.dispose();
        TREE_TILE_TEXTURE.dispose();
    }


    /**
     * Get size of a tile
     * @return size
     */
    public float getSize() {return SIZE;}

    /**
     * Get x coordinate of a tile
     * @return x
     */
    public float getX() {return X;}

    /**
     * Get y coordinate of a tile
     * @return y
     */
    public float getY() {return Y;}

    /**
     * Get row of a tile
     * @return row
     */
    public int getROW() {return ROW;}

    /**
     * Get column of a tile
     * @return column
     */
    public int getCOLUMN() {return COLUMN;}

    /**
     * Get texture of a tile
     * @return texture
     */
    public Texture getTexture() {return texture;}

    public boolean isTransparent() {
        return transparent;
    }

    /**
     * Set texture of a tile
     * @param texture new texture
     */
    public void setTexture(Texture texture) {this.texture = texture;}

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
        if (!transparent) texture = TREE_TILE_TEXTURE;
    }

    public void setWaterTexture() {
        texture = WATER_TILE_TEXTURE;
        safe = false;
    }
    public void setRoadTexture() {
        texture = ROAD_TILE_TEXTURE;
    }
    public void setRailRoadTexture() {
        texture = RAIL_ROAD_TILE_TEXTURE;
    }
    public void setFinishTexture() {
        texture = FINISH_TILE_TEXTURE;
        finish = true;
    }
    public void setLily() {
        safe = true;
        texture = LILY_PAD_TEXTURE;
    }

    public void setSmallLily() {
        safe = true;
        texture = LILY_PAD_SMALL_TEXTURE;
    }

    public boolean isLily() {
        return texture == LILY_PAD_TEXTURE || texture == LILY_PAD_SMALL_TEXTURE;
    }

    public void setScore(boolean score) {
        isScore = score;
    }

    public boolean isScore() {
        return isScore;
    }

    public boolean isFinish() {
        return finish;
    }

    public boolean isSafe() {
        return safe;
    }
}
