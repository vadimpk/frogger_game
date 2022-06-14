package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.frogger.game.Frog;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.Score;

import static com.frogger.game.Const.*;
import static com.frogger.game.Const.BUTTON_HEIGHT;

public class DieScreen extends Screen{

    private final Level currentLevel;

    public DieScreen(FroggerGame game, Level currentLevel) {
        super(game);
        this.currentLevel = currentLevel;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        float distanceX = 0.1f * WINDOW_WIDTH;
        float startingX = (WINDOW_WIDTH - 2 * BUTTON_WIDTH - distanceX) / 2;
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("buttons/back_btn.png"))));
        backButton.setBounds(startingX, WINDOW_HEIGHT / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        ImageButton restartButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("buttons/restart_btn.png"))));
        restartButton.setBounds(startingX + BUTTON_WIDTH + distanceX, WINDOW_HEIGHT / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });
        restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new FroggerGameScreen(game, currentLevel));
                for (Score score : currentLevel.getMap().getScores()) {
                    score.setUncollected();
                }
                FroggerGame.gameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        });

        stage.addActor(restartButton);
        stage.addActor(backButton);
    }
}
