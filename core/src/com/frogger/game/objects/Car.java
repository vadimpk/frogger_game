package com.frogger.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.frogger.game.Util;

import java.util.Random;

public class Car extends MovingObject {

    private static final Texture CAR_PACK = new Texture(Gdx.files.internal("objects/car/cars.png"));

    private static final TextureRegion CAR_HEAD_TEXTURE_1 = new TextureRegion(CAR_PACK, 0, 0, 60, 75);
    private static final TextureRegion CAR_TAIL_TEXTURE_1 = new TextureRegion(CAR_PACK, 0, 75, 60, 75);
    private static final TextureRegion CAR_HEAD_TEXTURE_2 = new TextureRegion(CAR_PACK, 0, 150, 60, 75);
    private static final TextureRegion CAR_TAIL_TEXTURE_2 = new TextureRegion(CAR_PACK, 0, 225, 60, 75);
    private static final TextureRegion CAR_HEAD_TEXTURE_3 = new TextureRegion(CAR_PACK, 0, 300, 60, 75);
    private static final TextureRegion CAR_TAIL_TEXTURE_3 = new TextureRegion(CAR_PACK, 0, 375, 60, 75);


    private static final boolean SAFE = false;

    private TextureRegion textureHead;
    private TextureRegion textureTail;
    private int textureRotation;

    public Car(float size, float x, float y, float speed, int length, Util.Direction direction) {
        super(size, x, y, speed, length, direction);

        this.setSafe(SAFE);
        Random rand = new Random();
        int t = rand.nextInt(3);
        if (t == 0) {
            textureHead = CAR_HEAD_TEXTURE_1;
            textureTail = CAR_TAIL_TEXTURE_1;
        }
        if (t == 1) {
            textureHead = CAR_HEAD_TEXTURE_2;
            textureTail = CAR_TAIL_TEXTURE_2;
        }
        if (t == 2) {
            textureHead = CAR_HEAD_TEXTURE_3;
            textureTail = CAR_TAIL_TEXTURE_3;
        }

        if (direction == Util.Direction.LEFT) textureRotation = 0;
        else textureRotation = 180;
    }

    @Override
    public void render(SpriteBatch batch) {
        move();
        if (getDirection() == Util.Direction.LEFT) {
            batch.draw(textureHead, getX(), getY(), getSize()/2, getSize()/2, getSize(), getSize(),1,1, textureRotation);
            batch.draw(textureTail, getX()+ getSize(), getY(), getSize()/2, getSize()/2, getSize(), getSize(),1,1, textureRotation);
        } else {
            batch.draw(textureTail, getX(), getY(), getSize()/2, getSize()/2, getSize(), getSize(),1,1, textureRotation);
            batch.draw(textureHead, getX()+ getSize(), getY(), getSize()/2, getSize()/2, getSize(), getSize(),1,1, textureRotation);

        }
    }


    public static void dispose() {
        CAR_PACK.dispose();
    }
}
