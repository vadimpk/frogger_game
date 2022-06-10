package com.frogger.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.Tile;
import com.frogger.game.Util;

public class Log extends MovingObject {

    private static final Texture DEFAULT_LOG_TEXTURE = new Texture(Gdx.files.internal("objects/log/log.png"));
    private static final Texture DEFAULT_LOG_TEXTURE_2 = new Texture(Gdx.files.internal("objects/log/log2.png"));
    private static final Texture DEFAULT_LOG_TEXTURE_3 = new Texture(Gdx.files.internal("objects/log/log3.png"));

    private static final Texture BREAKING_LOG_TEXTURE = new Texture(Gdx.files.internal("objects/log/log-breaking.png"));
    private static final Texture BREAKING_LOG_TEXTURE_2 = new Texture(Gdx.files.internal("objects/log/log-breaking-2.png"));
    private static final Texture BREAKING_LOG_TEXTURE_3 = new Texture(Gdx.files.internal("objects/log/log-breaking-3.png"));

    private static final Texture BREAKING_LOG_TEXTURE2 = new Texture(Gdx.files.internal("objects/log/log-breaking2.png"));
    private static final Texture BREAKING_LOG_TEXTURE2_2 = new Texture(Gdx.files.internal("objects/log/log-breaking2-2.png"));
    private static final Texture BREAKING_LOG_TEXTURE2_3 = new Texture(Gdx.files.internal("objects/log/log-breaking2-3.png"));

    private static final Texture SINGLE_LOG_TEXTURE = new Texture(Gdx.files.internal("objects/log/single-log.png"));
    private static final Texture SINGLE_LOG_TEXTURE_2 = new Texture(Gdx.files.internal("objects/log/single-log-2.png"));
    private static final Texture SINGLE_LOG_TEXTURE_3 = new Texture(Gdx.files.internal("objects/log/single-log-3.png"));

    private static final Texture FADING_LOG_TEXTURE_1 = new Texture(Gdx.files.internal("log.png"));
    private static final Texture FADING_LOG_TEXTURE_2 = new Texture(Gdx.files.internal("water.png"));

    /** fields for logs that are fading over time */
    private boolean fading = false;
    private int state;
    private long deltaTime;
    private long startTime;
    private Texture texture;

    public Log(float size, float x, float y, float speed, int length, Util.Direction direction) {
        super(size, x, y, speed, length, direction);
    }

    public Log(float size, float x, float y, float speed, int length, Util.Direction direction, boolean fading, long deltaTime) {
        super(size, x, y, speed, length, direction);
        this.fading = fading;
        this.deltaTime = deltaTime;
        state = 0;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch) {

        move();

        // if log is changing over time
        if (fading) {

            // every 10 * deltaTime seconds change the state
            if (TimeUtils.nanoTime() - startTime > 10 * deltaTime) {
                state = (state > 2) ? 0 : state + 1;
                startTime = TimeUtils.nanoTime();
            }
        }

        for (int i = 0; i < getLength(); i++) {

            switch (state) {
                case 0:
                    if (getLength() == 1) texture = SINGLE_LOG_TEXTURE;
                    else if (i == 0) texture = DEFAULT_LOG_TEXTURE_2;
                    else if (i == getLength() - 1) texture = DEFAULT_LOG_TEXTURE_3;
                    else texture = DEFAULT_LOG_TEXTURE;
                    batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
                    setSafe(true);
                    break;
                case 1:
                    if (getLength() == 1) texture = SINGLE_LOG_TEXTURE_2;
                    else if (i == 0) texture = BREAKING_LOG_TEXTURE_2;
                    else if (i == getLength() - 1) texture = BREAKING_LOG_TEXTURE_3;
                    else texture = BREAKING_LOG_TEXTURE;
                    batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
                    setSafe(true);
                    break;
                case 2:
                    if (getLength() == 1) texture = SINGLE_LOG_TEXTURE_3;
                    else if (i == 0) texture = BREAKING_LOG_TEXTURE2_2;
                    else if (i == getLength() - 1) texture = BREAKING_LOG_TEXTURE2_3;
                    else texture = BREAKING_LOG_TEXTURE2;
                    batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
                    setSafe(true);
                    break;
                case 3:
                    setSafe(false);
                    break;
            }
        }
    }


    public static void dispose() {
        DEFAULT_LOG_TEXTURE.dispose();
        FADING_LOG_TEXTURE_1.dispose();
        FADING_LOG_TEXTURE_2.dispose();
    }
}
