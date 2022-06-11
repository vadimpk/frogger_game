package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.frogger.game.Map.getFrog;

public class Score {

    private Tile tile;
    private Texture texture;
    private boolean isCollected;

    public Score(Tile tile) {
        this.tile = tile;
        texture = new Texture(Gdx.files.internal("star.png"));
        isCollected = false;
    }

    public void render(SpriteBatch batch) {
        update();
        batch.draw(texture, tile.getX() + 0.1f * tile.getSize(), tile.getY() + 0.1f * tile.getSize(), 0.8f * tile.getSize(), 0.8f * tile.getSize());
    }

    private void update() {
        if (getFrog().getTile() == tile) isCollected = true;
    }

    public void dispose() {
        texture.dispose();
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setUncollected(){
        isCollected = false;
    }
}
