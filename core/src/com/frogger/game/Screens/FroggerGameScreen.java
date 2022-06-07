package com.frogger.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.Frog;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.Map;

import static com.frogger.game.FroggerGame.*;

public class FroggerGameScreen extends ScreenAdapter {

    private Level level;
    private FroggerGame game;
    private Level current;
    private Frog frog;


    public FroggerGameScreen(FroggerGame game, Level level) {
        this.game = game;
        this.level = level;
        frog = level.getMap().getFrog();
    }

    @Override
    public void render(float delta) {
        level.render(delta);
        if (!frog.isAlive()) {
            ((Game)Gdx.app.getApplicationListener()).setScreen(new DieScreen(game, level));
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


}
