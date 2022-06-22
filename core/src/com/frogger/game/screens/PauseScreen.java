package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;

import java.util.HashMap;

import static com.frogger.game.Const.*;

public class PauseScreen extends Screen{
    private final FroggerGameScreen gameScreen;
    Texture bgTexture;
    public PauseScreen(FroggerGame game, FroggerGameScreen gameScreen) {
        super(game);
        this.gameScreen = gameScreen;
        buttonStyles = new HashMap<>();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        createMenuButtons();

        //Create buttons
        float startingX = (WINDOW_WIDTH / 2) - (BUTTON_WIDTH / 2);
        buttons.put("resume", new TextButton("Resume", buttonStyles.get("green")));
        buttons.get("resume").setBounds(startingX, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *0.1f, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.put("restart", new TextButton("Restart", buttonStyles.get("yellow")));
        buttons.get("restart").setBounds(startingX,0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *1.7f , BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.put("back",  new TextButton("Back", buttonStyles.get("red")));
        buttons.get("back").setBounds(startingX, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *3.3f, BUTTON_WIDTH, BUTTON_HEIGHT);

        //Add listeners to buttons
        buttons.get("resume").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FroggerGameScreen.isPaused = !FroggerGameScreen.isPaused;
                switchScreenWithFading(gameScreen, 0f);
            }
        });
        buttons.get("restart").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new FroggerGameScreen(game, FroggerGameScreen.level), 0.3f);
            }
        });

        buttons.get("back").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new MainMenuScreen(game), 0.3f);
            }
        });

        bgTexture = new Texture(Gdx.files.internal("bg.png"));
        Image bg = new Image(bgTexture);
        bg.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.addActor(bg);
        stage.addActor(buttons.get("resume"));
        stage.addActor(buttons.get("restart"));
        stage.addActor(buttons.get("back"));


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_ALPHA_BITS);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            FroggerGameScreen.isPaused = !FroggerGameScreen.isPaused;
            switchScreenWithFading(gameScreen, 0f);
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bgTexture.dispose();
    }
}
