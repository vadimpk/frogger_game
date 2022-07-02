package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.frogger.game.FroggerGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.frogger.game.utils.Const.WINDOW_HEIGHT;
import static com.frogger.game.utils.Const.WINDOW_WIDTH;

/**
 * SplashScreen.java
 * @author stas-bukovskiy
 * Class of dsplaying game logo with scaling effect.
 * It used only once, on starting of game.
 */
public class SplashScreen extends Screen {
    private Image splashImg;

    /**
     * Basic constructor
     * @param game - FroggerGame instance
     */
    public SplashScreen(FroggerGame game) {
        super(game);
    }


    /**
     * Method adds logo image and add for it animation
     */
    @Override

    public void show() {
        Gdx.input.setInputProcessor(stage);

        Runnable transitionRunnable = new Runnable() {
            @Override
            public void run() {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        };

        Texture splashTex = new Texture(Gdx.files.internal("backgrounds/logo.png"));
        splashImg = new Image(splashTex);
        splashImg.setSize(0.3f*splashTex.getWidth(), 0.3f*splashTex.getHeight());
        splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight());
        splashImg.setPosition(stage.getWidth() / 2 - 32, stage.getHeight() + 32);
        splashImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - splashImg.getWidth() / 2, 0.7f*stage.getHeight() - splashImg.getHeight() / 2, 2f, Interpolation.swing)),
                delay(1.5f), fadeOut(1.25f), run(transitionRunnable)));

        stage.addActor(splashImg);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }
}