package com.frogger.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.FroggerGame;
import com.frogger.game.Map;
import com.frogger.game.Util.Direction;

import java.sql.Time;

import static com.frogger.game.screens.FroggerGameScreen.level;

public class Train extends MovingObject {

    private boolean moving;
    private long deltaTime;
    private long startedMovingTime;
    private long startedMovingTime2;

    private static final Direction MOVING_DIRECTION = Direction.LEFT;
    private static final Texture HEAD_TEXTURE = new Texture(Gdx.files.internal("objects/train/train-head.png"));
    private static final Texture TEXTURE = new Texture(Gdx.files.internal("objects/train/train.png"));

    private Sound trainSound = Gdx.audio.newSound(Gdx.files.internal("sounds/train.mp3"));
    private boolean soundPlaying = false;


    private static final long TIME_BEFORE_FIRST_MOVE = 40000000;
    private static final long TIME_BETWEEN_MOVES     = 400000000;

    private static final float SPEED  = 1.5f;
    private static final int LENGTH   = 50;
    private static final boolean SAFE = false;

    private static final int MIN_TILES_BEFORE_MOVING = 3;
    private static final int MIN_TILES_AFTER_MOVING  = 10;

    public Train(float size, float x, float y) {
        super(size, x + size,
                y, SPEED, LENGTH, MOVING_DIRECTION);
        setSafe(SAFE);
    }

    public void render(SpriteBatch batch) {

        float frogY = Map.getFrog().getY();

        if (!moving) {
            if (getY() - frogY < MIN_TILES_BEFORE_MOVING * getSize()) {
                moving = true;
                startedMovingTime = TimeUtils.nanoTime();
                deltaTime = TIME_BEFORE_FIRST_MOVE;
            }
        }

        if (Math.abs(getY() - frogY) > MIN_TILES_AFTER_MOVING * getSize()) {
            moving = false;
        }

        if (moving) {
            if (TimeUtils.nanoTime() - startedMovingTime > 10 * deltaTime) {
                if (!soundPlaying) {
                    startedMovingTime2 = TimeUtils.nanoTime();
                    soundPlaying = true;
                    trainSound.play(0.5f);
                }

                if (TimeUtils.nanoTime() - startedMovingTime2 > 2 * deltaTime) {
                    move();

                    batch.draw(HEAD_TEXTURE, getX(), getY(), getSize(), getSize());
                    for (int i = 1; i < getLength(); i++) {
                        batch.draw(TEXTURE, getX() + i * getSize(), getY(), getSize(), getSize());
                    }
                }
            }
        }

    }

    public void pausedRender(SpriteBatch batch) {
        batch.draw(HEAD_TEXTURE, getX(), getY(), getSize(), getSize());
        for (int i = 1; i < getLength(); i++) {
            batch.draw(TEXTURE, getX() + i * getSize(), getY(), getSize(), getSize());
        }
    }

    @Override
    public void move() {

        if ((getX() + getSize()*getLength()) < level.getMap().getTiles()[0][0].getX()) {
            startedMovingTime = TimeUtils.nanoTime();
            soundPlaying = false;
            deltaTime = TIME_BETWEEN_MOVES;
            setX(level.getMap().getTiles()[0][level.getMap().getnColumns() -1].getX() + level.getMap().getTiles()[0][0].getSize());
        } else
            setX(getX() - getSize() / getSpeed());

    }


    public static void dispose() {
        TEXTURE.dispose();
        HEAD_TEXTURE.dispose();
    }
}
