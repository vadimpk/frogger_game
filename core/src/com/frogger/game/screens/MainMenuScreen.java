package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.LevelsGenerator;

import static com.frogger.game.Const.*;

public class MainMenuScreen extends Screen {

    public static boolean IS_SOUNDS_ON = true;

    private Texture bgTexture;
    private Button soundsButton;

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
        stage.addActor(soundsButton);

    }

    private void initButtons() {
       createMenuButtons();
        //Create buttons
        float startingX = (WINDOW_WIDTH / 2) - (BUTTON_WIDTH / 2);
        soundsButton = new Button(buttonStyles.get("sounds"));
        soundsButton.setBounds(0.05f* WINDOW_HEIGHT, 0.85f*WINDOW_HEIGHT, BUTTON_HEIGHT, BUTTON_HEIGHT);

        buttons.put("play", new TextButton("Play", textButtonStyles.get("green")));
        buttons.get("play").setBounds(startingX, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *0.1f, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.put("levels", new TextButton("Levels", textButtonStyles.get("yellow")));
        buttons.get("levels").setBounds(startingX,0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *1.7f , BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.put("exit",  new TextButton("Exit", textButtonStyles.get("red")));
        buttons.get("exit").setBounds(startingX, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *3.3f, BUTTON_WIDTH, BUTTON_HEIGHT);

        //Add listeners to buttons
        buttons.get("play").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new FroggerGameScreen(game, LevelsGenerator.getBigLevel()), 0.3f);
            }
        });
        buttons.get("levels").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new LevelsScreen(game), 0.3f);
            }
        });

        buttons.get("exit").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        soundsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                IS_SOUNDS_ON = !IS_SOUNDS_ON;
            }
        });
    }
}
