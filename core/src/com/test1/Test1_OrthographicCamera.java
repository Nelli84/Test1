package com.test1;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Comparator;

public class Test1_OrthographicCamera extends ScreenAdapter implements InputProcessor{
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
	private static final float WORLD_TO_SCREEN = .01f ;
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;
	
	private static final float CAMERA_SPEED = 2.0f;
	private static final float CAMERA_ZOOM_SPEED = 2.0f;
	private static final float CAMERA_ZOOM_MAX = 1.0f;
	private static final float CAMERA_ZOOM_MIN = 0.01f;
	private static final float CAMERA_MOVE_EDGE = 0.2f;

	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private Texture levelTexture;
	private Vector3 touch;
	
		
	boolean renderInterrupted = true;
	
	
//	private static final float FRAME_DURATION = 1.0f / 30;
	
	int bgWidth;
	int bgHeight;
	
	public Test1_OrthographicCamera () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		batch = new SpriteBatch();
		
		touch = new Vector3();
		
		levelTexture = new Texture(Gdx.files.internal("data/jungle-level.png"));
		
		levelTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		camera.position.x = SCENE_WIDTH * .5f;
		camera.position.y = SCENE_HEIGHT * .5f;
		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		Gdx.app.debug("index", "create" );
		renderInterrupted = true;
		
		Gdx.input.setInputProcessor(this);
		
	}

	@Override
	public void render (float delta) {
//		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			camera.position.x -= delta * CAMERA_SPEED;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			camera.position.x += delta * CAMERA_SPEED;
		}
		if(Gdx.input.isKeyPressed(Keys.UP)){
			camera.position.y += delta * CAMERA_SPEED;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			camera.position.y -= delta * CAMERA_SPEED;
		}
		
		// Touching on the edges also moves the camera
		if (Gdx.input.isTouched()) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
			camera.unproject(touch);
			
			if (touch.x > SCENE_WIDTH * (1.0f - CAMERA_MOVE_EDGE)) {
				camera.position.x += CAMERA_SPEED * delta;
			}
			else if (touch.x < SCENE_WIDTH * CAMERA_MOVE_EDGE) {
				camera.position.x -= CAMERA_SPEED * delta;
			}
			
			if (touch.y > SCENE_HEIGHT * (1.0f - CAMERA_MOVE_EDGE)) {
				camera.position.y += CAMERA_SPEED * delta;
			}
			else if (touch.y < SCENE_HEIGHT * CAMERA_MOVE_EDGE) {
				camera.position.y -= CAMERA_SPEED * delta;
			}
		}
		
		// Page up/down control the zoom
		if (Gdx.input.isKeyPressed(Keys.PAGE_UP)) {
			camera.zoom -= CAMERA_ZOOM_SPEED * delta;
		}
		else if (Gdx.input.isKeyPressed(Keys.PAGE_DOWN)) {
			camera.zoom += CAMERA_ZOOM_SPEED * delta;
		}
		
		float halfWidth = SCENE_WIDTH * .5f;
		float halfHeight = SCENE_HEIGHT * .5f;
		
		camera.position.x = MathUtils.clamp(camera.position.x, halfWidth * camera.zoom, levelTexture.getWidth() * WORLD_TO_SCREEN - halfWidth * camera.zoom);
		camera.position.y = MathUtils.clamp(camera.position.y, halfHeight * camera.zoom, levelTexture.getHeight() * WORLD_TO_SCREEN - halfHeight * camera.zoom);
		
		camera.zoom = MathUtils.clamp(camera.zoom, CAMERA_ZOOM_MIN, CAMERA_ZOOM_MAX);
		
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		touch = touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(touch);
		
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
//		batch.draw(levelTexture, 0, 0);
		batch.draw(levelTexture,
				   0.0f, 0.0f,
				   0.0f, 0.0f,
				   levelTexture.getWidth(), levelTexture.getHeight(),
				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				   0.0f,
				   0, 0,
				   levelTexture.getWidth(), levelTexture.getHeight(),
				   false, false);
		
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
		levelTexture.dispose();
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
		if(button == Buttons.LEFT){
			
		}
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
	
	private static class RegionComparator implements Comparator<AtlasRegion>{

		@Override
		public int compare(AtlasRegion o1, AtlasRegion o2) {
			return o1.name.compareTo(o2.name);
		}
		
	}
}


