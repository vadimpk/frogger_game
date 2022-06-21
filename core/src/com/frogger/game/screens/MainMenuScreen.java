package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.LevelsGenerator;

import java.util.HashMap;
import java.util.Map;

import static com.frogger.game.Const.*;

public class MainMenuScreen extends Screen {

    private final Map<String,  TextButton.TextButtonStyle> buttonStyles;
    private TextButton playButton;
    private TextButton levelsButton;
    private TextButton exitButton;

    public MainMenuScreen(FroggerGame game) {
        super(game);
        buttonStyles = new HashMap<>();
    }

    @Override
    public void show() {
        super.show();

        initButtons();

        //Add buttons to table
        stage.addActor(playButton);
        stage.addActor(levelsButton);
        stage.addActor(exitButton);

    }

    private void initButtons() {
        buttonStyles.put("green", new TextButton.TextButtonStyle());
        buttonStyles.put("yellow", new TextButton.TextButtonStyle());
        buttonStyles.put("red", new TextButton.TextButtonStyle());
        for (String key : buttonStyles.keySet()) {
            TextButton.TextButtonStyle buttonStyle = buttonStyles.get(key);
            buttonStyle.font = fonts.get("36");
            buttonStyle.up = skin.getDrawable(key + "-btn-up");
            buttonStyle.down = skin.getDrawable(key + "-btn-down");
            buttonStyle.over = skin.getDrawable(key + "-btn-over");
        }

        //Create buttons
        float startingX = (WINDOW_WIDTH / 2) - (BUTTON_WIDTH / 2);
        playButton =  new TextButton("Play", buttonStyles.get("green"));
        playButton.setBounds(startingX, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *0.1f, BUTTON_WIDTH, BUTTON_HEIGHT);
        levelsButton = new TextButton("Levels", buttonStyles.get("yellow"));
        levelsButton.setBounds(startingX,0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *1.7f , BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton = new TextButton("Exit", buttonStyles.get("red"));
        exitButton.setBounds(startingX, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *3.3f, BUTTON_WIDTH, BUTTON_HEIGHT);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new FroggerGameScreen(game, LevelsGenerator.getLevels()[0]), 0.3f);
            }
        });
        levelsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new LevelsScreen(game), 0.3f);
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
