package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_FrameBuffer extends ScreenAdapter{
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
	private enum GalleryState {
		PICTURE,
		TRANSITIONING,
	}
	
	private static final float WORLD_TO_SCREEN = 1.0f / 100.0f;
	
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 720;
	
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;
	
	private static final int GALLERY_NUM_PICTURES = 4;
	private static final float GALLERY_PICTURE_TIME = 3.0f;
	private static final float GALLERY_TRANSITION_TIME = 2.0f;
	
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	
	private TextureRegion [] gallery;
	private FrameBuffer currentFrameBuffer;
	private FrameBuffer nextFrameBuffer;
	
	private int currentPicture;
	private float time;
	private GalleryState state;
		
	boolean renderInterrupted = true;
	
	
	public Test1_FrameBuffer () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		batch = new SpriteBatch();
		
		gallery = new TextureRegion[GALLERY_NUM_PICTURES];
		
		for (int i = 0; i < GALLERY_NUM_PICTURES; ++i) {
			gallery[i] = new TextureRegion(new Texture(Gdx.files.internal("data/gallery/gallery" + (i + 1) + ".jpg")));
		}
		
		currentFrameBuffer = new FrameBuffer(Format.RGB888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false);
		nextFrameBuffer = new FrameBuffer(Format.RGB888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false);
		
		currentPicture = 0;
		time = 0.0f;
		state = GalleryState.PICTURE;
		
		camera.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f, 0.0f);
		
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
		
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		batch.setProjectionMatrix(camera.combined);
		
		time += delta;
		
		switch (state) {
		case PICTURE:
			updateStatePicture();
			break;
		case TRANSITIONING:
			updateStateTransitioning();
			break;
		}
		
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

		for (TextureRegion background : gallery) {
			background.getTexture().dispose();
		}
		
		currentFrameBuffer.dispose();
		nextFrameBuffer.dispose();
	}


	private void updateStatePicture() {
		batch.begin();
		drawRegion(gallery[currentPicture]);
		batch.end();
		
		if (time > GALLERY_PICTURE_TIME) {
			time = 0.0f;
			state = GalleryState.TRANSITIONING;
			
			TextureRegion region;
			
			region = gallery[currentPicture];
//			region.flip(false, true);
			
			currentFrameBuffer.bind();
			
			Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			batch.begin();
//			drawRegion(region);
			drawTexture_(region.getTexture());
			batch.end();
			
//			region.flip(false, true);
			
			currentPicture = (currentPicture + 1) % GALLERY_NUM_PICTURES;
			
			region = gallery[currentPicture];
//			region.flip(false,  true);
			
			nextFrameBuffer.bind();
			
			Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			batch.begin();
//			drawRegion((gallery[currentPicture]));
			drawTexture_((gallery[currentPicture]).getTexture());
			batch.end();
			
			FrameBuffer.unbind();
			
//			region.flip(false, true);
		}
	}
	
	private void updateStateTransitioning() {
		float alpha = Math.min(time / GALLERY_TRANSITION_TIME, 1.0f); 
		
		batch.begin();
		batch.setColor(1.0f, 1.0f, 1.0f, 1.0f - alpha);
		drawTexture(currentFrameBuffer.getColorBufferTexture());
		
		batch.setColor(1.0f, 1.0f, 1.0f, alpha);
		drawTexture(nextFrameBuffer.getColorBufferTexture());
		batch.end();
		
		if (time > GALLERY_TRANSITION_TIME) {
			time = 0.0f;
			state = GalleryState.PICTURE;
		}
	}
	
	private void drawRegion(TextureRegion region) {
		int width = region.getRegionWidth();
		int height = region.getRegionHeight();
		
		batch.draw(region,
				   0.0f, 0.0f,
				   0.0f, 0.0f,
				   width, height,
				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				   0.0f);
	}
	
	private void drawTexture(Texture texture) {
		int width = texture.getWidth();
		int height = texture.getHeight();
		
		batch.draw(texture,
				   0.0f, 0.0f,
				   0.0f, 0.0f,
				   width, height,
				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				   0.0f,
				   0, 0,
				   width, height,
				   false, false);
	}
	private void drawTexture_(Texture texture) {
		int width = texture.getWidth();
		int height = texture.getHeight();
		
		batch.draw(texture,
				0.0f, 0.0f,
				0.0f, 0.0f,
				width, height,
				WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				0.0f,
				0, 0,
				width, height,
				false, true);
	}
}


