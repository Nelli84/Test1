package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Comparator;

public class Test1_SpriteSheetBasedAnimation extends ScreenAdapter implements InputProcessor{
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
	private static final float WORLD_TO_SCREEN = .01f ;
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;

	private OrthographicCamera camera;
	private Viewport viewport;
	
	private TextureAtlas cavemanAtlas;
	private TextureAtlas trexAtlas;
	private Texture background;
	
	private Animation dinosaurWalk;
	private Animation cavemanWalk;
	
	private float animationTime;
	
	private Array<Color> colors;
	private int currentColor;
	
	private Vector3 tmp = new Vector3();
	
	private SpriteBatch batch;
	
	
	boolean renderInterrupted = true;
	
	
	private static final float FRAME_DURATION = 1.0f / 30;
	
	int bgWidth;
	int bgHeight;
	
	public Test1_SpriteSheetBasedAnimation () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		animationTime = 0;
		
		cavemanAtlas = new TextureAtlas("data/caveman.atlas");
		trexAtlas = new TextureAtlas("data/trex.atlas");
		
		background = new Texture(Gdx.files.internal("data/jungle-level.png"));
		
		RegionComparator regionComparator = new RegionComparator();
		Array<AtlasRegion> cavemanRegions = new Array<TextureAtlas.AtlasRegion>(cavemanAtlas.getRegions());
		cavemanRegions.sort(regionComparator);
		
		Array<AtlasRegion> trexRegions = new Array<TextureAtlas.AtlasRegion>(trexAtlas.getRegions());
		trexRegions.sort(regionComparator);
		
		cavemanWalk = new Animation(FRAME_DURATION, cavemanRegions,PlayMode.LOOP);
		dinosaurWalk = new Animation(FRAME_DURATION, trexRegions,PlayMode.LOOP);
		
		
		currentColor = 0;
		
		colors = new Array<Color>();
		colors.add(Color.WHITE);
		colors.add(Color.BLACK);
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		
//		camera.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f, 0.0f);
		
//		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		bgWidth = background.getWidth();
		bgHeight = background.getHeight();
		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		Gdx.app.debug("index", "create" );
		renderInterrupted = true;
		
		Gdx.input.setInputProcessor(this);
		
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		animationTime += delta;
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		tmp = tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(tmp);
		
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background,
				   0.0f, 0.0f,
				   0.0f, 0.0f,
				   bgWidth, bgHeight,
				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				   0.0f,
				   0, 0,
				   bgWidth, bgHeight,
				   false, false);

    	int width;
    	int height;
    	
    	float originX;
    	float originY;
    	
		TextureRegion cavemanFrame = cavemanWalk.getKeyFrame(animationTime);
        width = cavemanFrame.getRegionWidth();
        height = cavemanFrame.getRegionHeight();
		originX = width * .5f;
		originY = height * .5f;
		
		
		batch.draw(cavemanFrame,
				   1.0f - originX, 3.70f - originY,
				   originX, originY,
				   width, height,
				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				   0.0f);
		
//       batch.draw(cavemanWalk.getKeyFrame(animationTime), 100.0f, 275.0f);
		
		TextureRegion dinosaurFrame = dinosaurWalk.getKeyFrame(animationTime);
		width = dinosaurFrame.getRegionWidth();
		height = dinosaurFrame.getRegionHeight();
		originX = width * 0.5f;
		originY = height * 0.5f;
		
		batch.draw(dinosaurFrame,
				   6.75f - originX, 4.70f - originY,
				   originX, originY,
				   width, height,
				   WORLD_TO_SCREEN, WORLD_TO_SCREEN,
				   0.0f);

		
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
		cavemanAtlas.dispose();
		trexAtlas.dispose();
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
		if(button == Buttons.LEFT){
			currentColor = (currentColor + 1) % colors.size;
//			dinosaur.setColor(colors.get(currentColor));
			
			for(TextureRegion region : cavemanWalk.getKeyFrames()){
				region.flip(true, false);
			}
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


