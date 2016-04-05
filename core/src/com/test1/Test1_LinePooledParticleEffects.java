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
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_LinePooledParticleEffects extends ScreenAdapter implements InputProcessor{
	private static final float SCENE_WIDTH = 1280f;
	private static final float SCENE_HEIGHT = 720f;
	
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private ParticleEffectPool pool;
	private Array<PooledEffect> activeEffects;
	private Vector3 touchPos;
	
		
	boolean renderInterrupted = true;
	
//	private static final float FRAME_DURATION = 1.0f / 30;
	
	
	public Test1_LinePooledParticleEffects () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		batch = new SpriteBatch();
		touchPos = new Vector3();
		
		ParticleEffect explosionEffect = new ParticleEffect();
		explosionEffect.load(Gdx.files.internal("data/line_.particle"), Gdx.files.internal("data"));
		pool = new ParticleEffectPool(explosionEffect, 10, 100);
		activeEffects = new Array<PooledEffect>();
		
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
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for (int i = 0; i < activeEffects.size; ) {
			PooledEffect effect = activeEffects.get(i);
			
			if (effect.isComplete()) {
//				pool.free(effect);
//				activeEffects.removeIndex(i);
				effect.reset();
			}
			else {
				effect.draw(batch, delta);
				++i;
			}
		}
		
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
		
	}
	
	@Override
	public void hide() {
		dispose();
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
		if(character == 'a' && currentEffect != null){
			ParticleEmitter emitter = currentEffect.findEmitter("lineParticle");
			Vector2 tmp = new Vector2(emitter.getX(), emitter.getY());
			
			emitter.getSpawnWidth().setHighMax(tmp.dst(SCENE_WIDTH * .5f, SCENE_HEIGHT * .5f));
		}
		return false;
	}

	PooledEffect currentEffect;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        PooledEffect effect = pool.obtain();
        currentEffect = effect;
		
		if (effect != null) {
			touchPos.set(screenX, screenY, 0.0f);
			viewport.unproject(touchPos);
			
			activeEffects.add(effect);
			effect.setPosition(touchPos.x, touchPos.y);
			
			ParticleEmitter emitter = effect.findEmitter("lineParticle");
//			Vector2 tmp = new Vector2(emitter.getX(), emitter.getY());
			
			emitter.getSpawnWidth().setHigh( SCENE_WIDTH * .5f - emitter.getX());
			emitter.getSpawnHeight().setHigh( SCENE_HEIGHT * .5f - emitter.getY());
			Gdx.app.debug("index", " emitter.x = "+emitter.getX());
			Gdx.app.debug("index", " emitter.y = "+emitter.getY());
			Gdx.app.debug("index", "  Math.abs(SCENE_HEIGHT * .5f - emitter.getY() = "+
			Math.abs(SCENE_HEIGHT * .5f - emitter.getY()));
//			emitter.getSpawnWidth().setHigh(100);
//			emitter.getSpawnHeight().setHigh(100);
//			emitter.getSpawnWidth().setLow(0);
//			emitter.getSpawnHeight().setLow(0);
			emitter.getTint().setColors(new float[]{1f, 1f, 1f});
			effect.reset();
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
	
	}


