package com.frogger.game.utils;

import com.badlogic.gdx.Gdx;

public class Util {
    public enum Direction {UP,DOWN,LEFT,RIGHT,NONE}
    public enum TypeOfRow {STATIC,LOG,CAR,TRAIN, LILY}
    public enum TypeOfTile {DEFAULT, NOT_TRANSIENT}
    public enum Character {FROG, CRAB, BIRD, EGG, BOTTLE_OF_COKE, BOTTLE_OF_WINE, FISH, PIZZA}
    public enum TileSkin {OAK_FOREST, FIR_FOREST, BEACH, DARK_FOREST}
    public static final float WINDOW_WIDTH = Gdx.graphics.getWidth();
    public static final float WINDOW_HEIGHT = Gdx.graphics.getHeight();
    public static final float BUTTON_HEIGHT = WINDOW_HEIGHT * 0.1f;
    public static final float BUTTON_WIDTH = BUTTON_HEIGHT * 3f;
}
