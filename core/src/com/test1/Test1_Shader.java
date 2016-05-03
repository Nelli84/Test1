package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Test1_Shader extends ScreenAdapter implements InputProcessor{
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
	private static final float WORLD_TO_SCREEN = .01f ;
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;

    private static final int NUM_SHADERS = 5;
	
	private OrthographicCamera camera;
	private FitViewport viewport;
	private SpriteBatch batch;
	private Texture background;
	
	private ShaderProgram shaders[];
	private String shaderNames[];
	private int currentShader;
	
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 720;
	
	private FrameBuffer fboB;
	private int dirLocation;
	private int radiusLocation;
	
	boolean renderInterrupted = true;
	
	
	public Test1_Shader () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		batch = new SpriteBatch();
		
		background = new Texture(Gdx.files.internal("data/jungle-level.png"));
		
		shaders = new ShaderProgram[NUM_SHADERS];
		shaderNames = new String[NUM_SHADERS];
		currentShader = 0;
		
		shaders[0] = null;
		shaderNames[0] = "Null";
		shaders[1] = new ShaderProgram(Gdx.files.internal("data/shaders/grayscale.vert"),
									   Gdx.files.internal("data/shaders/grayscale.frag"));
		shaderNames[1] = "Grayscale";
		shaders[2] = new ShaderProgram(Gdx.files.internal("data/shaders/sepia.vert"),
									   Gdx.files.internal("data/shaders/sepia.frag"));
		shaderNames[2] = "Sepia";
		shaders[3] = new ShaderProgram(Gdx.files.internal("data/shaders/inverted.vert"),
									   Gdx.files.internal("data/shaders/inverted.frag"));
		shaderNames[3] = "Inverted";
		
		shaders[4] = new ShaderProgram(Gdx.files.internal("data/shaders/blur.vert"),
				   Gdx.files.internal("data/shaders/blur_.frag"));
        shaderNames[4] = "blur_";
        
        shaders[4].begin();
		shaders[4].setUniformf("resolution", VIRTUAL_WIDTH);
		shaders[4].end();
       
		dirLocation = shaders[4].getUniformLocation("dir");
		radiusLocation = shaders[4].getUniformLocation("radius");
		fboB = new FrameBuffer(Format.RGBA8888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false);	
		
//		camera.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f, 0.0f);
		
		Gdx.input.setInputProcessor(this);
		
		for (ShaderProgram shader : shaders) {
			if (shader != null && !shader.isCompiled()) {
				Gdx.app.error("ShaderSample: ", shader.getLog());
			}
		}
		
		Gdx.app.log("ShaderSample", "Switching to shader " + shaderNames[currentShader]);
//		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
//		batch.setProjectionMatrix(camera.combined);
		
		
		
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(currentShader == 4){
			applyBlur(3);
			return;
		}
		
//		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		int width = background.getWidth();
		int height = background.getHeight();
		
		float width_ = viewport.getWorldWidth();
		float height_ = width_ / width * height;
		
		float y = (viewport.getWorldHeight() - height_) * .5f;
		
//		batch.draw(background,
//				   0.0f, 0.0f,
//				   0.0f, 0.0f,
//				   width, height,
//				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
//				   0.0f,
//				   0, 0,
//				   width, height,
//				   false, false);
		
		batch.draw(background, 0, y, width_, height_);
		
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
		
		viewport.update(width, height, true);
		
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
		fboB.dispose();
		
		for (ShaderProgram shader : shaders) {
			if (shader != null) {
				shader.dispose();
			}
		}
	}

	private void applyBlur(float blur) {		
		// Horizontal blur from FBO A to FBO B
		
//		viewport.update(VIRTUAL_WIDTH,VIRTUAL_HEIGHT,true);
//		batch.setProjectionMatrix(camera.combined);
		
		fboB.begin();
		batch.begin();
		batch.setShader(shaders[4]);
		shaders[4].setUniformf(dirLocation, 1.0f, 0.0f);
		shaders[4].setUniformf(radiusLocation, blur);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		drawTexture(background, true);
		batch.flush();
		fboB.end();
		
		// Vertical blur from FBO B to the screen
				
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
//		batch.setProjectionMatrix(camera.combined);

		shaders[4].setUniformf(dirLocation, 0.0f, 1.0f);
//		shaders[4].setUniformf(radiusLocation, blur);
		
		drawTexture(fboB.getColorBufferTexture(), false);
		batch.end();
	}
	
	
	
	private void drawTexture(Texture texture, boolean flipY) {
		int width = texture.getWidth();
		int height = texture.getHeight();
		
		float width_ = viewport.getWorldWidth();
		float height_ = width_ / width * height;
		
		float y = (viewport.getWorldHeight() - height_) * .5f;
		
		
		batch.draw(texture,
				   0, y,
				   0.0f, 0.0f,
				   width_, height_,
				   1, 1,
				   0.0f,
				   0, 0,
				   width, height,
				   false, flipY);
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
		currentShader = (currentShader + 1) % shaders.length;
		
		if(currentShader != 4){
			batch.setShader(shaders[currentShader]);
		}
		
		
		Gdx.app.log("ShaderSample", "Switching to shader " + shaderNames[currentShader]);
		
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
		// TODO Auto-generated method stub
		return false;
	}
}
