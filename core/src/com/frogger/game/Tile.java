package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public class Tile {

    // TODO: add different textures
    private static Texture defaultTileTexture = new Texture(Gdx.files.internal("tile.png"));

    /** initialize tile attributes */
    private int row, column;
    private float x, y;
    private float size;
    private Texture texture;

    Tile(int numberOfRows, float screenWidth, float screenHeight, int column, int row) {

        this.column = column;
        this.row = row;

        // size of a game batch is 90% of whole screen height
        // to calculate size of a single tile size of a batch is divided by number of tiles per row
        size = (float) 0.9 * screenHeight / numberOfRows;

        // x coordinate is:
        // x of first column + size of every tile in a row before this one
        x = (float) (screenWidth - 0.9 * screenHeight) / 2 + size * this.column;

        // y coordinate is:
        // starting point (5% of height) + size of every tile in a column below this one
        y = (float) (0.05 * screenHeight) + size * this.row;

        // set texture
        texture = defaultTileTexture;
    }


    /**
     * Get size of a tile
     * @return size
     */
    public float getSize() {return size;}

    /**
     * Get x coordinate of a tile
     * @return x
     */
    public float getX() {return x;}

    /**
     * Get y coordinate of a tile
     * @return y
     */
    public float getY() {return y;}

    /**
     * Get row of a tile
     * @return row
     */
    public int getRow() {return row;}

    /**
     * Get column of a tile
     * @return column
     */
    public int getColumn() {return column;}

    /**
     * Get texture of a tile
     * @return texture
     */
    public Texture getTexture() {return texture;}

    /**
     * Set texture of a tile
     * @param texture new texture
     */
    public void setTexture(Texture texture) {this.texture = texture;}
}
