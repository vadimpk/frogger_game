package com.frogger.game;

import com.badlogic.gdx.graphics.Texture;

public class Logs extends MovableRow {

    public Logs(Tile startingTile, float speed, float phase, float amountOfMovingObj, int movingObjLength, float movingObjHeight, Texture movingObjTexture, Texture bg) {
        super(startingTile, speed, phase, amountOfMovingObj, movingObjLength, movingObjHeight, movingObjTexture, bg);
    }

    public void update(float dt, Frog frog) {
        super.update(dt);
        if(!isCollision(frog) && frog.getY() == y) {
//            frog.setAlive(false);
        }
        else if (isCollision(frog)) {
            MovingObj collider = getCollider(frog);
            if(collider != null) {
                float size = collider.width / movingObjLength;
                for(int i = movingObjLength; i >= 0 ; i--) {
                    if(frog.getX() > collider.x + size*i) {
                        frog.setX(collider.x + size * i);
//                        frog.setTile();
                        collider.isFrogOn = true;
                    }
                }
            }
        }
        for (MovingObj movingObj : movingObjs) {
            if(movingObj.isFrogOn) frog.setX(frog.getX() + speed);
        }
        if(frog.getY() != y) for (MovingObj movingObj : movingObjs) movingObj.isFrogOn =false;
    }

    @Override
    public boolean isCollision(Frog frog) {
        if(frog.getY() == y) {
            for (MovingObj movingObj : movingObjs) {
                if (movingObj.x < frog.getX() && movingObj.x + movingObj.width > frog.getX() + frog.getSize()){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public MovingObj getCollider(Frog frog) {
        for (MovingObj movingObj : movingObjs) {
            if (movingObj.x < frog.getX() && movingObj.x + movingObj.width > frog.getX() + frog.getSize()){
                return movingObj;
            }
        }
        return null;
    }
}

