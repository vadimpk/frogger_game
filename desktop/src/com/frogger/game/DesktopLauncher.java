package com.frogger.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.frogger.game.FroggerGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setMaximized(true);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("Frogger Game");

		try {
			new Lwjgl3Application(new FroggerGame(), config);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
