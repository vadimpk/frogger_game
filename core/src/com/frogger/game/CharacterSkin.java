package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CharacterSkin {

    private static final Texture FROG_PACK = new Texture(Gdx.files.internal("characters/frog/frog.png"));
    private static final Texture TURTLE = new Texture(Gdx.files.internal("characters/frog/turtle.png"));
    private static final Texture CAT = new Texture(Gdx.files.internal("characters/frog/cat.png"));
    private static final Texture BIRD = new Texture(Gdx.files.internal("characters/frog/bird.png"));
    private static final Texture EGG = new Texture(Gdx.files.internal("characters/frog/egg.png"));

    private Texture texturePack;

    public TextureRegion standing;
    public TextureRegion jumping;
    public TextureRegion drowning1;
    public TextureRegion drowning2;
    public TextureRegion drowning3;
    public TextureRegion drowning4;
    public TextureRegion drowning5;
    public TextureRegion dead;

    private String name;
    private int price;
    private boolean isUnlocked;
    private boolean isChosen;
    private Util.Character character;

    public CharacterSkin(String name, int price, boolean isUnlocked, Util.Character character) {
        this.name = name;
        this.price = price;
        this.isUnlocked = isUnlocked;
        this.character = character;
        setCharacter(character);
    }

    public void setChosen(boolean chosen) {
        if(chosen) Frog.get().setCharacter(this);
        isChosen = chosen;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setCharacter(Util.Character character) {
        if (character == Util.Character.FROG) texturePack = FROG_PACK;
        else if (character == Util.Character.TURTLE) texturePack = TURTLE;
        else if (character == Util.Character.CAT) texturePack = CAT;
        else if (character == Util.Character.BIRD) texturePack = BIRD;
        else if (character == Util.Character.EGG) texturePack = EGG;

        standing = new TextureRegion(texturePack, 300, 150, 150, 150);
        jumping = new TextureRegion(texturePack, 150, 150, 150, 150);
        drowning1 = new TextureRegion(texturePack, 0, 0, 150, 150);
        drowning2 = new TextureRegion(texturePack, 150, 0, 150, 150);
        drowning3 = new TextureRegion(texturePack, 300, 0, 150, 150);
        drowning4 = new TextureRegion(texturePack, 0, 150, 150, 150);
        drowning5 = new TextureRegion(texturePack, 0, 300, 150, 150);
        dead = new TextureRegion(texturePack, 150, 300, 150, 150);
    }

    public static void dispose() {
        FROG_PACK.dispose();
        TURTLE.dispose();
        CAT.dispose();
        BIRD.dispose();
        EGG.dispose();
    }

    public TextureRegion getStanding() {
        return standing;
    }

    public Util.Character getCharacter() {
        return character;
    }
}
