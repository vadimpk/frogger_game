package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.frogger.game.screens.FroggerGameScreen.level;

public class Scorer {

    public static final Texture FILLED_STAR = new Texture(Gdx.files.internal("objects/stars/star.png"));
    public static final Texture UNFILLED_STAR = new Texture(Gdx.files.internal("objects/stars/star-unfilled.png"));
    private float x, y, size;

    public Scorer(float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.size = size;

        for (Score s: level.getMap().getScores()) {
            s.setUncollected();
        }
    }

    public void render(SpriteBatch batch) {
        int score = 0;
        for (Score s: level.getMap().getScores()) {
            if(s.isCollected()) score++;
        }


        switch (score) {
            case 0:
                batch.draw(UNFILLED_STAR, x, y, size, size);
                batch.draw(UNFILLED_STAR, x + size, y, size, size);
                batch.draw(UNFILLED_STAR, x + 2*size, y, size, size);
                break;
            case 1:
                batch.draw(FILLED_STAR, x, y, size, size);
                batch.draw(UNFILLED_STAR, x + size, y, size, size);
                batch.draw(UNFILLED_STAR, x + 2*size, y, size, size);
                break;
            case 2:
                batch.draw(FILLED_STAR, x, y, size, size);
                batch.draw(FILLED_STAR, x + size, y, size, size);
                batch.draw(UNFILLED_STAR, x + 2*size, y, size, size);
                break;
            case 3:
                batch.draw(FILLED_STAR, x, y, size, size);
                batch.draw(FILLED_STAR, x + size, y, size, size);
                batch.draw(FILLED_STAR, x + 2*size, y, size, size);
                break;
            default:
                throw new RuntimeException("the illegal value for score: " + score);
        }

    }

    public static void dispose(){
        FILLED_STAR.dispose();
        UNFILLED_STAR.dispose();
    }
}
