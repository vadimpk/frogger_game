package com.frogger.game.attributeObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.utils.Audio;
import com.frogger.game.mapObjects.Tile;

import static com.frogger.game.mapObjects.Map.getFrog;

/**
 * Score.java
 * @author stas-bukovskiy
 * Class of star score, that is on each level and should be collected by player
 */
public class Score {

    private static final Texture STARS_PACK = new Texture(Gdx.files.internal("objects/stars/stars.png"));

    private static final TextureRegion STAR_1 = new TextureRegion(STARS_PACK, 0, 280,289,280);
    private static final TextureRegion STAR_2 = new TextureRegion(STARS_PACK, 289, 560,289,280);
    private static final TextureRegion STAR_3 = new TextureRegion(STARS_PACK, 0, 0,289,280);
    private static final TextureRegion STAR_4 = new TextureRegion(STARS_PACK, 289, 0,289,280);
    private static final TextureRegion STAR_5 = new TextureRegion(STARS_PACK, 578, 0,289,280);

    private final Tile tile;
    private TextureRegion texture;
    private boolean isCollected;

    private int animationState;
    private static final long ANIMATION_TIME = 10000000;
    private long startTime;

    /**
     * Constructor creates star score on specific tile
     * @param tile - tile where score will be created
     */
    public Score(Tile tile) {
        this.tile = tile;
        texture = STAR_1;
        animationState = 1;
        startTime = TimeUtils.nanoTime();
        isCollected = false;
    }

    /**
     * Method draw star if it is not collected
     * @param batch - SpriteBatch instance for drawing
     */
    public void render(SpriteBatch batch) {
        update();
        batch.draw(texture, tile.getX() + 0.1f * tile.getSize(), tile.getY() + 0.1f * tile.getSize(), 0.8f * tile.getSize(), 0.8f * tile.getSize());
    }

    /**
     * Method checks if frog collects this score and sets texture for animating
     */
    private void update() {

        if (getFrog().getX() + getFrog().getSize() >= tile.getX() + 0.3*tile.getSize() &&
                getFrog().getX() <= tile.getX() + 0.7*tile.getSize() &&
                getFrog().getY() + getFrog().getSize() >= tile.getY() + 0.3*tile.getSize() &&
                getFrog().getY() <= tile.getY() + 0.7*tile.getSize()) {
            isCollected = true;
            Audio.playCollectedStarSound();
        }

        //if (tile.getX() - getFrog().getSize() / 2 <= getFrog().getX() && getFrog().getX() + 0.5*getFrog().getSize() <= tile.getX() + tile.getSize() &&
        //        tile.getY() - getFrog().getSize() / 2 <= getFrog().getY() && getFrog().getY()  + 0.5*getFrog().getSize()<= tile.getY() + tile.getSize())

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

    /**
     * Method dispose star texture
     */
    public void dispose() {
        STARS_PACK.dispose();
    }

    /**
     * Method returns true if this score is collected otherwise false
     * @return true if this score is collected otherwise false
     */
    public boolean isCollected() {
        return isCollected;
    }

    /**
     * Method sets score uncollected
     */
    public void setUncollected(){
        isCollected = false;
    }
}
