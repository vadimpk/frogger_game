package com.frogger.game.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.*;
import com.frogger.game.skins.SkinPanel;
import com.frogger.game.utils.Audio;

import static com.frogger.game.utils.Const.*;
import static com.frogger.game.DataIO.*;

public class SkinsScreen extends Screen {

    private SkinPanel skinPanel;

    public SkinsScreen(FroggerGame game) {
        super(game);
        createButtonStyles();
    }

    @Override
    public void show() {
        super.show();

        for (int i = 0; i < getSkins().length; i++) {
            if (getSkins()[i].isActive()) {
                skinPanel = new SkinPanel(i);
            }
        }


        TextButton rightArrow = new TextButton("", textButtonStyles.get("right-arrow"));
        TextButton leftArrow = new TextButton("", textButtonStyles.get("left-arrow"));

        leftArrow.setBounds(0.2f * WINDOW_WIDTH, WINDOW_HEIGHT * 0.45f, 0.1f * WINDOW_HEIGHT, 0.1f * WINDOW_HEIGHT);

        rightArrow.setBounds(0.8f * WINDOW_WIDTH - 0.1f * WINDOW_HEIGHT, WINDOW_HEIGHT * 0.45f, 0.1f * WINDOW_HEIGHT, 0.1f * WINDOW_HEIGHT);

        final TextButton buyButton = new TextButton("Buy", textButtonStyles.get("buy"));

        buyButton.setBounds((WINDOW_WIDTH - BUTTON_WIDTH) / 2, 0.2f * WINDOW_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        buyButton.setDisabled(defineBuyButtonAvailability());


        rightArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skinPanel.nextSkin();
                buyButton.setDisabled(defineBuyButtonAvailability());
            }
        });

        leftArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
        stage.addActor(getBackButton(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 0.08f * WINDOW_HEIGHT));
    }

    @Override
    public void render(float delta) {

        skinPanel.render();

        stage.act();
        stage.draw();
    }

    private boolean defineBuyButtonAvailability() {
        return skinPanel.getSkin().isUnlocked() || DataIO.getStarNumber() < skinPanel.getSkin().getPrice();
    }

    private void createButtonStyles() {

        TextButton.TextButtonStyle buyButtonStyle = new TextButton.TextButtonStyle();
        buyButtonStyle.font = fonts.get("36");
        buyButtonStyle.up = skin.getDrawable("yellow-btn-up");
        buyButtonStyle.down = skin.getDrawable("yellow-btn-down");
        buyButtonStyle.over = skin.getDrawable("yellow-btn-over");
        buyButtonStyle.disabled = skin.getDrawable("btn-disabled");


        TextButton.TextButtonStyle rightButtonStyle = new TextButton.TextButtonStyle();
        rightButtonStyle.font = fonts.get("24");
        rightButtonStyle.up = skin.getDrawable("arrow-btn-up");
        rightButtonStyle.down = skin.getDrawable("arrow-btn-down");
        rightButtonStyle.over = skin.getDrawable("arrow-btn-over");
        rightButtonStyle.disabled = skin.getDrawable("arrow-btn-disabled");


        textButtonStyles.put("left-arrow", rightButtonStyle);
        textButtonStyles.put("right-arrow", rightButtonStyle);
        textButtonStyles.put("buy", buyButtonStyle);
    }
}

