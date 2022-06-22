package com.frogger.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.Util;

public class Log extends MovingObject {

    private static final Texture LOG_PACK = new Texture(Gdx.files.internal("objects/log/log-pack.png"));

    private static final TextureRegion DEFAULT_LOG_TEXTURE_1 = new TextureRegion(LOG_PACK, 0, 600, 150, 150);
    private static final TextureRegion DEFAULT_LOG_TEXTURE_2 = new TextureRegion(LOG_PACK, 150, 600, 150, 150);
    private static final TextureRegion DEFAULT_LOG_TEXTURE_3 = new TextureRegion(LOG_PACK, 300, 600, 150, 150);

    private static final TextureRegion BREAKING_LOG_TEXTURE_1 = new TextureRegion(LOG_PACK, 0, 750, 150, 150);
    private static final TextureRegion BREAKING_LOG_TEXTURE_2 = new TextureRegion(LOG_PACK, 150, 750, 150, 150);
    private static final TextureRegion BREAKING_LOG_TEXTURE_3 = new TextureRegion(LOG_PACK, 300, 750, 150, 150);

    private static final TextureRegion BREAKING_LOG_TEXTURE2_1 = new TextureRegion(LOG_PACK, 0, 900, 150, 150);
    private static final TextureRegion BREAKING_LOG_TEXTURE2_2 = new TextureRegion(LOG_PACK, 150, 900, 150, 150);
    private static final TextureRegion BREAKING_LOG_TEXTURE2_3 = new TextureRegion(LOG_PACK, 300, 900, 150, 150);

    private static final TextureRegion SINGLE_LOG_TEXTURE_1 = new TextureRegion(LOG_PACK, 0, 1050, 150, 150);
    private static final TextureRegion SINGLE_LOG_TEXTURE_2 = new TextureRegion(LOG_PACK, 150, 1050, 150, 150);
    private static final TextureRegion SINGLE_LOG_TEXTURE_3 = new TextureRegion(LOG_PACK, 300, 1050, 150, 150);

    private static final TextureRegion FLO0DED_DEFAULT_LOG_TEXTURE_1 = new TextureRegion(LOG_PACK, 0, 0, 150, 150);
    private static final TextureRegion FLO0DED_DEFAULT_LOG_TEXTURE_2 = new TextureRegion(LOG_PACK, 150, 0, 150, 150);
    private static final TextureRegion FLO0DED_DEFAULT_LOG_TEXTURE_3 = new TextureRegion(LOG_PACK, 300, 0, 150, 150);

    private static final TextureRegion FLO0DED_BREAKING_LOG_TEXTURE_1 = new TextureRegion(LOG_PACK, 0, 150, 150, 150);
    private static final TextureRegion FLO0DED_BREAKING_LOG_TEXTURE_2 = new TextureRegion(LOG_PACK, 150, 150, 150, 150);
    private static final TextureRegion FLO0DED_BREAKING_LOG_TEXTURE_3 = new TextureRegion(LOG_PACK, 300, 150, 150, 150);

    private static final TextureRegion FLO0DED_BREAKING_LOG_TEXTURE2_1 = new TextureRegion(LOG_PACK, 0, 300, 150, 150);
    private static final TextureRegion FLO0DED_BREAKING_LOG_TEXTURE2_2 = new TextureRegion(LOG_PACK, 150, 300, 150, 150);
    private static final TextureRegion FLO0DED_BREAKING_LOG_TEXTURE2_3 = new TextureRegion(LOG_PACK, 300, 300, 150, 150);

    private static final TextureRegion FLO0DED_SINGLE_LOG_TEXTURE_1 = new TextureRegion(LOG_PACK, 0, 450, 150, 150);
    private static final TextureRegion FLO0DED_SINGLE_LOG_TEXTURE_2 = new TextureRegion(LOG_PACK, 150, 450, 150, 150);
    private static final TextureRegion FLO0DED_SINGLE_LOG_TEXTURE_3 = new TextureRegion(LOG_PACK, 300, 450, 150, 150);

    /** fields for logs that are fading over time */
    private boolean fading = false;
    private int state;
    private long deltaTime;
    private long startTime;
    private TextureRegion texture;
    private boolean flooded;

    public Log(float size, float x, float y, float speed, int length, Util.Direction direction) {
        super(size, x, y, speed, length, direction);
    }

