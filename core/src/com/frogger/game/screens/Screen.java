package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frogger.game.FroggerGame;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public abstract class Screen implements com.badlogic.gdx.Screen {
    protected FroggerGame game;

    protected SpriteBatch batch;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected Map<String, BitmapFont> fonts;
    protected Skin skin;
    protected TextureAtlas buttonAtlas;
    protected Map<String, TextButton.TextButtonStyle> buttonStyles;
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

        fonts.put("36", new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt")));
        fonts.put("100", new BitmapFont(Gdx.files.internal("fonts/Pixellari_100.fnt")));
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.atlas"));
        skin.addRegions(buttonAtlas);
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
    }
}
