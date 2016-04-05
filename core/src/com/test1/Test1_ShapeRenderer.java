package com.test1;

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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test1_ShapeRenderer extends ScreenAdapter implements InputProcessor{
	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1); 
//	private static final float WORLD_TO_SCREEN = .01f ;
//	private static final float SCENE_WIDTH = 12.80f;
//	private static final float SCENE_HEIGHT = 7.20f;
//	
	private static final float SCENE_WIDTH = 40.0f;
	private static final float SCENE_HEIGHT = 22.50f;
	
		
	boolean renderInterrupted = true;
	
	private OrthographicCamera camera;
	private Viewport viewport;
	private ShapeRenderer shapeRenderer;

	private boolean drawGrid = true;
	private boolean drawFunction = true;
	private boolean drawCircles = true;
	private boolean drawRectangles = true;
	private boolean drawPoints = true;
	private boolean drawTriangles = true;
	
//	private float debugFunction[] = {2,5,  -1,3, 5,1};
	private float debugFunction[] ;//= {-3,9,  -2,4, -1,1};
	
	
	int bgWidth;
	int bgHeight;
	
	Image img = new Image(new Texture("data/caveman_.png"));
	Stage stage;
	private SpriteBatch batch;
	private Texture levelTexture;
	
	public Test1_ShapeRenderer () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
		
		stage = new Stage(viewport);
		
		batch = new SpriteBatch();
		
        levelTexture = new Texture(Gdx.files.internal("data/jungle-level.png"));
		
		levelTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		img.setSize(10, 10);
		
		stage.addActor(img);
		
        shapeRenderer = new ShapeRenderer();
		
		debugFunction = new float[40];
		
//		for (int x = -10; x < 10; ++x) {
//			int i = (x + 10) * 2; 
//			debugFunction[i] = x;
//			debugFunction[i + 1] = x * x;
//		}
		
		int i = 0;
		for (int x = -10; x < 10; ++x) {
			 
			debugFunction[i] = x;
			debugFunction[++i] = x * x ;
			i++;
		}
		
		camera.position.set(0.0f, 0.0f, 0.0f);
		
		Gdx.app.log("index", "G: toggle grid");
		Gdx.app.log("index", "F: toggle function y = x^2");
		Gdx.app.log("index", "C: toggle circles");
		Gdx.app.log("index", "R: toggle rectangles");
		Gdx.app.log("index", "P: toggle points");
		Gdx.app.log("index", "T: toggle triangles");
		
		Gdx.input.setInputProcessor(this);
		renderInterrupted = true;
		
