package com.frogger.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.gameObjects.Train;
import com.frogger.game.screens.MainMenuScreen;

/**
 * Audio.java
 * @author vadympolishchuk
 * Class that handles all the audio in a game.
 */

public class Audio {

    /** initialize all the audio files */
    private static final Sound SOUND_CLICKED        = Gdx.audio.newSound(Gdx.files.internal("sounds/click-sound.mp3"));
    private static final Sound SOUND_FROG_JUMPING   = Gdx.audio.newSound(Gdx.files.internal("sounds/frog-jumping.mp3"));
    private static final Sound SOUND_FROG_SPLASHING = Gdx.audio.newSound(Gdx.files.internal("sounds/splash.mp3"));
    private static final Sound SOUND_FROG_DYING     = Gdx.audio.newSound(Gdx.files.internal("sounds/dead.mp3"));
    private static final Sound SOUND_FROG_ON_LOG    = Gdx.audio.newSound(Gdx.files.internal("sounds/log.mp3"));
    private static final Sound SOUND_TRAFFIC        = Gdx.audio.newSound(Gdx.files.internal("sounds/traffic-sound.mp3"));
    private static final Sound SOUND_TRAIN          = Gdx.audio.newSound(Gdx.files.internal("sounds/train.mp3"));
    private static final Sound SOUND_COLLECTED_STAR = Gdx.audio.newSound(Gdx.files.internal("sounds/collect-star.mp3"));


    /** fields to define whether particular sound is playing at the moment */
    private static final long SOUND_CLICKED_DURATION = 200000000;
    private static long startedPlayingClickedSound;

    private static boolean playingFrogJumping   = false;
    private static boolean playingFrogSplashing = false;
    private static boolean playingFrogDying     = false;
    private static boolean playingFrogOnLog     = false;
    private static boolean playingTraffic       = false;

    /**
     * Method that plays given sound with given volume if game sounds are on
     * @param sound sound to play
     * @param volume volume of a sound
     */
    private static void playSound(Sound sound, float volume) {
        if (MainMenuScreen.IS_SOUNDS_ON) {
            sound.play(volume);
        }
    }

    /**
     * Play a sound of a clicked button
     */
    public static void playClickedSound() {
        if (TimeUtils.nanoTime() - startedPlayingClickedSound >= SOUND_CLICKED_DURATION) {
            startedPlayingClickedSound = TimeUtils.nanoTime();
            playSound(SOUND_CLICKED, 1.0f);
        }
    }

    /**
     * Play a sound of frog jumping
     */
    public static void playFrogJumpingSound() {
        if (!playingFrogJumping) {
            playSound(SOUND_FROG_JUMPING, 0.4f);
            playingFrogJumping = true;
        }
    }

    /**
     * Play a sound of frog splashing into the water
     */
    public static void playFrogSplashingSound() {
        if (!playingFrogSplashing) {
            playSound(SOUND_FROG_SPLASHING, 0.6f);
            playingFrogSplashing = true;
        }
    }

    /**
     * Play a sound of frog dying
     */
    public static void playFrogDyingSound() {
        if (!playingFrogDying) {
            playSound(SOUND_FROG_DYING, 0.8f);
            playingFrogDying = true;
            stopPlayingTrafficSound();
            setPlayingTraffic(true);
        }
    }

    /**
     * Play a sound of frog landing on a log
     */
    public static void playFrogOnLogSound() {
        if (!playingFrogOnLog) {
            playSound(SOUND_FROG_ON_LOG, 1.0f);
            playingFrogOnLog = true;
        }
    }

    /**
     * Play a sound of a train coming
     */
    public static void playTrainSound(Train train) {
        if (!train.isPlayingSound()) {
            playSound(SOUND_TRAIN, 0.5f);
            train.setPlayingSound(true);
        }
    }

    /**
     * Play a sound of collected star
     */
    public static void playCollectedStarSound() {
        playSound(SOUND_COLLECTED_STAR, 1.0f);
    }

    /**
     * Start playing traffic sounds (it's long, so toggle here)
     */
    public static void startPlayingTrafficSound() {
        if (!playingTraffic) {
            playSound(SOUND_TRAFFIC, 1.0f);
            playingTraffic = true;
        }
    }

    /**
     * Stop playing traffic sounds (it's long, so toggle here)
     */
    public static void stopPlayingTrafficSound() {
        if (playingTraffic) {
            SOUND_TRAFFIC.stop();
            playingTraffic = false;
        }
    }


    public static void setPlayingFrogDying(boolean playingFrogDying) {
        Audio.playingFrogDying = playingFrogDying;
    }

    public static void setPlayingFrogJumping(boolean playingFrogJumping) {
        Audio.playingFrogJumping = playingFrogJumping;
    }

    public static void setPlayingFrogOnLog(boolean playingFrogOnLog) {
        Audio.playingFrogOnLog = playingFrogOnLog;
    }

    public static void setPlayingFrogSplashing(boolean playingFrogSplashing) {
        Audio.playingFrogSplashing = playingFrogSplashing;
    }

    public static void setPlayingTraffic(boolean playingTraffic) {
        Audio.playingTraffic = playingTraffic;
    }

    public static void dispose() {
        SOUND_CLICKED.dispose();
        SOUND_FROG_JUMPING.dispose();
        SOUND_FROG_SPLASHING.dispose();
        SOUND_FROG_DYING.dispose();
        SOUND_FROG_ON_LOG.dispose();
        SOUND_TRAFFIC.dispose();
        SOUND_TRAIN.dispose();
        SOUND_COLLECTED_STAR.dispose();
    }
}
