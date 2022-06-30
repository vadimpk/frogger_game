package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frogger.game.utils.Audio;
import com.frogger.game.FroggerGame;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.frogger.game.utils.Const.*;

public abstract class Screen implements com.badlogic.gdx.Screen {
    protected FroggerGame game;

    protected SpriteBatch batch;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected Map<String, BitmapFont> fonts;
    protected Skin skin;
    protected TextureAtlas buttonAtlas;
    protected Map<String, TextButton.TextButtonStyle> textButtonStyles;
    protected Map<String, Button.ButtonStyle> buttonStyles;
    protected Map<String, TextButton> buttons;
    protected boolean isSwitching;

    public Screen(FroggerGame game) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);

        fonts = new HashMap<>();
        buttons = new HashMap<>();

        fonts.put("24", new BitmapFont(Gdx.files.internal("fonts/Pixellari_24.fnt")));
        fonts.put("36", new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt")));
        fonts.put("100", new BitmapFont(Gdx.files.internal("fonts/Pixellari_100.fnt")));
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.atlas"));
        skin.addRegions(buttonAtlas);
        textButtonStyles = new HashMap<>();
        buttonStyles = new HashMap<>();

        isSwitching = false;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        for (BitmapFont font : fonts.values()) font.dispose();
        skin.dispose();
        buttonAtlas.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.getRoot().addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.3f)));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    public void switchScreenWithFading(final Screen newScreen, float duration) {
        stage.getRoot().getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(fadeOut(duration));
        sequenceAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                ((Game)Gdx.app.getApplicationListener()).setScreen(newScreen);
            }
        }));
        stage.getRoot().addAction(sequenceAction);
    }



    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


    public void createMenuButtons(){
        textButtonStyles.put("green", new TextButton.TextButtonStyle());
        textButtonStyles.put("yellow", new TextButton.TextButtonStyle());
        textButtonStyles.put("red", new TextButton.TextButtonStyle());
        for (String key : new String[]{"green", "yellow", "red"}) {
            TextButton.TextButtonStyle buttonStyle = textButtonStyles.get(key);
            buttonStyle.font = fonts.get("36");
            buttonStyle.up = skin.getDrawable(key + "-btn-up");
            buttonStyle.down = skin.getDrawable(key + "-btn-down");
            buttonStyle.over = skin.getDrawable(key + "-btn-over");
        }

        Button.ButtonStyle soundsStyle = new Button.ButtonStyle();
        soundsStyle.up = skin.getDrawable("sound-on-btn-up");
        soundsStyle.down = skin.getDrawable("sound-on-btn-down");
        soundsStyle.over = skin.getDrawable("sound-on-btn-over");
        soundsStyle.checked = skin.getDrawable("sound-off-btn-up");
        soundsStyle.checkedOver = skin.getDrawable("sound-off-btn-over");
        soundsStyle.checkedDown = skin.getDrawable("sound-off-btn-down");
        buttonStyles.put("sounds", soundsStyle);
    }

    public TextButton getBackButton(float x, float y) {
        if(!textButtonStyles.containsKey("red")) createMenuButtons();
        TextButton backButton = new TextButton("Back", textButtonStyles.get("red"));
        backButton.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreenWithFading(new MainMenuScreen(game), 0.3f);
            }
        });
        return backButton;
    }
}