//		shapeRenderer.setAutoShapeType(true);
	}

	@Override
	public void render (float delta) {
//		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClearColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
//		stage.draw();
		
		//
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
//		batch.draw(levelTexture, 0, 0);
		batch.draw(levelTexture,
				   0.0f, 0.0f,
				   0.0f, 0.0f,
				   levelTexture.getWidth(), levelTexture.getHeight(),
				   .01f, .01f,
				   0.0f,
				   0, 0,
				   levelTexture.getWidth(), levelTexture.getHeight(),
				   false, false);
		
		batch.end();
		stage.draw();
		
		drawDebugGraphics();
		
		
		
		if(renderInterrupted){
			Gdx.app.debug("index", "render");
			renderInterrupted = false;
		}
	}
	
	private void drawDebugGraphics() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		// Draw grid
		if (drawGrid) {
			shapeRenderer.begin(ShapeType.Line);
//			shapeRenderer.begin();

//
//			batch.setProjectionMatrix(camera.combined);
//			batch.begin();
////			batch.draw(levelTexture, 0, 0);
//			batch.draw(levelTexture,
//					   0.0f, 0.0f,
//					   0.0f, 0.0f,
//					   levelTexture.getWidth(), levelTexture.getHeight(),
//					   .01f, .01f,
//					   0.0f,
//					   0, 0,
//					   levelTexture.getWidth(), levelTexture.getHeight(),
//					   false, false);
//			
//			batch.end();
//			stage.draw();
			
			
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.line(-SCENE_WIDTH, 0.0f, SCENE_WIDTH, 0.0f);
			shapeRenderer.line(0.0f, -SCENE_HEIGHT, 0.0f, SCENE_HEIGHT);
			
			shapeRenderer.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a);
			
			for (int i = (int) - Math.ceil(SCENE_HEIGHT * 2); i <= (int)  Math.ceil(SCENE_HEIGHT * 2); ++i) {
				if (i == 0)
					continue;
				
				shapeRenderer.line(-SCENE_WIDTH, i, SCENE_WIDTH, i);
			}
			
			for (int i = -(int)  Math.ceil(SCENE_WIDTH * 2); i <= (int) Math.ceil(SCENE_WIDTH * 2); ++i) {
				if (i == 0)
					continue;
				
				shapeRenderer.line(i, -SCENE_HEIGHT, i, SCENE_HEIGHT);
			}
			
			shapeRenderer.end();
		}
		
		if (drawFunction) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.ORANGE);
			shapeRenderer.polyline(debugFunction);
			shapeRenderer.end();
		}
		
		if (drawCircles) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.CYAN);
			
			shapeRenderer.circle(5.2f, 3.1f, 2.3f, 8);
			shapeRenderer.circle(-5.3f, 7.1f, 1.1f, 30);
			shapeRenderer.circle(12.4f, -6.4f, 1.75f, 30);

			shapeRenderer.setColor(Color.GREEN);
			
			shapeRenderer.rect(10, 0f,  2f, 1f,   4f, 2f,  1f, 1f,   .0f);

			shapeRenderer.setColor(Color.CYAN);
		
			shapeRenderer.circle(-9.1f, -5.8f, 2.25f, 30);
			
			shapeRenderer.end();
		}
		
		
		if (drawRectangles) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.MAROON);
		shapeRenderer.rect(10f, 0f,  2f, 1f,   4f, 2f,  1f, 1f,   45.0f);
		shapeRenderer.setColor(Color.OLIVE);
		shapeRenderer.rect(10, 0,  0f, 0f,   4f, 2f,  1f, 1f,   45.0f);
//		shapeRenderer.rect(7.2f, 2.4f, 3.3f, 2.8f, 0.0f, 0.0f, 45.0f);
//		shapeRenderer.rect(-8.4f, 3.8f, 6.1f, 2.3f, 0.0f, 0.0f, 75.0f);
//		shapeRenderer.rect(-4.2f, -3.4f, 3.3f, 2.8f, 0.0f, 0.0f, 25.0f);
//		shapeRenderer.rect(3.2f, -6.4f, 3.9f, 1.8f, 0.0f, 0.0f, 60.0f);
		
		shapeRenderer.end();
	}
		
		if (drawPoints) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.OLIVE);
			
			shapeRenderer.x(0.0f, -2.0f, .35f);
			shapeRenderer.setColor(Color.MAGENTA);

			shapeRenderer.x(-5.0f, 0.0f, 0.25f);
			shapeRenderer.x(3.0f, 8.0f, 0.25f);
			shapeRenderer.x(-7.0f, 2.0f, 0.25f);
			shapeRenderer.x(7.0f, -3.0f, 0.25f);
			
			shapeRenderer.end();
		}
		if (drawPoints) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.YELLOW);
			
			shapeRenderer.point(-10.0f, 0.0f, 0f);

			shapeRenderer.end();
		}
		
		if (drawTriangles) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			
			shapeRenderer.triangle(-16.1f, -5.2f, -14.0f, -2.1f, -13.4f, 3.8f);
			
			shapeRenderer.end();
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
		
		shapeRenderer.dispose();
		
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.G) {
			drawGrid = !drawGrid;
		}
		else if (keycode == Keys.F) {
			drawFunction = !drawFunction;
		}
		else if (keycode == Keys.C) {
			drawCircles = !drawCircles;
		}
		else if (keycode == Keys.R) {
			drawRectangles = !drawRectangles;
		}
		else if (keycode == Keys.P) {
			drawPoints = !drawPoints;
		}
		else if (keycode == Keys.T) {
			drawTriangles = !drawTriangles;
		}
		
		return true;
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
		return false;
	}
	
}


