package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Comparator;

public class Test1_ParticleEffects extends ScreenAdapter implements InputProcessor{
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;
	
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private ParticleEffect[] effects;
	private int currentEffect;
	private Vector3 touchPos;
		
	boolean renderInterrupted = true;
	
//	private static final float FRAME_DURATION = 1.0f / 30;
	
	int bgWidth;
	int bgHeight;
	
	TextureAtlas atlas = new TextureAtlas("data/caveman.atlas");
	
	public Test1_ParticleEffects () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		batch = new SpriteBatch();
		touchPos = new Vector3();
		
		effects = new ParticleEffect[8];
		currentEffect = 0;
		
		effects[0] = new ParticleEffect();
		effects[0].load(Gdx.files.internal("data/fire.particle"), Gdx.files.internal("data"));
		
		effects[1] = new ParticleEffect();
//		effects[1].load(Gdx.files.internal("data/stars.particle"), Gdx.files.internal("data"));
		effects[1].load(Gdx.files.internal("data/stars.particle"), atlas);
		
		effects[2] = new ParticleEffect();
		effects[2].load(Gdx.files.internal("data/ice.particle"), Gdx.files.internal("data"));

		effects[3] = new ParticleEffect();
		effects[3].load(Gdx.files.internal("data/555"), Gdx.files.internal("data"));

		effects[4] = new ParticleEffect();
		effects[4].load(Gdx.files.internal("data/555_1"), Gdx.files.internal("data"));

		effects[5] = new ParticleEffect();
		effects[5].load(Gdx.files.internal("data/555_1_a"), Gdx.files.internal("data"));

		effects[6] = new ParticleEffect();
		effects[6].load(Gdx.files.internal("data/555_1_2Emitters"), atlas);
		
		effects[6].findEmitter("e2").setContinuous(true);

		effects[7] = new ParticleEffect();
		effects[7].load(Gdx.files.internal("data/555_3Emitters.particle"), Gdx.files.internal("data"));

		for (ParticleEffect effect : effects) {
			effect.start();
		}
		
		
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
		
		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
		camera.unproject(touchPos);
		
//		effects[6].findEmitter("e2").setPosition(0, 2);
		
		effects[currentEffect].setPosition(touchPos.x, touchPos.y);
		for (ParticleEffect effect : effects) {
			effect.setPosition(touchPos.x, touchPos.y);
			
			if (effect.isComplete()) {
				effect.reset();
			}
		}
		
//		effects[6].findEmitter("e2").setPosition(0, 2);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		effects[currentEffect].draw(batch, Gdx.graphics.getDeltaTime());
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
        
        atlas.dispose();
		
		for (ParticleEffect effect : effects) {
			effect.dispose();
		}
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		currentEffect = (currentEffect + 1) % effects.length;
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


