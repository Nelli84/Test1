package com.test1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameScreen implements Screen{

	 private float worldWidth = 1280;
	    private float worldHeight = 720;
	    private Stage stage;
	    private UIStage uiStage;
	    private Group contentGroup;

	    private Texture texture;

	    public GameScreen() {
	        uiStage = new UIStage();

	        stage = new Stage(new ExtendViewport(worldWidth, worldHeight));
	        worldWidth = stage.getWidth();
	        worldHeight = stage.getHeight();

	        contentGroup = new Group();
//	        Image image = new Image(new Texture("image/bg_pixel.png"));
	        texture = new Texture("map.jpg");
	        Image image = new Image(texture);
	        if(worldWidth > worldHeight){
	            image.setSize(worldWidth, worldWidth / image.getWidth() * image.getHeight());
	        }else{
	            image.setSize(worldHeight / image.getHeight() * image.getWidth(), worldHeight);
	        }

	        contentGroup.setSize(image.getWidth(), image.getHeight());
	        contentGroup.addActor(image);
	        contentGroup.setPosition(stage.getWidth() * .5f, stage.getHeight() * .5f, Align.center);

	        stage.addActor(contentGroup);

	        Gdx.input.setInputProcessor(new InputMultiplexer(new GestureDetector(new ZoomGestureHandler()){
	            @Override
	            public boolean scrolled(int amount) {

	                //Zoom out
	                if (amount > 0 && ((OrthographicCamera)stage.getCamera()).zoom < 1) {
	                    ((OrthographicCamera)stage.getCamera()).zoom += 0.1f;
	                }

	                //Zoom in
	                if (amount < 0 && ((OrthographicCamera)stage.getCamera()).zoom > 0.1) {
	                    ((OrthographicCamera)stage.getCamera()).zoom -= 0.1f;
	                }

	                stage.getCamera().position.x = MathUtils.clamp(stage.getCamera().position.x, stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom * .5f, stage.getWidth() - stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom * .5f);
	                stage.getCamera().position.y = MathUtils.clamp(stage.getCamera().position.y, stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom * .5f, stage.getHeight() - stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom * .5f);

	                return true;
	            }

	        }, uiStage, stage));
	    }

	    @Override
	    public void show() {

	    }

	    @Override
	    public void render(float delta) {
	        ///     146 196 255 == 146 / 255  196 / 255  255 / 255 (libgdx)
	        Gdx.gl.glClearColor(0.5725490196f, 0.76862745098f, 1.0f, 1.0f);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	        stage.act(delta);
	        stage.draw();
	    }

	    @Override
	    public void resize(int width, int height) {
	        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	    }

	    @Override
	    public void pause() {

	    }

	    @Override
	    public void resume() {

	    }

	    @Override
	    public void hide() {

	    }

	    @Override
	    public void dispose() {

	        stage.dispose();
	        uiStage.dispose();
	        texture.dispose();

	    }


	    class ZoomGestureHandler extends GestureDetector.GestureAdapter {

	        public float initialZoom = 1.0f;
	        private float velocityXX;
	        private float velocityYY;
	        private RepeatAction repeatedAction;
	        private boolean fling = false;
	        private boolean finishAction = true;
	        
	        private Action action = new Action() {
	            @Override
	            public boolean act(float delta) {

	                stage.getCamera().position.add(-velocityXX * delta, velocityYY * delta ,0);

	                stage.getCamera().position.x = MathUtils.clamp(stage.getCamera().position.x, stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom * .5f, stage.getWidth() - stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom * .5f);
	                stage.getCamera().position.y = MathUtils.clamp(stage.getCamera().position.y, stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom * .5f, stage.getHeight() - stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom * .5f);


	                velocityXX = velocityXX * .95f;
	                velocityYY = velocityYY * .95f;
	                

	                if(Math.abs(velocityXX) < 10f && Math.abs(velocityYY) < 10){
	                	finishAction = true;
	                	if(repeatedAction != null){
	    	            	repeatedAction.finish();
	    	            }
	                }
	                Gdx.app.debug("myLog",velocityXX+"  "+velocityYY+ "   act -------------------------------------------------"+finishAction);
	                return finishAction;
	            }
	        };


	        @Override
	        public boolean touchDown(float x, float y, int pointer, int button) {

	            initialZoom = ((OrthographicCamera)stage.getCamera()).zoom;
	            
	            finishAction = true;
	            if(repeatedAction != null){
	            	repeatedAction.finish();
	            }

	            Gdx.app.debug("myLog","touchDown -------------------------------------------------"+finishAction);
	            return false;
	        }

	        @Override
	        public boolean zoom(float initialDistance, float distance) {

	        	finishAction = false;
	        	
	            float ratio = initialDistance / distance;

	            ((OrthographicCamera)stage.getCamera()).zoom = MathUtils.clamp(initialZoom * ratio, 0.1f, 1.0f);
	            stage.getCamera().position.x = MathUtils.clamp(stage.getCamera().position.x, stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom * .5f, stage.getWidth() - stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom * .5f);
	            stage.getCamera().position.y = MathUtils.clamp(stage.getCamera().position.y, stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom * .5f, stage.getHeight() - stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom * .5f);

	            Gdx.app.debug("myLog","zoom -------------------------------------------------"+finishAction);

	            return true;
	        }

	        @Override
	        public boolean pan(float x, float y, float deltaX, float deltaY) {

	        	Gdx.app.debug("myLog","pan -------------------------------------------------"+finishAction);
	        	
	            float deltaXX = deltaX * stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom / stage.getViewport().getScreenWidth();
	            float deltaYY = deltaY * stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom / stage.getViewport().getScreenHeight();

	            stage.getCamera().position.add(-deltaXX, deltaYY ,0);
	            stage.getCamera().position.x = MathUtils.clamp(stage.getCamera().position.x, stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom * .5f, stage.getWidth() - stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom * .5f);
	            stage.getCamera().position.y = MathUtils.clamp(stage.getCamera().position.y, stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom * .5f, stage.getHeight() - stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom * .5f);
	            return true;
	        }
	        
	        @Override
	        public boolean panStop(float x, float y, int pointer, int button) {
//	        	if(pointer != 0) {
//	        		return false;
//	        	}
	        	
	        	if(finishAction){
	        		fling = true;
	            }
	        	Gdx.app.debug("myLog","panStop -------------------------------------------------"+finishAction);
	        	return true;
	        }

	        @Override
	        public boolean fling(float velocityX, float velocityY, int button) {
	        	if(!fling){
	        		return false;
	        	}
	        	
	        	fling = false;
	        	finishAction = false;
	        	
	            velocityXX = velocityX * stage.getWidth() * ((OrthographicCamera)stage.getCamera()).zoom / stage.getViewport().getScreenWidth();
	            velocityYY = velocityY * stage.getHeight() * ((OrthographicCamera)stage.getCamera()).zoom / stage.getViewport().getScreenHeight();
	            
	            if(repeatedAction != null){
	            	repeatedAction.finish();
	            }
	            
	            repeatedAction = Actions.forever(action);
	            stage.getRoot().addAction(repeatedAction);
	            Gdx.app.debug("myLog","fling -------------------------------------------------");
	            return true;
	        }
	    }
}

