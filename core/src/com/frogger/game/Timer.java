package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Timer {

    private float timer;

    private BitmapFont font;

    private boolean isStopped;
    private boolean isReversed;
    private Label timeLabel;
    private Stage stage;
    private float animatingTime;


    public Timer(float x, float y, int timer, Stage stage) {
        font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        this.timer = timer;
        timeLabel = new Label("Time:", new Label.LabelStyle(font, Color.BLACK));
        timeLabel.setX(x);
        timeLabel.setY(stage.getHeight() - 1.07f*timeLabel.getHeight());
        this.stage = stage;
    }

    public void render(float delta, SpriteBatch batch) {
        if (!isStopped) timer += (isReversed) ? delta : -delta;
        if (timer < 0) timer = 0;
        if(!isReversed && timer < 5) {
            if(animatingTime <= 0) {
                timeLabel.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f), Actions.alpha(1), Actions.fadeOut(0.5f)));
                animatingTime = 1f;
            }else {
                animatingTime -= delta;
            }
        }
        timeLabel.setText("Time: " + convert(timer));
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

    public void show() {
        stage.addActor(timeLabel);
    }
}
