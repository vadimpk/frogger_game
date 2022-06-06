package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.TimeUtils;

import static com.frogger.game.FroggerGame.*;

public class FroggerGameScreen extends ScreenAdapter {

    private Level level;
    private FroggerGame game;
    private Level current;
    private Frog frog;




    public FroggerGameScreen(FroggerGame game, Level level) {
        this.game = game;
        this.level = level;
        frog = Map.getFrog();
    }

    @Override
    public void render(float delta) {
        level.render(delta);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


}
