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
    private boolean isStoped;

    public Timer(float x, float y) {
        this.x = x;
        this.y = y;

        font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        timer = 0;

    }

    public void render(float delta, SpriteBatch batch) {
        if (!isStoped) timer += delta;
        font.draw(batch, "Time: " + getTime(), x, y);
    }

    public String getTime() {
        int minutes = (int) ((timer % 3600) / 60);
        int seconds = (int) (timer % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void dispose() {
        font.dispose();
    }

    public void stop() {
        isStoped = true;
    }
}
