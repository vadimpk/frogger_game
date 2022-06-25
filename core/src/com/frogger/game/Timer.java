package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Timer {

    private float timer;

    private BitmapFont font;

    private float x, y;
    private boolean isStopped;
    private boolean isReversed;


    public Timer(float x, float y, int timer) {
        this.x = x;
        this.y = y;

        font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        this.timer = timer;

    }

    public void render(float delta, SpriteBatch batch) {
        if (!isStopped) timer += (isReversed) ? delta : -delta;
        if (timer < 0) timer = 0;
        font.draw(batch, "Time: " + getTime(), x, y);
    }

    public String getTime() {
        return convert(timer);
    }

    public static String convert(float timer) {
        int minutes = (int) ((timer % 3600) / 60);
        int seconds = (int) (timer % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

    public float getTimer() {
        return timer;
    }

    public void dispose() {
        font.dispose();
    }

    public void stop() {
        isStopped = true;
    }

    public void start() {
        isStopped = false;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean isReversed() {
        return isReversed;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }
}
