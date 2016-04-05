package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_FrameBufferParticleEffect extends ScreenAdapter implements InputProcessor{
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
private static final float WORLD_TO_SCREEN = 1.0f / 100.0f;
	
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;
	
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 720;

	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	
	private Texture background;
	private ParticleEffectPool pool;
	private Array<PooledEffect> activeEffects;
	private Vector3 touchPos;
	private FrameBuffer particleBuffer;
	private TextureRegion particleRegion;
		
	boolean renderInterrupted = true;
	
	Sprite sprite, sprite2;
	
	
	public Test1_FrameBufferParticleEffect () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		batch = new SpriteBatch();
		touchPos = new Vector3();
		particleRegion = new TextureRegion();
		background = new Texture(Gdx.files.internal("data/jungle-level.png"));
		
		sprite = new Sprite(new Texture(Gdx.files.internal("data/caveman.png")));
		sprite.setSize(SCENE_WIDTH * 0.3f, SCENE_HEIGHT * 0.7f);
		sprite.setPosition(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.2f);
		sprite.setColor(1, 1, 1, .5f);
		
		sprite2 = new Sprite(sprite);
		sprite2.setX(SCENE_WIDTH * .05f);
		sprite2.setColor(1, 1, 1, .25f);
		
		particleBuffer = new FrameBuffer(Format.RGBA8888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false);
		
		ParticleEffect explosionEffect = new ParticleEffect();
		explosionEffect.load(Gdx.files.internal("data/explosion.particle"), Gdx.files.internal("data"));
		pool = new ParticleEffectPool(explosionEffect, 10, 100);
		activeEffects = new Array<PooledEffect>();
		
		Gdx.input.setInputProcessor(this);
		
//		camera.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f, 0.0f);
		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		Gdx.app.debug("index", "create" );
		renderInterrupted = true;
//		batch.disableBlending();
		}

	@Override
	public void render (float delta) {
//		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		viewport.update(VIRTUAL_WIDTH, VIRTUAL_HEIGHT,true);
		
		batch.setProjectionMatrix(camera.combined);
		
        particleBuffer.bind();
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		sprite.draw(batch);

		for (int i = 0; i < activeEffects.size; ) {
			PooledEffect effect = activeEffects.get(i);
			
			if (effect.isComplete()) {
				pool.free(effect);
				activeEffects.removeIndex(i);
			}
			else {
				effect.draw(batch, delta);
				++i;
			}
		}
		
		batch.end();
		
		particleRegion.setRegion(particleBuffer.getColorBufferTexture());
		particleRegion.flip(false, true);
		
		
		FrameBuffer.unbind();
		
		
	    viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		int width = background.getWidth();
		int height = background.getHeight();
		
		batch.draw(background,
				   0.0f, 0.0f,
				   0.0f, 0.0f,
				   width, height,
				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				   0.0f,
				   0, 0,
				   width, height,
				   false, false);
		
		sprite2.draw(batch);
	
//		width = particleRegion.getRegionWidth();
//		height = particleRegion.getRegionHeight();
//		
//		batch.draw(particleRegion,
//				   0.0f, 0.0f,
//				   0.0f, 0.0f,
//				   width, height,
//				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
//				   0.0f);
		
		batch.draw(particleRegion, 0, 0, SCENE_WIDTH, SCENE_HEIGHT);
		
		
//		for (int i = 0; i < activeEffects.size; ) {
//			PooledEffect effect = activeEffects.get(i);
//			
//			if (effect.isComplete()) {
//				pool.free(effect);
//				activeEffects.removeIndex(i);
//			}
//			else {
//				effect.draw(batch, delta);
//				++i;
//			}
//		}

		
		
		batch.end();
		
		
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
		

		for (PooledEffect effect : activeEffects) {
			effect.dispose();
		}
		
		batch.dispose();
		background.dispose();
		particleBuffer.dispose();
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
        PooledEffect effect = pool.obtain();
		
		if (effect != null) {
			touchPos.set(screenX, screenY, 0.0f);
			
			viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			
			viewport.unproject(touchPos);
			
			activeEffects.add(effect);
			effect.setPosition(touchPos.x, touchPos.y);
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
		// TODO Auto-generated method stub
		return false;
	}
}


