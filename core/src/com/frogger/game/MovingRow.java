package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import static com.frogger.game.FroggerGame.rows;
import static com.frogger.game.FroggerGame.tilesPerRow;

public class MovingRow extends Row{

    private float width, startingX;


    private float speed, phase, distance;
    private int amount, length;
    private TileGroup[] tileGroups;
    private Row bg;

    public MovingRow(int rowIndex, String tileSrc, String bgSrc, float speed, float phase, int amount, int length) {
        super(rowIndex, tileSrc);
        this.bg = new StaticRow(rowIndex, bgSrc);
        this.speed = speed;
        this.phase = phase;
        this.amount = amount;
        this.length = length;
        this.tiles = new Tile[amount * length];
        this.tileGroups = new TileGroup[amount];

        int counter = 0;
        for(int i = 0; i < amount; i++) {
            Tile[] tilesForGroup = new Tile[amount];
            for(int j = 0; j < length; j++) {
                Tile tile = new Tile(tilesPerRow, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), counter, rowIndex);
                tiles[counter] = tile;
                tilesForGroup[j] = tile;
                counter++;
            }
            tileGroups[i] = new TileGroup(tilesForGroup);
        }

        float tileSize =  tiles[0].getSize();
        width =  (tileSize * tilesPerRow);
        float movingObjWidth = tileGroups[0].width;

        if (width - amount * movingObjWidth < 0) throw new IllegalArgumentException("distance between moving objects smaller than 0");
        distance = (width - amount * movingObjWidth) / amount;
        startingX = (float) Gdx.graphics.getWidth() / 2 - width / 2;
        for(int i = 0; i < amount; i++) {
            tileGroups[i].x = startingX - movingObjWidth - (distance + movingObjWidth)*i ;
            tileGroups[i].y = tiles[0].getY();
        }
        System.out.println(distance);
        System.out.println(startingX);
    }

    @Override
    public Tile[] getTiles() {
        return bg.getTiles();
    }

    @Override
    public void render(SpriteBatch batch) {
        bg.render(batch);

        for (TileGroup tileGroup: tileGroups) {
            tileGroup.render(batch, texture);
        }

//        for (TileGroup tileGroup: tileGroups) {
//            if(tileGroup.x >= startingX && tileGroup.x + tileGroup.width <= startingX + width)
//                tileGroup.render(batch, texture);
//            else if(tileGroup.x < startingX && tileGroup.x + tileGroup.width > startingX)
//                tileGroup.render(batch, texture, startingX, tileGroup.x + tileGroup.width - startingX);
//            else if(tileGroup.x < startingX + width && tileGroup.x + tileGroup.width > startingX + width)
//                tileGroup.render(batch, texture, tileGroup.x, tileGroup.width - (tileGroup.x + tileGroup.width - startingX -width));
//        }
    }

    @Override
    protected void update(float dt) {
        for (TileGroup tileGroup: tileGroups) {
            if(tileGroup.x > startingX + width) tileGroup.x = startingX - (tileGroup.width);
            tileGroup.x += speed;
        }
    }

    private static class TileGroup {
        Tile[] tiles;
        float width, height, x, y;

        public TileGroup(Tile[] tiles) {
            this.tiles = tiles;
            width = tiles.length * tiles[0].getSize();
            height = tiles[0].getSize();
        }


        public void render(SpriteBatch batch, Texture texture) {
            batch.draw(texture, x, y, width, height);
        }

        public void render(SpriteBatch batch, Texture texture, float x, float width) {
            batch.draw(texture, x, y, width, height);
        }
    }
}
