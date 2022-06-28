package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.LevelsGenerator;

import static com.frogger.game.Const.*;

public class MainMenuScreen extends Screen {

    private Texture bgTexture;
    private static Sound clickedSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click-sound.mp3"));
    private static boolean soundPlaying = false;

    public MainMenuScreen(FroggerGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        initButtons();

        //Add buttons to table
        stage.addActor(buttons.get("play"));
        stage.addActor(buttons.get("levels"));
        stage.addActor(buttons.get("exit"));
    }

    private void initButtons() {

        createMenuButtons();
        soundPlaying = false;

        //Create buttons
        float startingX = (WINDOW_WIDTH / 2) - (BUTTON_WIDTH / 2);
        buttons.put("play", new TextButton("Play", buttonStyles.get("green")));
        buttons.get("play").setBounds(startingX, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *0.1f, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.put("levels", new TextButton("Levels", buttonStyles.get("yellow")));
        buttons.get("levels").setBounds(startingX,0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *1.7f , BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.put("exit",  new TextButton("Exit", buttonStyles.get("red")));
        buttons.get("exit").setBounds(startingX, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *3.3f, BUTTON_WIDTH, BUTTON_HEIGHT);

        //Add listeners to buttons
        buttons.get("play").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new FroggerGameScreen(game, LevelsGenerator.getBigLevel()), 0.3f);
                if (!soundPlaying) {
                    clickedSound.play(1.0f);
                    soundPlaying = true;
                }
            }
        });
        buttons.get("levels").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new LevelsScreen(game), 0.3f);
                if (!soundPlaying) {
                    clickedSound.play(1.0f);
                    soundPlaying = true;
                }

            }
        });

        buttons.get("exit").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                if (!soundPlaying) {
                    clickedSound.play(1.0f);
                    soundPlaying = true;
                }
            }
        });
    }
}
