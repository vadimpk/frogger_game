package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font100;
    private BitmapFont font36;
    private Skin skin;
    private TextureAtlas buttonAtlas;

    public DieScreen(FroggerGame game, Level currentLevel) {
        super(game);
        this.currentLevel = currentLevel;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        font36 = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"));
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.atlas"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font36;
        textButtonStyle.up = skin.getDrawable("green-btn-up");
        textButtonStyle.down = skin.getDrawable("green-btn-down");
        textButtonStyle.over = skin.getDrawable("green-btn-over");

        float distanceX = 0.1f * WINDOW_WIDTH;
        float startingX = (WINDOW_WIDTH - 2 * BUTTON_WIDTH - distanceX) / 2;
        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.setBounds(startingX, WINDOW_HEIGHT / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        TextButton restartButton = new TextButton("Restart", textButtonStyle);
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

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        font36.dispose();
        buttonAtlas.dispose();
    }
}
