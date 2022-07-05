package com.frogger.game.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.utils.Audio;

import static com.frogger.game.utils.Util.*;

/**
 * TypeOfSkinChooser.java
 * @author vadympolishchuk
 * Screen that opens when player clicks on "Skins" button on Main Menu.
 * It helps player to choose what type of skin he wants to change, either
 * skin for character of for tiles (so it has two buttons)
 * Both buttons call SkinsScreen, just with different parameters (different skins to show)
 */

public class TypeOfSkinChooser extends Screen {


    public TypeOfSkinChooser(FroggerGame game) {
        super(game);
        createMenuButtons();
    }

    /**
     * Show two buttons (one for character skins and other for tiles skins)
     */
    @Override
    public void show() {
        super.show();

        setBackground();

        buttons.put("character", new TextButton("Character", textButtonStyles.get("yellow")));
        buttons.put("map", new TextButton("Map", textButtonStyles.get("yellow")));

        float distanceX = 0.1f*WINDOW_WIDTH;
        float startingX = WINDOW_WIDTH/2 - BUTTON_WIDTH - distanceX / 2;
        buttons.get("character").setBounds(startingX,0.5f*WINDOW_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.get("map").setBounds(startingX + distanceX + BUTTON_WIDTH,0.5f*WINDOW_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);

        buttons.get("character").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreen(new SkinsScreen(game, true));
            }
        });

        buttons.get("map").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreen(new SkinsScreen(game, false));
            }
        });

        stage.addActor(buttons.get("character"));
        stage.addActor(buttons.get("map"));
        stage.addActor(getBackButton(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 0.2f*WINDOW_HEIGHT, new MainMenuScreen(game)));
    }
}
