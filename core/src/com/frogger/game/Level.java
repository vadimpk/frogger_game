package com.frogger.game;

public class Level{
    private int number;
    private int bestScore;
    private int starScore;
    private Map map;

    public Level(int number, int bestScore, int starScore, Map map) {
        this.number = number;
        this.bestScore = bestScore;
        this.starScore = starScore;
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

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getStarScore() {
        return starScore;
    }

    public void setStarScore(int starScore) {
        this.starScore = starScore;
    }


    public void setMap(Map map) {
        this.map = map;
    }

    public void render(float delta) {
        map.render(delta);
    }
}
