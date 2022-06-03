package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Row {
    protected Texture texture;
    protected int  rowIndex;
    protected Tile[] tiles;

    protected Row(int rowIndex, String src) {
        this.rowIndex = rowIndex;
        this.texture = new Texture(Gdx.files.internal(src));
    }
    public Tile[] getTiles() {
        return tiles;
    }

    protected abstract void render(SpriteBatch batch);
    protected abstract void update(float dt);

}
