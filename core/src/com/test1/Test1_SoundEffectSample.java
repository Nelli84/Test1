package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_SoundEffectSample extends ScreenAdapter implements InputProcessor{
	private IntMap<Sound> sounds;
	private IntArray keys = new IntArray();

	public Test1_SoundEffectSample() {
		sounds = new IntMap<Sound>();
		sounds.put(Input.Keys.NUM_1, Gdx.audio.newSound(Gdx.files.internal("data/sfx/sfx_01.wav")));
		keys.add(Input.Keys.NUM_1);
		sounds.put(Input.Keys.NUM_2, Gdx.audio.newSound(Gdx.files.internal("data/sfx/sfx_02.wav")));
		keys.add(Input.Keys.NUM_2);
		sounds.put(Input.Keys.NUM_3, Gdx.audio.newSound(Gdx.files.internal("data/sfx/sfx_03.mp3")));
		keys.add(Input.Keys.NUM_3);
		sounds.put(Input.Keys.NUM_4, Gdx.audio.newSound(Gdx.files.internal("data/sfx/sfx_04.wav")));
		keys.add(Input.Keys.NUM_4);
		sounds.put(Input.Keys.NUM_5, Gdx.audio.newSound(Gdx.files.internal("data/sfx/sfx_05.mp3")));
		keys.add(Input.Keys.NUM_5);
		sounds.put(Input.Keys.NUM_6, Gdx.audio.newSound(Gdx.files.internal("data/sfx/sfx_06.mp3")));
		keys.add(Input.Keys.NUM_6);
		sounds.put(Input.Keys.NUM_7, Gdx.audio.newSound(Gdx.files.internal("data/sfx/sfx_07.wav")));
		keys.add(Input.Keys.NUM_7);

		Gdx.input.setInputProcessor(this);

		Gdx.app.log("SoundEffectSample", "Instructions");
		Gdx.app.log("SoundEffectSample", "- Press keys 1-0 to play sounds");
		Gdx.app.log("SoundEffectSample", "- Press s to stop all sounds");
		Gdx.app.log("SoundEffectSample", "- Press p to pause all sounds");
		Gdx.app.log("SoundEffectSample", "- Press r to resume all soud");
	}

	@Override
	public void dispose() {
		for (Sound sound : sounds.values()) {
			sound.dispose();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.39f, 0.58f, 0.92f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	long soundId;
	@Override
	public boolean keyDown (int keycode) {

		Gdx.app.log("SoundEffectSample", "keycode = " + keycode);
		if (keycode == Input.Keys.S) {
			for (Sound sound : sounds.values()) {
				sound.stop();
			}

			Gdx.app.log("SoundEffectSample", "Sounds stopped");
		}else if (keycode == Input.Keys.N) {
			for (Sound sound : sounds.values()) {
				sound.setLooping(soundId,false);
			}

			Gdx.app.log("SoundEffectSample", "Sounds not loop "+soundId);
		}
		else if (keycode == Input.Keys.L) {
			for (Sound sound : sounds.values()) {
				sound.setLooping(soundId,true);
			}

			Gdx.app.log("SoundEffectSample", "Sounds loop "+soundId);
		}else if (keycode == Input.Keys.P) {
			for (Sound sound : sounds.values()) {
				sound.pause();
			}

			Gdx.app.log("SoundEffectSample", "Sounds paused");
		}
		else if (keycode == Input.Keys.R) {
			for (Sound sound : sounds.values()) {
				sound.resume();
			}

			Gdx.app.log("SoundEffectSample", "Sounds resumed");
		}
		else {
			Sound sound = sounds.get(keycode);

			if (sound != null)
			{
				soundId = sound.loop();
				Gdx.app.log("SoundEffectSample", "Playing sound");
			}
		}

		return true;
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
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		Sound sound = sounds.get(keys.get(MathUtils.random(sounds.size - 1)));
		sound.play();

		Gdx.app.log("SoundEffectSample", "Playing sound");
		return true;
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

