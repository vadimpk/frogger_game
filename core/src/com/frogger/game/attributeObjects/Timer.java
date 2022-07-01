package com.frogger.game.attributeObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Timer.java
 * @author stas-bukovskiy
 * Class displays time that counts time and displays it
 */
public class Timer {

    private float timer;

    private final BitmapFont font;

    private boolean isStopped;
    private Label timeLabel;
    private Stage stage;
    private float animatingTime;

    /**
     * Constructor of Timer instance
     * @param x - starting x-coordinate of drawing
     * @param y - starting y-coordinate of drawing
     * @param timer - time that will be passing
     * @param stage - stage where timer will be displayed
     */
    public Timer(float x, float y, int timer, Stage stage) {
        font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        this.timer = timer;
        timeLabel = new Label("Time:", new Label.LabelStyle(font, Color.BLACK));
        timeLabel.setX(x);
        timeLabel.setY(stage.getHeight() - 1.07f*timeLabel.getHeight());
        this.stage = stage;
    }

    /**
     * Method draws time, counts passed time
     * @param delta
     */
    public void render(float delta) {
        if (!isStopped) timer -= delta;
        if (timer < 0) timer = 0;
        if(timer < 5) {
            if(animatingTime <= 0) {
                timeLabel.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f), Actions.alpha(1), Actions.fadeOut(0.5f)));
                animatingTime = 1f;
            }else {
                animatingTime -= delta;
            }
        }
        timeLabel.setText("Time: " + convert(timer));
    }

    /**
     * Method returns time in string representations
     * @return time in string representations
     */
    public String getTime() {
        return convert(timer);
    }

    /**
     * Method receives float time and converts it minutes and seconds string representation
     * @param timer - time in float
     * @return minutes and seconds string representation
     */
    public static String convert(float timer) {
        int minutes = (int) ((timer % 3600) / 60);
        int seconds = (int) (timer % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Method returns time in float
     * @return time in float
     */
    public float getTimer() {
        return timer;
    }

    /**
     * Method disposes font
     */
    public void dispose() {
        font.dispose();
    }

    /**
     * Method stops time counting
     */
    public void stop() {
        isStopped = true;
    }

    /**
     * Method starts time counting
     */
    public void start() {
        isStopped = false;
    }

    /**
     * Method returns if times is stopped
     * @return true if times is stopped otherwise false
     */
    public boolean isStopped() {
        return isStopped;
    }

    /**
     * Method add timer to stage
     */
    public void show() {
        stage.addActor(timeLabel);
    }
}
