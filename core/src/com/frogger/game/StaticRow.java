package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.frogger.game.FroggerGame.tilesPerRow;

public class StaticRow extends Row {
    public StaticRow(int rowIndex, String src) {
        super(rowIndex, src);
        tiles = new Tile[tilesPerRow];
        for (int i = 0; i < tilesPerRow; i ++)
            tiles[i] = new Tile(tilesPerRow, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), i, rowIndex);
    }

    public void render(SpriteBatch batch){
        for(Tile tile: tiles)
            batch.draw(tile.getTexture(), tile.getX(), tile.getY(), tile.getSize(), tile.getSize());
    }

    @Override
    protected void update(float dt) {

    }
}
