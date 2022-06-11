package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.frogger.game.screens.FroggerGameScreen.level;

public class Scorer {
    private Texture filledStar;
    private Texture unfilledStar;
    private float x, y, size;

    public Scorer(float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.size = size;

        filledStar = new Texture(Gdx.files.internal("star.png"));
        unfilledStar = new Texture(Gdx.files.internal("unfilled_star.png"));
    }

    public void render(SpriteBatch batch) {
        //TODO: make animation for this star
        int score = 0;
        for (Score s: level.getMap().getScores()) {
            if(s.isCollected()) score++;
        }


        switch (score) {
            case 0:
                batch.draw(unfilledStar, x, y, size, size);
                batch.draw(unfilledStar, x + size + 0.2f*size, y + 0.1f*size, size, size);
                batch.draw(unfilledStar, x + 2f*(size + 0.2f*size), y, size, size);
                break;
            case 1:
                batch.draw(filledStar, x, y, size, size);
                batch.draw(unfilledStar, x + size + 0.2f*size, y + 0.1f*size, size, size);
                batch.draw(unfilledStar, x + 2f*(size + 0.2f*size), y, size, size);
                break;
            case 2:
                batch.draw(filledStar, x, y, size, size);
                batch.draw(filledStar, x + size + 0.2f*size, y + 0.1f*size, size, size);
                batch.draw(unfilledStar, x + 2f*(size + 0.2f*size), y, size, size);
                break;
            case 3:
                batch.draw(filledStar, x, y, size, size);
                batch.draw(filledStar, x + size + 0.2f*size, y + 0.1f*size, size, size);
                batch.draw(filledStar, x + 2f*(size + 0.2f*size), y, size, size);
                break;
            default:
                throw new RuntimeException("the illegal value for score: " + score);
        }

    }

    public void dispose(){
        filledStar.dispose();
        unfilledStar.dispose();

        for (Score s : level.getMap().getScores()) s.dispose();
    }
}
