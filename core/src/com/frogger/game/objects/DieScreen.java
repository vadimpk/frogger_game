package com.frogger.game.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frogger.game.FroggerGame;
import com.frogger.game.FroggerGameScreen;
import com.frogger.game.MainMenuScreen;

public class DieScreen extends ScreenAdapter {

    private FroggerGame game;

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    public DieScreen(FroggerGame game) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float btnWidth = width * 0.15f;
        float btnHeight = height * 0.15f;

        ImageButton button = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("btn.png"))));
        button.setBounds(width / 2 - btnWidth /2,0.6f * height - btnHeight*1.2f , btnWidth, btnHeight);

        //Add listeners to buttons
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(button);

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
