package com.frogger.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.Audio;
import com.frogger.game.CharacterSkin;
import com.frogger.game.DataIO;
import com.frogger.game.FroggerGame;

import static com.frogger.game.Const.*;
import static com.frogger.game.DataIO.*;
import static com.frogger.game.Scorer.FILLED_STAR;

public class SkinsScreen extends Screen{

    private  Label starNumber;
    private SkinPanel[] skinPanels;
    public SkinsScreen(FroggerGame game) {
        super(game);
        createButtonStyles();
        createSkinPanels();
    }

    @Override
    public void show() {
        super.show();

        float panelWidth = 0.75f*WINDOW_WIDTH / 4;
        float panelHeight = 0.38f*WINDOW_HEIGHT;
        float distanceX = 0.01f*WINDOW_WIDTH + panelWidth;
        float distanceY = 0.01f* WINDOW_HEIGHT;
        for (int i = 0; i < Math.min(skinPanels.length, 8); i++) {
            if(i < 4) skinPanels[i].show(stage, 0.1f*WINDOW_WIDTH + distanceX * i,
                                        0.6f*WINDOW_HEIGHT + distanceY,
                                        panelWidth, panelHeight);
            else skinPanels[i].show(stage, 0.1f*WINDOW_WIDTH + distanceX * (i - 4) ,
                    0.2f*WINDOW_HEIGHT + distanceY,
                    panelWidth, panelHeight);
        }

        Image star = new Image(FILLED_STAR);
        float starSize = 0.08f * WINDOW_HEIGHT;
        star.setBounds(WINDOW_WIDTH - 0.1f*WINDOW_HEIGHT, 0.9f*WINDOW_HEIGHT, starSize, starSize);
        starNumber = new Label(String.valueOf(DataIO.getStarNumber()), new Label.LabelStyle(fonts.get("36"), Color.BLACK));
        starNumber.setX(star.getX() - starNumber.getWidth()*1.2f);
        starNumber.setY(star.getY() + starSize / 2 - starNumber.getHeight()/2);

        stage.addActor(getBackButton(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 0.08f*WINDOW_HEIGHT));
        stage.addActor(star);
        stage.addActor(starNumber);
    }

    private void createButtonStyles() {
        TextButton.TextButtonStyle buyButtonStyle = new TextButton.TextButtonStyle();
        buyButtonStyle.font = fonts.get("24");
        buyButtonStyle.up = skin.getDrawable("yellow-btn-up");
        buyButtonStyle.down = skin.getDrawable("yellow-btn-down");
        buyButtonStyle.over = skin.getDrawable("yellow-btn-over");
        buyButtonStyle.disabled = skin.getDrawable("btn-disabled");
        TextButton.TextButtonStyle setButtonStyle = new TextButton.TextButtonStyle();
        setButtonStyle.font = fonts.get("24");
        setButtonStyle.up = skin.getDrawable("green-btn-up");
        setButtonStyle.down = skin.getDrawable("green-btn-down");
        setButtonStyle.over = skin.getDrawable("green-btn-over");
        setButtonStyle.disabled = skin.getDrawable("btn-disabled");
        textButtonStyles.put("buy", buyButtonStyle);
        textButtonStyles.put("set", setButtonStyle);
    }

    private void createSkinPanels() {
        final CharacterSkin[] skins = getSkins();
        skinPanels = new SkinPanel[skins.length];
        for (int i = 0; i < skins.length; i++) {
            skinPanels[i] = new SkinPanel(skins[i]);
        }
    }

    private class SkinPanel {
        Image promo;
        TextButton buyButton;
        TextButton setButton;
        Label priceLabel;
        Label nameLabel;

        CharacterSkin skin;

        public SkinPanel(final CharacterSkin skin) {
            this.skin = skin;
            promo = new Image(skin.getStanding());
            buyButton = new TextButton("Buy", textButtonStyles.get("buy"));
            setButton = new TextButton("Set", textButtonStyles.get("set"));
            Label.LabelStyle labelStyle = new Label.LabelStyle(fonts.get("24"), Color.BLACK);
            priceLabel = new Label("Price: " + skin.getPrice(), labelStyle);
            nameLabel = new Label(skin.getName(), labelStyle);
            if (skin.isChosen() || !skin.isUnlocked()) setButton.setDisabled(true);
            if (skin.isUnlocked()) buyButton.setDisabled(true);
        }

        public void show(Stage stage, float x, float y, float width, float height) {
            float distanceY = 0.05f*height;
            float distanceX = 0.05f*width;
            float buttonHeight = 0.1f*height;
            float buttonWidth = 3f*buttonHeight;


            setButton.setBounds(x + width / 2 - buttonWidth / 2, y + distanceX, buttonWidth, buttonHeight);
            buyButton.setBounds(x + width / 2 - buttonWidth / 2, y + 2f*distanceX + buttonHeight, buttonWidth, buttonHeight);
            priceLabel.setX(x+ width / 2 - priceLabel.getWidth() / 2);
            priceLabel.setY(buyButton.getY() + distanceY + buttonHeight);
            nameLabel.setX(x+ width / 2 - nameLabel.getWidth() / 2);
            nameLabel.setY(priceLabel.getY() + distanceY + priceLabel.getHeight());
            float imgSize = height - 6f*distanceY - 2f*buttonHeight - priceLabel.getHeight() - nameLabel.getHeight();
            promo.setBounds(x + (width/2 - imgSize/2), nameLabel.getY() + distanceY + nameLabel.getHeight(),
                    imgSize, imgSize);

            setButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Audio.playClickedSound();
                    for (CharacterSkin skin : getSkins()) skin.setChosen(false);
                    skin.setChosen(true);
                    for (SkinPanel skinPanel : skinPanels) if(skinPanel.skin.isUnlocked()) skinPanel.setButton.setDisabled(false);
                    setButton.setDisabled(true);
                    updateSkins();
                }
            });

            buyButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Audio.playClickedSound();
                    if(getStarNumber() >= skin.getPrice()) {
                        setButton.setDisabled(false);
                        buyButton.setDisabled(true);
                        skin.setUnlocked(true);
                        starNumber.setText(String.valueOf(DataIO.getStarNumber()));
                        updateSkins();
                    }
                }
            });

            stage.addActor(setButton);
            stage.addActor(buyButton);
            stage.addActor(priceLabel);
            stage.addActor(nameLabel);
            stage.addActor(promo);
        }
    }
}
