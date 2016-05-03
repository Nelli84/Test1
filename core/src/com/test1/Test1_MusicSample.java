package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Test1_MusicSample extends ScreenAdapter implements InputProcessor{
	private static final float VOLUME_CHANGE = 0.2f;

	private Array<Music> songs;
	private int currentSongIdx;
	private float volume;
	private SongListener listener;

	public Test1_MusicSample() {
		listener = new SongListener();

		songs = new Array<Music>();
		songs.add(Gdx.audio.newMusic(Gdx.files.internal("data/music/song_1.mp3")));
		songs.add(Gdx.audio.newMusic(Gdx.files.internal("data/music/song_2.mp3")));
		songs.add(Gdx.audio.newMusic(Gdx.files.internal("data/music/song_3.mp3")));
		songs.add(Gdx.audio.newMusic(Gdx.files.internal("data/music/song_4.mp3")));
		songs.add(Gdx.audio.newMusic(Gdx.files.internal("data/music/song_5.mp3")));

		currentSongIdx = 0;
		volume = 1.0f;

		Gdx.input.setInputProcessor(this);

		Gdx.app.log("MusicSample", "Instructions");
		Gdx.app.log("MusicSample", "- Press right to play the next song");
		Gdx.app.log("MusicSample", "- Press left to play the previous song");
		Gdx.app.log("MusicSample", "- Press p to pause");
		Gdx.app.log("MusicSample", "- Press r to resume");
		Gdx.app.log("MusicSample", "- Press up to increase volume");
		Gdx.app.log("MusicSample", "- Press down to decrease volume");

		playSong(0);
	}

	@Override
	public void dispose() {
		for (Music song : songs) {
			song.dispose();
		}
	}

	@Override
	public boolean keyDown (int keycode) {
		if (keycode == Input.Keys.P) {
			songs.get(currentSongIdx).pause();
			Gdx.app.log("MusicSample", "Song paused");
		}
		else if (keycode == Input.Keys.R) {
			songs.get(currentSongIdx).play();
			Gdx.app.log("MusicSample", "Song resumed");
		}
		else if (keycode == Input.Keys.S) {
			songs.get(currentSongIdx).stop();
			Gdx.app.log("MusicSample", "Song stop");
		}
		else if (keycode == Input.Keys.UP) {
			changeVolume(VOLUME_CHANGE);
			Gdx.app.log("MusicSample", "Volume up");
		}
		else if (keycode == Input.Keys.DOWN) {
			changeVolume(-VOLUME_CHANGE);
			Gdx.app.log("MusicSample", "Volume down");
		}
		else if (keycode == Input.Keys.RIGHT) {
			playSong((currentSongIdx + 1) % songs.size);
			Gdx.app.log("MusicSample", "Next song");
		}

		else if (keycode == Input.Keys.LEFT) {
			int songIdx = (currentSongIdx - 1) < 0 ? songs.size - 1 : currentSongIdx - 1;
			playSong(songIdx);
			Gdx.app.log("MusicSample", "Previous song");
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
		playSong((currentSongIdx + 1) % songs.size);
		Gdx.app.log("MusicSample", "Next song");
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

	void playSong(int songIdx) {
		Music song = songs.get(currentSongIdx);
		song.setOnCompletionListener(null);
		song.stop();

		currentSongIdx = songIdx;
		song = songs.get(currentSongIdx);
		song.play();
		song.setVolume(volume);
		song.setOnCompletionListener(listener);
	}

	void changeVolume(float volumeChange) {
		Music song = songs.get(currentSongIdx);
		volume = MathUtils.clamp(song.getVolume() + volumeChange, 0.0f, 1.0f);
		song.setVolume(volume);
	}

	private class SongListener implements Music.OnCompletionListener {
		@Override
		public void onCompletion(Music music) {
			playSong((currentSongIdx + 1) % songs.size);
			Gdx.app.log("MusicSample", "Song finished, play next song");
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f,.5f,.5f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}

