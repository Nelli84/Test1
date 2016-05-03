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
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_Blur extends ScreenAdapter implements InputProcessor{
	
		
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
	private static final float WORLD_TO_SCREEN = .01f ;
		
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;
	
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 720;
	
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private Texture background;
	private Texture foreground;
	private Texture mountains;
	private Texture rock;
	private Texture dinosaur;
	private Texture caveman;
	private ShaderProgram shader;
	private FrameBuffer fboA;
	private FrameBuffer fboB;
	
	boolean renderInterrupted = true;
	
	
	public Test1_Blur () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		
		batch = new SpriteBatch();
		
		background = new Texture(Gdx.files.internal("data/blur/background.png"));
		foreground = new Texture(Gdx.files.internal("data/blur/foreground.png"));
		mountains = new Texture(Gdx.files.internal("data/blur/mountains.png"));
		rock = new Texture(Gdx.files.internal("data/blur/rock.png"));
		caveman = new Texture(Gdx.files.internal("data/blur/caveman.png"));
		dinosaur = new Texture(Gdx.files.internal("data/blur/dinosaur.png"));
		
		shader = new ShaderProgram(Gdx.files.internal("data/shaders/blur.vert"), Gdx.files.internal("data/shaders/blur.frag"));
		fboA = new FrameBuffer(Format.RGBA8888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false);
		fboB = new FrameBuffer(Format.RGBA8888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false);
		
		camera.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f, 0.0f);

		if (!shader.isCompiled()) {
			Gdx.app.error("Shader", shader.getLog());
			Gdx.app.exit();
		}
		
		shader.begin();
		shader.setUniformf("resolution", VIRTUAL_WIDTH);
		shader.end();
		
	}

	float blur = 0;
	int coef = 1;
	@Override
	public void render (float delta) {
		blur += delta * coef;
		if(blur > 7){
			coef = -1;
		}
		if(blur < 0){
			coef = 1;
			blur = 0;
		}
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		batch.begin();
		
		// Draw background as-is
		batch.setShader(null);
		
		drawTexture(background,  0.0f, 0.0f);
		batch.flush();
		
		// Draw blurred mountains
//		viewport.update(VIRTUAL_WIDTH,VIRTUAL_HEIGHT,true);
//		batch.setProjectionMatrix(camera.combined);
		fboA.begin();
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setShader(null);
		
		drawTexture(mountains, 0.0f, 0.0f);
		batch.flush();
		fboA.end();
//		applyBlur(3.0f);
		applyBlur(blur);
		
		// Draw foreground and characters without blur effect
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
		batch.setProjectionMatrix(camera.combined);
		batch.setShader(null);
		drawTexture(foreground, 0.0f, 0.0f);
		drawTexture(caveman, 1.0f, 1.5f);
		drawTexture(dinosaur, 6.0f, 2.45f);
		batch.flush();
		
		// Draw blurred rock
//		viewport.update(VIRTUAL_WIDTH,VIRTUAL_HEIGHT,true);
//		batch.setProjectionMatrix(camera.combined);
		fboA.begin();
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setShader(null);
		
		drawTexture(rock, 0.0f, 0.0f);
		batch.flush();
		fboA.end();
		applyBlur(5.0f);
		
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
		
		viewport.update(width, height,true);
		
		
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
		foreground.dispose();
		mountains.dispose();
		caveman.dispose();
		dinosaur.dispose();
		background.dispose();
		shader.dispose();
		fboA.dispose();
		fboB.dispose();
	}

	
	private void applyBlur(float blur) {		
		// Horizontal blur from FBO A to FBO B
		fboB.begin();
		batch.setShader(shader);
		shader.setUniformf("dir", 1.0f, 0.0f);
		shader.setUniformf("radius", blur);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		drawTexture(fboA.getColorBufferTexture(),  0.0f, 0.0f);
		batch.flush();
		fboB.end();
		
		// Vertical blur from FBO B to the screen
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
		batch.setProjectionMatrix(camera.combined);

		shader.setUniformf("dir", 0.0f, 1.0f);
		shader.setUniformf("radius", blur);
		
		drawTexture(fboB.getColorBufferTexture(), 0.0f, 0.0f);
		batch.flush();
	}
	
	
	
	private void drawTexture(Texture texture, float x, float y) {
		int width = texture.getWidth();
		int height = texture.getHeight();
		
		batch.draw(texture,
				   x, y,
				   0.0f, 0.0f,
				   width, height,
				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				   0.0f,
				   0, 0,
				   width, height,
				   false, false);
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
		
		return false;

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
