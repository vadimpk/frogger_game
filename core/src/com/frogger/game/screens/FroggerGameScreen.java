package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.frogger.game.Frog;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;

public class FroggerGameScreen extends ScreenAdapter {

    public static Level level;
    private FroggerGame game;


    public FroggerGameScreen(FroggerGame game, Level level) {
        this.game = game;
        FroggerGameScreen.level = level;
        Frog.get().respawn(level.getMap().getTiles()[0][level.getMap().getnColumns() / 2]);

    }

    @Override
    public void render(float delta) {
        level.render(delta);
        if (!Frog.get().isAlive()) {
            ((Game)Gdx.app.getApplicationListener()).setScreen(new DieScreen(game, level));
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


}
