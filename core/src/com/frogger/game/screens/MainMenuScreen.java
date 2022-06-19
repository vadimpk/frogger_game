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
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.LevelsGenerator;
import com.frogger.game.Map;

import static com.frogger.game.Const.*;

public class MainMenuScreen extends Screen {

    private TextButton.TextButtonStyle textGreenButtonStyle;
    private TextButton.TextButtonStyle textYellowButtonStyle;
    private TextButton.TextButtonStyle textRedButtonStyle;
    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private TextButton playButton;
    private TextButton levelsButton;
    private TextButton exitButton;

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
        font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"));
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.atlas"));
        skin.addRegions(buttonAtlas);
        textGreenButtonStyle = new TextButton.TextButtonStyle();
        textRedButtonStyle = new TextButton.TextButtonStyle();
        textYellowButtonStyle = new TextButton.TextButtonStyle();
        textGreenButtonStyle.font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        textRedButtonStyle.font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        textYellowButtonStyle.font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);

        textGreenButtonStyle.up = skin.getDrawable("green-btn-up");
        textGreenButtonStyle.down = skin.getDrawable("green-btn-down");
        textGreenButtonStyle.over = skin.getDrawable("green-btn-over");

        textRedButtonStyle.up = skin.getDrawable("red-btn-up");
        textRedButtonStyle.down = skin.getDrawable("red-btn-down");
        textRedButtonStyle.over = skin.getDrawable("red-btn-over");

        textYellowButtonStyle.up = skin.getDrawable("yellow-btn-up");
        textYellowButtonStyle.down = skin.getDrawable("yellow-btn-down");
        textYellowButtonStyle.over = skin.getDrawable("yellow-btn-over");

        //Create buttons
        playButton =  new TextButton("Play", textGreenButtonStyle);
        playButton.setBounds((WINDOW_WIDTH / 2) - (BUTTON_WIDTH / 2), 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *0.1f, BUTTON_WIDTH, BUTTON_HEIGHT);
        levelsButton = new TextButton("Levels", textYellowButtonStyle);
        levelsButton.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH /2,0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *1.7f , BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton = new TextButton("Exit", textRedButtonStyle);
        exitButton.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH /2, 0.65f * WINDOW_HEIGHT - BUTTON_HEIGHT *3.3f, BUTTON_WIDTH, BUTTON_HEIGHT);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Level level =  FroggerGame.levels[0];
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

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        skin.dispose();
        buttonAtlas.dispose();
    }
}
