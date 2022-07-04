package com.frogger.game.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.*;
import com.frogger.game.skins.CharacterSkin;
import com.frogger.game.skins.SkinPanel;
import com.frogger.game.utils.Audio;

import static com.frogger.game.utils.Const.*;
import static com.frogger.game.DataIO.*;

/**
 * SkinsScreen.java
 * @author stas-bukovskiy
 *
 * Class for screen for choosing map and character skins.
 * It gives player oppotunity to buy and select new skins for map or character
 */
public class SkinsScreen extends Screen {


    private SkinPanel skinPanel;

    /**
     * @param game - FroggerGame instance
     * @param characterSkins - true if player is choosing skin for character
     */
    public SkinsScreen(FroggerGame game, boolean characterSkins) {
        super(game);
        createButtonStyles();

        CharacterSkin[] skins = (characterSkins) ? getCharacterSkins() : getTileSkins();

        for (int i = 0; i < skins.length; i++) {
            if (skins[i].isActive()) {
                skinPanel = new SkinPanel(i, skins,stage);
            }
        }
    }

    /**
     * Method invokes show() method for skin panel and adds buttons and labels
     */
    @Override
    public void show() {
        super.show();

        setBackground();

        skinPanel.show();

        Button rightArrow = new Button(buttonStyles.get("arrow"));
        Button leftArrow = new Button(buttonStyles.get("arrow"));

        leftArrow.setBounds(0.2f * WINDOW_WIDTH + 0.1f * WINDOW_HEIGHT, WINDOW_HEIGHT * 0.55f, 0.1f * WINDOW_HEIGHT, 0.1f * WINDOW_HEIGHT);
        leftArrow.setTransform(true);
        leftArrow.setRotation(180);
        rightArrow.setBounds(0.8f * WINDOW_WIDTH - 0.1f * WINDOW_HEIGHT, WINDOW_HEIGHT * 0.45f, 0.1f * WINDOW_HEIGHT, 0.1f * WINDOW_HEIGHT);

        final TextButton buyButton = new TextButton("Buy", textButtonStyles.get("buy"));

        buyButton.setBounds((WINDOW_WIDTH - BUTTON_WIDTH) / 2, 0.2f * WINDOW_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        buyButton.setDisabled(defineBuyButtonAvailability());


        rightArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                skinPanel.nextSkin();
                buyButton.setDisabled(defineBuyButtonAvailability());
            }
        });

        leftArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                skinPanel.previousSkin();
                buyButton.setDisabled(defineBuyButtonAvailability());
            }
        });

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!defineBuyButtonAvailability()) Audio.playClickedSound();
                skinPanel.buySkin();
                buyButton.setDisabled(defineBuyButtonAvailability());
            }
        });

        stage.addActor(rightArrow);
        stage.addActor(leftArrow);
        stage.addActor(buyButton);
        stage.addActor(getBackButton(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 0.08f * WINDOW_HEIGHT, new TypeOfSkinChooser(game)));
    }

    /**
     * Method invokes render method for skin panel
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        skinPanel.render();
    }

    private boolean defineBuyButtonAvailability() {
        return (skinPanel.getCurrentSkin().isUnlocked() || DataIO.getStarNumber() < skinPanel.getCurrentSkin().getPrice());
    }

    /**
     * Method creates button styles
     */
    private void createButtonStyles() {
        TextButton.TextButtonStyle buyButtonStyle = new TextButton.TextButtonStyle();
        buyButtonStyle.font = fonts.get("36");
        buyButtonStyle.up = skin.getDrawable("yellow-btn-up");
        buyButtonStyle.down = skin.getDrawable("yellow-btn-down");
        buyButtonStyle.over = skin.getDrawable("yellow-btn-over");
        buyButtonStyle.disabled = skin.getDrawable("btn-disabled");


        Button.ButtonStyle rightButtonStyle = new Button.ButtonStyle();
        rightButtonStyle.up = skin.getDrawable("arrow-btn-up");
        rightButtonStyle.down = skin.getDrawable("arrow-btn-down");
        rightButtonStyle.over = skin.getDrawable("arrow-btn-over");
        rightButtonStyle.disabled = skin.getDrawable("arrow-btn-disabled");

        buttonStyles.put("arrow", rightButtonStyle);
        textButtonStyles.put("buy", buyButtonStyle);
    }

    /**
     * Method dispose all disposable instances
     */
    @Override
    public void dispose() {
        super.dispose();
        skinPanel.dispose();
    }
}

