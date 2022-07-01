package com.frogger.game.attributeObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.frogger.game.screens.FroggerGameScreen.level;

/**
 * Scorer.java
 * @author stas-bukovskiy
 * Class for displaying how much star is collected in FroggerGameScreen
 */
public class Scorer {

    private static final Texture STARS_PACK = new Texture(Gdx.files.internal("objects/stars/stars.png"));

    public static final TextureRegion FILLED_STAR = new TextureRegion(STARS_PACK, 289, 560, 289, 280);
    public static final TextureRegion UNFILLED_STAR = new TextureRegion(STARS_PACK, 0, 560, 289, 280);

    private static final BitmapFont FONT = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"));

    private final float x, y, size;

    /**
     * Constructor of class
     * @param x - starting x-coordinate of star drawing
     * @param y - starting y-coordinate of star drawing
     * @param size - size of star that is drawing
     */
    public Scorer(float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.size = size;

        for (Score s : level.getMap().getScores()) {
            s.setUncollected();
        }
    }

    /**
     * Method draws filled and unfilled star
     * @param batch - SpriteBatch for drawing
     */
    public void render(SpriteBatch batch) {
        int score = 0;
        for (Score s : level.getMap().getScores()) {
            if (s.isCollected()) score++;
        }

        switch (score) {
            case 0:
                batch.draw(UNFILLED_STAR, x, y, size, size);
                batch.draw(UNFILLED_STAR, x + size, y, size, size);
                batch.draw(UNFILLED_STAR, x + 2 * size, y, size, size);
                break;
            case 1:
                batch.draw(FILLED_STAR, x, y, size, size);
                batch.draw(UNFILLED_STAR, x + size, y, size, size);
                batch.draw(UNFILLED_STAR, x + 2 * size, y, size, size);
                break;
            case 2:
                batch.draw(FILLED_STAR, x, y, size, size);
                batch.draw(FILLED_STAR, x + size, y, size, size);
                batch.draw(UNFILLED_STAR, x + 2 * size, y, size, size);
                break;
            case 3:
                batch.draw(FILLED_STAR, x, y, size, size);
                batch.draw(FILLED_STAR, x + size, y, size, size);
                batch.draw(FILLED_STAR, x + 2 * size, y, size, size);
                break;
            default:
                throw new RuntimeException("the illegal value for score: " + score);
        }
    }

    /**
     * Method for texture disposing
     */
    public static void dispose() {
        STARS_PACK.dispose();
        FONT.dispose();
    }
}
