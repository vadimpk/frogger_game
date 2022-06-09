package com.frogger.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.Util;

public class Car extends MovingObject {

    private static final Texture DEFAULT_CAR_TEXTURE = new Texture(Gdx.files.internal("tile2.png"));
    private static final boolean SAFE = false;

    private static Texture texture;

    public Car(float size, float x, float y, float speed, int length, Util.Direction direction) {
        super(size, x, y, speed, length, direction);

        this.setSafe(SAFE);
        this.texture = DEFAULT_CAR_TEXTURE;
    }

    @Override
    public void render(SpriteBatch batch) {
        move();
        for (int i = 0; i < getLength(); i++) {
            batch.draw(texture, getX() + i * getSize(), getY(), getSize(), getSize());
        }
    }


    public static void dispose() {
        DEFAULT_CAR_TEXTURE.dispose();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
}