    public Log(float size, float x, float y, float speed, int length, Util.Direction direction, boolean fading, long deltaTime) {
        super(size, x, y, speed, length, direction);
        this.fading = fading;
        this.deltaTime = deltaTime;
        state = 0;
        startTime = TimeUtils.nanoTime();
        flooded = false;
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
                    if (getLength() == 1) {
                        texture = SINGLE_LOG_TEXTURE_1;
                        if (flooded) texture = FLO0DED_SINGLE_LOG_TEXTURE_1;
                    }
                    else if (i == 0) {
                        texture = DEFAULT_LOG_TEXTURE_2;
                        if (flooded) texture = FLO0DED_DEFAULT_LOG_TEXTURE_2;
                    }
                    else if (i == getLength() - 1) {
                        texture = DEFAULT_LOG_TEXTURE_3;
                        if (flooded) texture = FLO0DED_DEFAULT_LOG_TEXTURE_3;
                    }
                    else {
                        texture = DEFAULT_LOG_TEXTURE_1;
                        if (flooded) texture = FLO0DED_DEFAULT_LOG_TEXTURE_1;
                    }
                    batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
                    setSafe(true);
                    break;
                case 1:
                    if (getLength() == 1) {
                        texture = SINGLE_LOG_TEXTURE_2;
                        if (flooded) texture = FLO0DED_SINGLE_LOG_TEXTURE_2;
                    }
                    else if (i == 0) {
                        texture = BREAKING_LOG_TEXTURE_2;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE_2;
                    }
                    else if (i == getLength() - 1) {
                        texture = BREAKING_LOG_TEXTURE_3;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE_3;
                    }
                    else {
                        texture = BREAKING_LOG_TEXTURE_1;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE_1;
                    }
                    batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
                    setSafe(true);
                    break;
                case 2:
                    if (getLength() == 1) {
                        texture = SINGLE_LOG_TEXTURE_3;
                        if (flooded) texture = FLO0DED_SINGLE_LOG_TEXTURE_3;
                    }
                    else if (i == 0) {
                        texture = BREAKING_LOG_TEXTURE2_2;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE2_2;
                    }
                    else if (i == getLength() - 1) {
                        texture = BREAKING_LOG_TEXTURE2_3;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE2_3;
                    }
                    else {
                        texture = BREAKING_LOG_TEXTURE2_1;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE2_1;
                    }
                    batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
                    setSafe(true);
                    break;
                case 3:
                    setSafe(false);
                    break;
            }
        }
    }

    @Override
    public void pausedRender(SpriteBatch batch) {
        for (int i = 0; i < getLength(); i++) {

            switch (state) {
                case 0:
                    if (getLength() == 1) {
                        texture = SINGLE_LOG_TEXTURE_1;
                        if (flooded) texture = FLO0DED_SINGLE_LOG_TEXTURE_1;
                    } else if (i == 0) {
                        texture = DEFAULT_LOG_TEXTURE_2;
                        if (flooded) texture = FLO0DED_DEFAULT_LOG_TEXTURE_2;
                    } else if (i == getLength() - 1) {
                        texture = DEFAULT_LOG_TEXTURE_3;
                        if (flooded) texture = FLO0DED_DEFAULT_LOG_TEXTURE_3;
                    } else {
                        texture = DEFAULT_LOG_TEXTURE_1;
                        if (flooded) texture = FLO0DED_DEFAULT_LOG_TEXTURE_1;
                    }
                    batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
                    setSafe(true);
                    break;
                case 1:
                    if (getLength() == 1) {
                        texture = SINGLE_LOG_TEXTURE_2;
                        if (flooded) texture = FLO0DED_SINGLE_LOG_TEXTURE_2;
                    } else if (i == 0) {
                        texture = BREAKING_LOG_TEXTURE_2;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE_2;
                    } else if (i == getLength() - 1) {
                        texture = BREAKING_LOG_TEXTURE_3;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE_3;
                    } else {
                        texture = BREAKING_LOG_TEXTURE_1;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE_1;
                    }
                    batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
                    setSafe(true);
                    break;
                case 2:
                    if (getLength() == 1) {
                        texture = SINGLE_LOG_TEXTURE_3;
                        if (flooded) texture = FLO0DED_SINGLE_LOG_TEXTURE_3;
                    } else if (i == 0) {
                        texture = BREAKING_LOG_TEXTURE2_2;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE2_2;
                    } else if (i == getLength() - 1) {
                        texture = BREAKING_LOG_TEXTURE2_3;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE2_3;
                    } else {
                        texture = BREAKING_LOG_TEXTURE2_1;
                        if (flooded) texture = FLO0DED_BREAKING_LOG_TEXTURE2_1;
                    }
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
        LOG_PACK.dispose();
    }

    public void setFlooded(boolean flooded) {
        this.flooded = flooded;
    }
}
