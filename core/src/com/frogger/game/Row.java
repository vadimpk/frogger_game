package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.frogger.game.FroggerGame.tilesPerRow;

public class Row {
    protected Texture texture;
    protected float x, y;
    protected int width, height;

    public Row(Tile startingTile, String src) {
        this.x = startingTile.getX();
        this.y = startingTile.getY();
        this.width = (int) (startingTile.getSize() * tilesPerRow);
        this.height = (int) startingTile.getSize();
        texture = new Texture(Gdx.files.internal(src));
    }

    protected void render(SpriteBatch batch){
        batch.draw(texture, x, y, width, height);
    }
}
