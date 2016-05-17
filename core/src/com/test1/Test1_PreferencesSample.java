package com.test1;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

import java.util.Scanner;

public class Test1_PreferencesSample extends ScreenAdapter implements InputProcessor{
	private Scanner scanner = new Scanner(System.in);

	public Test1_PreferencesSample() {
		Preferences preferences = Gdx.app.getPreferences(Test1_PreferencesSample.class.getName());

		System.out.println("===================");
		System.out.println("Current preferences");
		System.out.println("===================");
		System.out.println("Player profile: " + preferences.getString("playerName", "default"));
		System.out.println("Difficulty: " + preferences.getInteger("difficulty", 5) + "/10");
		System.out.println("Music volume: " + preferences.getFloat("musicVolume", 100) + "/100");
		System.out.println("Effects volume: " + preferences.getFloat("effectsVolume", 100) + "/100");
		System.out.println("Show tips: " + preferences.getBoolean("showTips", true));
		System.out.println();


		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
			System.out.println("==================");
			System.out.println("Update preferences");
			System.out.println("==================");

			preferences.putString("playerName", getOption("Player profile"));
			preferences.putInteger("difficulty", MathUtils.clamp(Integer.parseInt(getOption("Difficulty")), 0, 10));
			preferences.putFloat("musicVolume", MathUtils.clamp(Float.parseFloat(getOption("Music volume")), 0.0f, 100.0f));
			preferences.putFloat("effectsVolume", MathUtils.clamp(Float.parseFloat(getOption("Effects volume")), 0.0f, 100.0f));
			preferences.putBoolean("showTips", Boolean.parseBoolean(getOption("Show tips (true/false)")));
			preferences.flush();
		}

		Gdx.app.exit();
	}

	private String getOption(String prompt) {
		System.out.print(prompt + ": ");
		return scanner.nextLine();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}

