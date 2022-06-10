package com.frogger.game;

import java.io.Serializable;

public class Level{
    private int number;
    private int scores;
    private int lives;
    private Map map;

    public Level(int number, int scores, Map map) {
        this.number = number;
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void render(float delta) {
        map.render(delta);
    }
}
