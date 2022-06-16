package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import static com.frogger.game.Map.getFrog;

public class Score {

    private static final Texture STAR_1 = new Texture(Gdx.files.internal("objects/stars/star-small-1.png"));
    private static final Texture STAR_2 = new Texture(Gdx.files.internal("objects/stars/star.png"));
    private static final Texture STAR_3 = new Texture(Gdx.files.internal("objects/stars/star-big-1.png"));
    private static final Texture STAR_4 = new Texture(Gdx.files.internal("objects/stars/star-big-2.png"));
    private static final Texture STAR_5 = new Texture(Gdx.files.internal("objects/stars/star-big-3.png"));


    private Tile tile;
    private Texture texture;
    private boolean isCollected;

    private int animationState;
    private static final long ANIMATION_TIME = 10000000;
    private long startTime;

    public Score(Tile tile) {
        this.tile = tile;
        texture = STAR_1;
        animationState = 1;
        startTime = TimeUtils.nanoTime();
        isCollected = false;
    }

    public void render(SpriteBatch batch) {
        update();
        batch.draw(texture, tile.getX() + 0.1f * tile.getSize(), tile.getY() + 0.1f * tile.getSize(), 0.8f * tile.getSize(), 0.8f * tile.getSize());
    }

    private void update() {
        if (getFrog().getTile() == tile) isCollected = true;

        if (TimeUtils.nanoTime() - startTime > 10 * ANIMATION_TIME) {
            animationState = (animationState > 9) ? 0 : animationState + 1;
            startTime = TimeUtils.nanoTime();
        }

        switch (animationState) {
            case 1:
            case 10:
                texture = STAR_1;
                break;
            case 2:
            case 9:
                texture = STAR_2;
                break;
            case 3:
            case 8:
                texture = STAR_3;
                break;
            case 4:
            case 7:
                texture = STAR_4;
                break;
            case 5:
            case 6:
                texture = STAR_5;
                break;

        }

    }

    public void dispose() {
        STAR_1.dispose();
        STAR_2.dispose();
        STAR_3.dispose();
        STAR_4.dispose();
        STAR_5.dispose();
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setUncollected(){
        isCollected = false;
    }
}
