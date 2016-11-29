package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class Test1_AssetManagerSimpleSample extends ScreenAdapter implements InputProcessor{

	private static final String TAG = "AssetManagerSimpleSample";

	SpriteBatch batch;

	Texture background, logo;
	TiledMap map;

	float unitScale;
	OrthogonalTiledMapRenderer renderer;
	OrthographicCamera camera;

	private Vector2 logoPos;

	AssetManager manager;

	public Test1_AssetManagerSimpleSample () {
		batch = new SpriteBatch();

		manager = new AssetManager();

		// Load assets, it is being loaded a map because it is a good example for dependencies between
		// assets, so don't focus on the map itself considering that it will be explained in later recipes
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		manager.load("data/loading_screen/map.tmx", TiledMap.class);
		unitScale = 1/16f;

		manager.load("data/loading_screen/background.png", Texture.class);
		manager.load("data/loading_screen/logo.png", Texture.class);
		manager.finishLoading(); // Blocks until all resources are loaded into memory
		Gdx.app.log(TAG, "Assets loaded");

		// Get Assets
		background = manager.get("data/loading_screen/background.png");
		map = manager.get("data/loading_screen/map.tmx");
		logo = manager.get("data/loading_screen/logo.png");

		renderer = new OrthogonalTiledMapRenderer(map, unitScale);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 80, 45);
		camera.update();

		// Get logo position
		logoPos = new Vector2();
		// >> bitwise operator bill just divide by 2, the explicitly written times, in this case 1
		logoPos.set((Gdx.graphics.getWidth()-logo.getWidth())>>1, Gdx.graphics.getHeight()>>1);

		// Trace dependences
		Gdx.app.log(TAG + ".Dependences:\n", manager.getDiagnostics());

	}

	@Override
	public void dispose() {
		manager.dispose();
		renderer.dispose();
		batch.dispose();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render background image
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();

		// Render map
		renderer.setView(camera);
		renderer.render();

		// Render logo
		batch.begin();
		batch.draw(logo, logoPos.x, logoPos.y);
		batch.end();
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

