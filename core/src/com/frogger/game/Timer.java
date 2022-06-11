package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Timer {

    private float timer;

    private BitmapFont font;

    private float x, y;

    public Timer(float x, float y) {
        this.x = x;
        this.y = y;

        font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        timer = 0;

    }

    public void render(float delta, SpriteBatch batch) {
        timer += delta;

        int minutes = (int) ((timer % 3600) / 60);
        int seconds = (int) (timer % 60);

        String timeString = String.format("%02d:%02d", minutes, seconds);

        font.draw(batch, "Time: " + timeString, x, y);
    }

    public void dispose() {
        font.dispose();
    }
}
