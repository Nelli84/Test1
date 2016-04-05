package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_ShaderUniform extends ScreenAdapter implements InputProcessor{
	
	private enum State {
		TransitionIn,
		TransitionOut,
		Picture,
	}
	
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
	private static final float WORLD_TO_SCREEN = .01f ;
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;
	
	private static final float TRANSITION_IN_TIME = 1.0f;
	private static final float TRANSITION_OUT_TIME = 0.5f;
	private static final float PICTURE_TIME = .5f;// 2.0f;


	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private Texture background;
	private ShaderProgram shader;
	private State state;
	private float time;
	private float resolution[];
	private float radius;
	
	boolean renderInterrupted = true;
	
	
	public Test1_ShaderUniform () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("data/jungle-level.png"));
		shader = new ShaderProgram(Gdx.files.internal("data/shaders/vignette.vert"), Gdx.files.internal("data/shaders/vignettePlus.frag"));
		resolution = new float[2];
		
		camera.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f, 0.0f);
		batch.setShader(shader);
		
		if (!shader.isCompiled()) {
			Gdx.app.error("Shader", shader.getLog());
		}
		
		state = State.TransitionIn;
		time = 0.0f;
		
		
		
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		switch(state) {
		case TransitionIn:
			radius = time / TRANSITION_IN_TIME;
			
			if (time > TRANSITION_IN_TIME) {
				time = 0.0f;
				state = State.Picture;
			}
			
			break;
		case TransitionOut:
			radius = 1.0f - time / TRANSITION_OUT_TIME;
			
			if (time > TRANSITION_OUT_TIME) {
				time = 0.0f;
				state = State.Picture;
			}
			
			break;
		case Picture:
			if (time > PICTURE_TIME) {
				time = 0.0f;
				state = radius == 0.0f ? State.TransitionIn : State.TransitionOut;
			}
			break;
		}
		
		radius = MathUtils.clamp(radius, 0.0f, 1.0f);
		time += delta * 1f;
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
        shader.setUniform2fv("resolution", resolution , 0, 2);
//        shader.setUniformf("resolution", resolution[0],resolution[1]);
        shader.setUniformf("radius", radius);
		
        
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
		
		viewport.update(width, height);
		
		resolution[0] = width;
		resolution[1] = height;

		
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
		shader.dispose();
		
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
