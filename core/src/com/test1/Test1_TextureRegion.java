package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_TextureRegion extends ScreenAdapter {
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
	private static final float WORLD_TO_SCREEN = .01f ;
	private static final float SCENE_WIDTH = 12.80f;
	private static final float SCENE_HEIGHT = 7.20f;

	private OrthographicCamera camera;
	private Viewport viewport;
	
	private TextureAtlas atlas;
	private TextureRegion backgroundRegion;
	private TextureRegion cavemanRegion;
	private TextureRegion dinosaurRegion;
	private TextureRegion dinosaurRegion_;
	
	
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
	
	public Test1_TextureRegion () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		atlas = new TextureAtlas("atlas_/atlas1.txt");
		cavemanRegion = atlas.findRegion("caveman");
		backgroundRegion = atlas.findRegion("background");
		dinosaurRegion = atlas.findRegion("trex");
//		atlas.addRegion("trex_", dinosaurRegion);
		dinosaurRegion_ = new AtlasRegion((AtlasRegion) dinosaurRegion);
		
		((AtlasRegion)dinosaurRegion).flip(true, false);
		
		cavemanTexture = new Texture("data/caveman_.png");
		cavemanTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		widthT = cavemanTexture.getWidth();
		heightT = cavemanTexture.getHeight();
		originXT = widthT * .5f;
		originYT = heightT * .5f;
	
		width = backgroundRegion.getRegionWidth();
		height = backgroundRegion.getRegionHeight();
		originX = width * .5f;
		originY = height * .5f;
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		Gdx.app.debug("index", "create" + width+ " - "+height);
		renderInterrupted = true;
		
		Sprite sprite;
		
		if(((AtlasRegion)cavemanRegion).rotate){
			((AtlasRegion)cavemanRegion).flip(false, true);
		}else{
			((AtlasRegion)cavemanRegion).flip(true, false);
			
		}
		
		if(((AtlasRegion)dinosaurRegion).rotate){
			
			img = new Image(dinosaurRegion){
				@Override
				public void setRotation(float degrees) {
                    degrees += -90;
					super.setRotation(degrees);
				}
			};
			img.rotateBy(-90);
		}else{
			
			img = new Image(dinosaurRegion);
		}
//		img = new Image(new TextureRegionDrawable(textureRegion));
		img.setSize(SCENE_WIDTH * .1f, SCENE_WIDTH * .1f/ img.getWidth() * img.getHeight() );
		img.setPosition(0, 0, Align.center);
		img.setOrigin(img.getWidth() * .5f, img.getHeight() * .5f );
		
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(backgroundRegion, 
				-originX , -originY, // x, y
				originX, 
				originY,   
				width, 
				height,
				WORLD_TO_SCREEN, WORLD_TO_SCREEN, // scaleX, scaleY
				 0          //  rot (degrees)
				);
//		batch.draw(cavemanTexture, // Texture
//				   -originXT  , -originYT   , // x, y
//				   originXT, originYT,  // originX, originY
//				   widthT, heightT,    //  width, height
//				   WORLD_TO_SCREEN, WORLD_TO_SCREEN, // scaleX, scaleY
//				   0.0f,           //  rot (degrees)
//				   0, 0,          //  srcX, srcY
//				   (int)widthT, (int)heightT, // srcWidth, srcHeight
//				   false, false  // flipX, flipY
//				   );
//		batch.setColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, .5f);
		batch.draw(cavemanRegion, // Texture
				-cavemanRegion.getRegionWidth() * .5f - SCENE_WIDTH * .4f , // x 
				-cavemanRegion.getRegionHeight() * .5f - SCENE_HEIGHT * .28f, // y
				cavemanRegion.getRegionWidth() * .5f , 
				cavemanRegion.getRegionHeight() * .5f,   
				cavemanRegion.getRegionWidth(), 
				cavemanRegion.getRegionHeight(),    //  width, height
				WORLD_TO_SCREEN, WORLD_TO_SCREEN, // scaleX, scaleY
				((AtlasRegion)cavemanRegion).rotate ? -90 : 0          //  rot (degrees)
				);
		batch.draw(dinosaurRegion, // Texture
				-dinosaurRegion.getRegionWidth() * .5f + SCENE_WIDTH * .19f , // x 
				-dinosaurRegion.getRegionHeight() * .5f + SCENE_HEIGHT * .058f, // y
				dinosaurRegion.getRegionWidth() * .5f , 
				dinosaurRegion.getRegionHeight() * .5f,   
				dinosaurRegion.getRegionWidth(), 
				dinosaurRegion.getRegionHeight(),    //  width, height
				WORLD_TO_SCREEN, WORLD_TO_SCREEN, // scaleX, scaleY
				((AtlasRegion)dinosaurRegion).rotate ? -90 : 0          //  rot (degrees)
				);
		batch.draw(dinosaurRegion_, // Texture
				-dinosaurRegion_.getRegionWidth() * .5f + SCENE_WIDTH * .129f , // x 
				-dinosaurRegion_.getRegionHeight() * .5f + SCENE_HEIGHT * .058f, // y
				dinosaurRegion_.getRegionWidth() * .5f , 
				dinosaurRegion_.getRegionHeight() * .5f,   
				dinosaurRegion_.getRegionWidth(), 
				dinosaurRegion_.getRegionHeight(),    //  width, height
				WORLD_TO_SCREEN, WORLD_TO_SCREEN, // scaleX, scaleY
				((AtlasRegion)dinosaurRegion_).rotate ? -90 : 0          //  rot (degrees)
				);
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
}
