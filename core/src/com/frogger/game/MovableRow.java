package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

import static com.frogger.game.FroggerGame.tilesPerRow;

public abstract class MovableRow{

    protected float startingX;

    protected float speed;
    protected float phase;
    protected float amountOfMovingObj;
    protected float distance;
    protected List<MovingObj> movingObjs;


    protected float x, y;
    protected float width, height;

    protected Texture bg;

    public MovableRow(Tile startingTile, float speed, float phase, float amountOfMovingObj, float movingObjWidth, float movingObjHeight, Texture movingObjTexture, Texture bg) {
        this.x = startingTile.getX();
        this.y = startingTile.getY();
        this.width = (int) (startingTile.getSize() * tilesPerRow);
        this.height = (int) startingTile.getSize();
        this.speed = speed;
        this.phase = phase;
        this.amountOfMovingObj = amountOfMovingObj;
        this.bg = bg;

        if (width - amountOfMovingObj * movingObjWidth < 0) throw new IllegalArgumentException("distance between moving objects smaller than 0");
        distance = (2 * movingObjWidth + width - amountOfMovingObj * movingObjWidth) / amountOfMovingObj;
        movingObjs = new ArrayList<>();
        startingX = (float) Gdx.graphics.getWidth() / 2 - width / 2;
        for(int i = 0; i < amountOfMovingObj; i++) {
            movingObjs.add(new MovingObj( startingX - (movingObjWidth + distance) * i, (y + height / 2 - movingObjHeight / 2), movingObjWidth, movingObjHeight, movingObjTexture));
        }
    }

    public void update(float dt) {
        for (MovingObj movingObj: movingObjs) {
            if(movingObj.x > startingX + width) movingObj.x = startingX - (movingObj.width + distance);
            movingObj.x += speed;
        }
    }

    public void render(SpriteBatch batch){
        batch.draw(bg, x, y, width, height);
        for (MovingObj movingObj: movingObjs) {
            if(movingObj.x >= startingX && movingObj.x + movingObj.width <= startingX + width)
                batch.draw(movingObj.texture, movingObj.x, movingObj.y, movingObj.width, movingObj.height);
            else if(movingObj.x < startingX && movingObj.x + movingObj.width > startingX)
                batch.draw(movingObj.texture, startingX, movingObj.y, movingObj.x +movingObj.width - startingX, movingObj.height);
            else if(movingObj.x < startingX + width && movingObj.x + movingObj.width > startingX + width)
                batch.draw(movingObj.texture, movingObj.x, movingObj.y, movingObj.width - (movingObj.x + movingObj.width - startingX -width), movingObj.height);
        }
    }

    public boolean isCollision(Frog frog) {
        if(frog.getY() == y) {
            for (MovingObj movingObj : movingObjs) {
                if (movingObj.x < frog.getX() && movingObj.x + movingObj.width > frog.getX() + frog.getSize()){
                    System.out.println("У мене є колізія!");
                    return true;
                }
            }
        }
        return false;
    }

    protected static class MovingObj {
        float x, y;
        float width, height;
        Texture texture;

        public MovingObj(float x, float y, float width, float height, Texture texture) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.texture = texture;
        }
    }
}
