package com.frogger.game.mapObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.frogger.game.DataIO;
import com.frogger.game.skins.CharacterSkin;
import com.frogger.game.Util;

import java.util.Random;

/**
 * Tile.java
 * @author vadympolishchuk
 * Method of a single tile.
 * Tile is the marking point of the game (every other game object is of a size of a single tile)
 * Tiles can be transparent, not transparent (trees) water, road or lily pads
 */

public class Tile{

    private static final Texture TILES_PACK = new Texture(Gdx.files.internal("objects/tiles/tiles.png"));

    private static final TextureRegion WATER_TILE_TEXTURE = new TextureRegion(TILES_PACK, 600, 600, 300, 300);
    private static final TextureRegion ROAD_TILE_TEXTURE = new TextureRegion(TILES_PACK, 0, 600, 300, 300);
    private static final TextureRegion RAIL_ROAD_TILE_TEXTURE = new TextureRegion(TILES_PACK, 600, 300, 300, 300);
    private static final TextureRegion FINISH_TILE_TEXTURE = new TextureRegion(TILES_PACK, 0, 0, 300, 300);
    private static final TextureRegion LILY_PAD_TEXTURE = new TextureRegion(TILES_PACK, 300, 300, 300, 300);
    private static final TextureRegion LILY_PAD_SMALL_TEXTURE = new TextureRegion(TILES_PACK, 0, 300, 300, 300);

    /** initialize tile attributes */
    private final int ROW;
    private final int COLUMN;
    private final float X, Y;
    private final float SIZE;

    private CharacterSkin skin;

    private boolean transparent;
    private boolean safe;
    private boolean isScore;
    private boolean finish;
    private TextureRegion texture;
    private int textureRotation;

    /**
     * Default constructor of a tile. Sets a tile in a certain position
     * depending on its row and column and world size
     * @param numberOfColumns columns in total
     * @param screenWidth screen width
     * @param screenHeight screen height
     * @param row row
     * @param column column
     */
    public Tile(int numberOfColumns, float screenWidth, float screenHeight, int row, int column) {

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

        skin = DataIO.getTileSkins()[0];
        for (CharacterSkin tileSkin : DataIO.getTileSkins()) {
            if(tileSkin.isActive()) skin = tileSkin;
        }

        // set texture
        Random random = new Random();
        if (random.nextBoolean()) texture = skin.transparentTile1;
        else texture = skin.transparentTile2;
        transparent = true;
        finish = false;
        safe = true;
        textureRotation = 0;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, X, Y, SIZE/2, SIZE/2 , SIZE, SIZE, 1, 1, textureRotation);
    }


    public static void dispose() {
        TILES_PACK.dispose();
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
    public TextureRegion getTexture() {return texture;}

    public boolean isTransparent() {
        return transparent;
    }

    public void setNewSkin(Util.TileSkin newSkin) {
        skin.setTileSkin(newSkin);

        if (isTransparent()) {
            if (texture != WATER_TILE_TEXTURE && texture != LILY_PAD_TEXTURE && texture != RAIL_ROAD_TILE_TEXTURE
                    && texture != ROAD_TILE_TEXTURE && texture != FINISH_TILE_TEXTURE) {

                Random random = new Random();
                if (random.nextBoolean()) texture = skin.transparentTile1;
                else texture = skin.transparentTile2;
            }
        } else {
            texture = skin.notTransparentTile;
        }

    }

    /**
     * Set texture of a tile
     * @param texture new texture
     */
    public void setTexture(TextureRegion texture) {this.texture = texture;}

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
        if (!transparent) texture = skin.notTransparentTile;
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

    public void setRandomRotation() {
        Random rand = new Random();
        if (rand.nextBoolean())
            textureRotation = 90;
            if (rand.nextBoolean())
                textureRotation = 0;
        else
            textureRotation = 180;
            if (rand.nextBoolean())
                textureRotation = 270;
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
