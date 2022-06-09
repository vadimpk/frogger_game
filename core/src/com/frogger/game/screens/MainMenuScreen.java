package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.Map;

import static com.frogger.game.Const.*;

public class MainMenuScreen extends Screen {

    private ImageButton playButton;
    private ImageButton levelsButton;
    private ImageButton exitButton;

    public MainMenuScreen(FroggerGame game) {
        super(game);
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        initButtons();

        //Add buttons to table
        stage.addActor(playButton);
        stage.addActor(levelsButton);
        stage.addActor(exitButton);
    }

    private void initButtons() {
        //Create buttons
        playButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("buttons/play_btn.png"))));
        playButton.setBounds((WINDOW_WIDTH / 2) - (BUTTON_WIDTH / 2), 0.6f * WINDOW_HEIGHT - BUTTON_HEIGHT *0.2f, BUTTON_WIDTH, BUTTON_HEIGHT);
        levelsButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("buttons/level_btn.png"))));
        levelsButton.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH /2,0.6f * WINDOW_HEIGHT - BUTTON_HEIGHT *1.2f , BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("buttons/exit_btn.png"))));
        exitButton.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH /2, 0.6f * WINDOW_HEIGHT - BUTTON_HEIGHT *2.2f, BUTTON_WIDTH, BUTTON_HEIGHT);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Level level = game.loadLevelsFromFile("levels.txt")[0];
                ((Game)Gdx.app.getApplicationListener()).setScreen(new FroggerGameScreen(game, level));
            }
        });
            levelsButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelsScreen(game));
                }
            });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
}
