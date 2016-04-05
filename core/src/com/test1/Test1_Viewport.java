package com.test1;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_Viewport extends ScreenAdapter implements InputProcessor{
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1);
	
	private static final String TAG = "index";
	
	private static final float MIN_SCENE_WIDTH = 800.0f;
	private static final float MIN_SCENE_HEIGHT = 600.0f;
	private static final float MAX_SCENE_WIDTH = 1280.0f;
	private static final float MAX_SCENE_HEIGHT = 720.0f;
	
	private ArrayMap<String, Viewport> viewports;
	private int currentViewport;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture background;
	
	private static final float CAMERA_SPEED = 2.0f;
	private static final float CAMERA_ZOOM_SPEED = 2.0f;
	private static final float CAMERA_ZOOM_MAX = 1.0f;
	private static final float CAMERA_ZOOM_MIN = 0.01f;
	private static final float CAMERA_MOVE_EDGE = 0.2f;

	private Vector3 touch;
	
		
	boolean renderInterrupted = true;
	
	
//	private static final float FRAME_DURATION = 1.0f / 30;
	
	int bgWidth;
	int bgHeight;
	
	public Test1_Viewport () {

        camera = new OrthographicCamera();
		
		createViewports();
		selectNextViewport();
		
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("data/menu.png"));
		
		Gdx.input.setInputProcessor(this);
		
		
		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		Gdx.app.debug("index", "create" );
		renderInterrupted = true;
		
		
	}

	@Override
	public void render (float delta) {
//		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, -background.getWidth() * 0.5f, -background.getHeight() * 0.5f);
		batch.end();
		
		
		if(renderInterrupted){
			Gdx.app.debug("index", "render");
			renderInterrupted = false;
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.debug("index", "resize");
		renderInterrupted = true;
		
		viewports.getValueAt(currentViewport).update(width, height);
		
	}

	@Override
	public void pause() {

		Gdx.app.debug("index", "pause");
		renderInterrupted = true;
	}

	@Override
	public void resume() {

		Gdx.app.debug("index", "resume");
		renderInterrupted = true;
	}

	@Override
	public void dispose() {

		Gdx.app.debug("index", "dispose");
		renderInterrupted = true;
		
		batch.dispose();
		background.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		selectNextViewport();
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
//		if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
//			caveman.scale(amount * .005f);
//		}else{
//			caveman.rotate(amount * .5f);
//		}
		
//		dinosaur_.scroll(amount * .005f, amount * .005f);
		return false;
	}

	private void createViewports(){
		viewports = new ArrayMap<String, Viewport>();
		viewports.put("StretchViewport", new StretchViewport(MIN_SCENE_WIDTH, MIN_SCENE_HEIGHT, camera));
		viewports.put("FitViewport", new FitViewport(MIN_SCENE_WIDTH, MIN_SCENE_HEIGHT, camera));
		viewports.put("FillViewport", new FillViewport(MIN_SCENE_WIDTH, MIN_SCENE_HEIGHT, camera));
		viewports.put("ScreenViewport", new ScreenViewport(camera));
		viewports.put("ExtendViewport (no max)", new ExtendViewport(MIN_SCENE_WIDTH, MIN_SCENE_HEIGHT, camera));
		viewports.put("ExtendViewport (max)", new ExtendViewport(MIN_SCENE_WIDTH, MIN_SCENE_HEIGHT, MAX_SCENE_HEIGHT, MAX_SCENE_WIDTH, camera));
		currentViewport = -1;
	}
	
	private void selectNextViewport() {
		currentViewport = (currentViewport + 1) % viewports.size;
		viewports.getValueAt(currentViewport).update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.app.log(TAG, "selected " + viewports.getKeyAt(currentViewport));
	}
	
}


