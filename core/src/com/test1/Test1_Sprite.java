package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_Sprite extends ScreenAdapter implements InputProcessor{
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
	private static final float WORLD_TO_SCREEN = .01f ;
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;

	private OrthographicCamera camera;
	private Viewport viewport;
	
	private TextureAtlas atlas;
	private Sprite background;
	private Sprite caveman;
	private Sprite dinosaur;
	private Sprite dinosaur_;
	
	private Array<Color> colors;
	private int currentColor;
	
	private Vector3 tmp = new Vector3();
	
	private SpriteBatch batch;
	private Texture cavemanTexture;
	
	float widthT;
	float heightT;
	float originXT;
	float originYT;
	
	float width;
	float height;
	float originX;
	float originY;
	
	
	boolean renderInterrupted = true;
	
	Image img;
	
	public Test1_Sprite () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		atlas = new TextureAtlas("atlas_/atlas1.txt");
		if(atlas.findRegion("caveman").rotate){
			caveman = new Sprite( atlas.findRegion("caveman")){
				
				@Override
				public void setRotation(float degrees) {
					if(isFlipX() || isFlipY()){
						
						degrees+=90;
					}else{
						degrees-=90;
					}
					super.setRotation(degrees);
				}
			};
			caveman.flip(true, false);
			if(caveman.isFlipX() || caveman.isFlipY()){
				caveman.rotate(90);
			}else{
				caveman.rotate(-90);
			}
		}else{
			caveman = new Sprite( atlas.findRegion("caveman"));
			caveman.flip(true, false);
		}
		
		caveman.setOrigin(caveman.getWidth() * .5f , 
				caveman.getHeight() * .5f);
		caveman.setScale(WORLD_TO_SCREEN);
		caveman.setPosition(-caveman.getRegionWidth() * .5f - SCENE_WIDTH * .4f, -caveman.getRegionHeight() * .5f - SCENE_HEIGHT * .28f);
		
		background =  new Sprite(atlas.findRegion("background"));
		background.setOrigin(background.getWidth() * .5f , 
				background.getHeight() * .5f);
		background.setScale(WORLD_TO_SCREEN);
		background.setPosition(-background.getRegionWidth() * .5f , -background.getRegionHeight() * .5f );
		
		
		dinosaur =  new Sprite(atlas.findRegion("trex"));
		dinosaur.setOrigin(dinosaur.getWidth() * .5f , 
				dinosaur.getHeight() * .5f);
		dinosaur.setScale(WORLD_TO_SCREEN);
//		dinosaur.setPosition(0, 0);
		dinosaur.setPosition(-dinosaur.getWidth() * .5f + SCENE_WIDTH * .19f , // x 
				-dinosaur.getHeight() * .5f + SCENE_HEIGHT * .058f );
		
//		atlas.addRegion("trex_", dinosaurRegion);
		dinosaur_ = new Sprite(dinosaur);
		dinosaur_.setOrigin(dinosaur_.getWidth() * .5f , 
				dinosaur_.getHeight() * .5f);
		dinosaur_.setScale(WORLD_TO_SCREEN);
		dinosaur_.setPosition(-dinosaur_.getRegionWidth() * .5f + SCENE_WIDTH * .129f , // x 
				-dinosaur_.getRegionHeight() * .5f + SCENE_HEIGHT * .058f );
		
//		dinosaur.setSize(dinosaur.getWidth() * .01f, dinosaur.getHeight() * .01f);
		dinosaur.flip(true, false);

		
		
		currentColor = 0;
		
		colors = new Array<Color>();
		colors.add(Color.WHITE);
		colors.add(Color.BLACK);
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		
		
		cavemanTexture = new Texture("data/caveman_.png");
		cavemanTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		widthT = cavemanTexture.getWidth();
		heightT = cavemanTexture.getHeight();
		originXT = widthT * .5f;
		originYT = heightT * .5f;
	
		width = background.getRegionWidth();
		height = background.getRegionHeight();
		originX = width * .5f;
		originY = height * .5f;
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		Gdx.app.debug("index", "create" + width+ " - "+height);
		renderInterrupted = true;
		
		
		img = new Image(dinosaur_);
//		img = new Image(new TextureRegionDrawable(textureRegion));
		img.setSize(SCENE_WIDTH * .1f, SCENE_WIDTH * .1f/ img.getWidth() * img.getHeight() );
		img.setPosition(0, 0, Align.center);
		img.setOrigin(img.getWidth() * .5f, img.getHeight() * .5f );
		img.rotateBy(-90);
		
		Gdx.input.setInputProcessor(this);
		
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tmp = tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(tmp);
		caveman.setPosition(tmp.x - caveman.getWidth() * .5f, tmp.y - caveman.getHeight() * .5f);
		
		if(caveman.getBoundingRectangle().overlaps(dinosaur.getBoundingRectangle())){
			dinosaur.setColor(Color.MAGENTA);
		}
		batch.begin();
		background.draw(batch);

		
		caveman.draw(batch);
		dinosaur.draw(batch);
		dinosaur_.draw(batch);
		
//		batch.setColor(Color.WHITE);
		img.draw(batch, 1);
		
		
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
		atlas.dispose();
//		cavemanTexture.dispose();
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
			dinosaur.setColor(colors.get(currentColor));
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
		if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
			caveman.scale(amount * .005f);
		}else{
			caveman.rotate(amount * .5f);
		}
		
//		dinosaur_.scroll(amount * .005f, amount * .005f);
		return false;
	}
}

