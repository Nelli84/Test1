package box2d_;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import aurelienribon.bodyeditor.BodyEditorLoader;

public class Test1_Box2d_1 implements Screen{
	
	public static final float WORLD_TO_SCREEN = .01f;
	private static final float WORLD_WIDTH = 12.8f;
	private static final float WORLD_HEIGHT = 7.2f;
	
	private static final Color BACKGROUND_COLOR = new Color(.2f, .92f, .2f, 1);
//	private static final Color BACKGROUND_COLOR = new Color(.39f, .58f, .92f, 1);
	
	
	private Box2DDebugRenderer debugRenderer;
	private World world;
	
	private Stage stage;
	private Stage uiStage;
	
	private ImageObjct ball;

	
	private float deltaX = .1038f;
	private Vector2 leftForce = new Vector2(-deltaX, 0);
	private Vector2 rightForce = new Vector2(deltaX, 0);
	
	private Texture bgTexture;
//	private Image bg1;
//	private Image bg2;
	private Image bg;
	
	private Vector2 bottomLeft;
	private Vector2 bottomRight;
	private Vector2 topLeft;
	private Vector2 topRight;

	private Vector2 direction = new Vector2();
	private Vector3 tmp = new Vector3();
	private boolean setScaleOne = false; 
	
	private Texture LeftTexture = new Texture(Gdx.files.internal("box2d/left_button.png"));
	private Texture rightTexture = new Texture(Gdx.files.internal("box2d/right_button.png"));
	private Texture upTexture = new Texture(Gdx.files.internal("box2d/up_button.png"));
	private Texture downTexture = new Texture(Gdx.files.internal("box2d/down_button.png"));
	private Texture grayTexture = new Texture(Gdx.files.internal("box2d/gray_button.png"));
	
	Runnable runnableLeft;
	Runnable runnableRight;
	Runnable runnableGoLeft = new Runnable() {
		
		@Override
		public void run() {

			goLeft();
		}
	};
	Runnable runnableGoRight = new Runnable() {
		
		@Override
		public void run() {

			goRight();
		}
	};
	
	Runnable runnable2;
	Runnable runnableBlow = new Runnable() {
		
		@Override
		public void run() {
			blow();
			
		}
	};
	Runnable runnableGoDown = new Runnable() {
		
		@Override
		public void run() {

			goDown();
		}
	};
	@Override
	public void show() {
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, -10), true);
		
		stage = new Stage(new FillViewport(WORLD_WIDTH, WORLD_HEIGHT), new PolygonSpriteBatch());
		
		uiStage = new Stage(new ScreenViewport());
		uiStage.getRoot().setSize(uiStage.getWidth(), uiStage.getHeight());
		
		final Button leftBtn = new Button(new TextureRegionDrawable(new TextureRegion(LeftTexture)));
		leftBtn.setSize(uiStage.getHeight() * .18f, uiStage.getHeight() * .18f);
		leftBtn.setPosition(uiStage.getWidth() * .08f, uiStage.getHeight() * .1f, Align.center);
		leftBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				runnableLeft = runnableGoLeft;
				leftBtn.setColor(Color.GRAY);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				runnableLeft = null;		
				leftBtn.setColor(Color.WHITE);
			}
		});
		uiStage.addActor(leftBtn);
		
		final Button rightBtn = new Button(new TextureRegionDrawable(new TextureRegion(rightTexture)));
		rightBtn.setSize(uiStage.getHeight() * .228f, uiStage.getHeight() * .228f);
		rightBtn.setPosition(uiStage.getWidth() * .28f, uiStage.getHeight() * .1f, Align.center);
		rightBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				runnableRight = runnableGoRight;
				rightBtn.setColor(Color.GRAY);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				runnableRight = null;	
				rightBtn.setColor(Color.WHITE);
				}
			    
		});
		uiStage.addActor(rightBtn);
		
		final Button upBtn = new Button(new TextureRegionDrawable(new TextureRegion(upTexture)));
		upBtn.setSize(uiStage.getHeight() * .16f, uiStage.getHeight() * .16f);
		upBtn.setPosition(uiStage.getWidth() * .92f, uiStage.getHeight() * .1f, Align.center);
		upBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				runnable2 = runnableBlow;
				upBtn.setColor(Color.GRAY);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				runnable2 = null;	
				for(Body b : bodies){
					b.setGravityScale(1);
				}
				ball.body.setGravityScale(1);

				upBtn.setColor(Color.WHITE);
			}
			
		});
		uiStage.addActor(upBtn);

		final Button downBtn = new Button(new TextureRegionDrawable(new TextureRegion(downTexture)));
		downBtn.setSize(uiStage.getHeight() * .16f, uiStage.getHeight() * .16f);
		downBtn.setPosition(uiStage.getWidth() * .78f, uiStage.getHeight() * .1f, Align.center);
		downBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				runnable2 = runnableGoDown;
				downBtn.setColor(Color.GRAY);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				runnable2 = null;	
				for(Body b : bodies){
					b.setGravityScale(1);
				}
				ball.body.setGravityScale(1);

				downBtn.setColor(Color.WHITE);
			}
			
		});
		uiStage.addActor(downBtn);
		
		uiStage.addListener(new InputListener(){
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y,
//					int pointer, int button) {
//				boolean bool = false;
//				if ( y < uiStage.getHeight() * .5f) {
//					Gdx.app.debug("index", "blow ");
//					if (x >= uiStage.getWidth() * .9f) {
//						Gdx.app.debug("index", "blow x> stage.getWidth() * .8f");
//						runnable2 = runnableBlow;
//						setScaleOne = true;
//						
//					} else if (x >= uiStage.getWidth() * .8f) {
//						Gdx.app.debug("index", "blow x> stage.getWidth() * .6f");
//						runnable2 = runnableGoDown;
//						setScaleOne = true;
//					}
//					
//									
//					bool = true;
//				}
//				return bool;
//			}
//			@Override
//			public void touchUp(InputEvent event, float x, float y,
//					int pointer, int button) {
//				Gdx.app.debug("index", "touchUp ?????? [" + y);
//				if(setScaleOne){
//					for(Body b : bodies){
//						b.setGravityScale(1);
//					}
//					ball.body.setGravityScale(1);
//					runnable2 = null;
//					setScaleOne = false;
//				}
////				runnable1 = null;
//			}
			
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				
				if(keycode == Keys.UP || keycode == Keys.DOWN){
					for(Body b : bodies){
						b.setGravityScale(1);
					}
					ball.body.setGravityScale(1);
				}
				
				return super.keyUp(event, keycode);
			}
			
		});
		
		bgTexture = new Texture(Gdx.files.internal("box2d/bg5.png"));
		bg = new Image(bgTexture);
		float scale = 1.2f;
		bg.setSize(bg.getWidth() * WORLD_TO_SCREEN * scale, bg.getHeight() * WORLD_TO_SCREEN * scale);
		
		stage.addActor(bg);

		int coef = 5;
		bottomLeft = new Vector2(0, 0);
		bottomRight = new Vector2(bg.getWidth() , 0);
		topLeft = new Vector2(0, bg.getHeight() * coef);
		topRight = new Vector2(bg.getWidth(), bg.getHeight() * coef);
		createGround();
		
//		createGround();
        createWorldEdge();
//		createSoftBall_();
		createSoftBall_SKeleton();
//		createSoftElipse();
		Gdx.input.setInputProcessor(uiStage);
	}

	private Vector3 touch = new Vector3(); 
	
	@Override
	public void render(float delta) {
//		Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
		Gdx.gl.glClearColor(Color.OLIVE.r,Color.OLIVE.g, Color.OLIVE.b, Color.OLIVE.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(runnableRight != null){
			runnableRight.run();
		}
		if(runnableLeft != null){
			runnableLeft.run();
		}
		
		if(runnable2 != null){
			runnable2.run();
		}

		
		if(Gdx.input.isKeyPressed(Keys.UP)){
			blow();	
		}
		
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			goDown();
		}

		if(Gdx.input.isKeyPressed(Keys.LEFT)){
              goLeft();

		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			goRight();
//			Gdx.app.debug("index", "isKeyPressed Right [" + ball.body.getLinearVelocity().x + " , " +ball.body.getLinearVelocity().y + "]");
		}
		
		world.step(1 / 60.0f, 8, 3);
		stage.act(delta);

		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		stage.draw();
		
		debugRenderer.render(world, stage.getCamera().combined);
		uiStage.act(delta);
		
		uiStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		uiStage.draw();
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		dispose();
		
	}

	@Override
	public void dispose() {
		world.dispose();
		stage.dispose();
		bgTexture.dispose();
		uiStage.dispose();

		LeftTexture.dispose();
		rightTexture.dispose();
		upTexture.dispose();
		downTexture.dispose();
		grayTexture.dispose();
	}

	private float bottomHeight = 156 * WORLD_TO_SCREEN;
	
	private void createGround(){
//		bgTexture = new Texture(Gdx.files.internal("box2d/bg5.png"));
//		bg = new Image(bgTexture);
//		bg.setSize(bg.getWidth() * WORLD_TO_SCREEN, bg.getHeight() * WORLD_TO_SCREEN);
//		
//		stage.addActor(bg);
		
		BodyDef gr1BDef = new BodyDef();
		gr1BDef.type = BodyType.StaticBody;
//		gr1BDef.position.x = bg.getWidth() * .495f;
		
		FixtureDef gr1FDef = new FixtureDef();
//		Vector2 v1 = new Vector2(0, bottomHeight);
//		Vector2 v2 = new Vector2(WORLD_WIDTH * 10, bottomHeight);
//		
//		EdgeShape bottomEdge = new EdgeShape();
//		bottomEdge.set(v1, v2);
//		
//		gr1FDef.shape = bottomEdge;
		gr1FDef.friction = .1f;
		
		Body gr1Body = world.createBody(gr1BDef);
//		gr1Body.createFixture(gr1FDef);

		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("box2d/map.json"));
		
	    // 4. Create the body fixture automatically by using the loader.
	    loader.attachFixture(gr1Body, "background", gr1FDef, bg.getWidth());
	}
	private void createBall(){
		float radius = 35 * WORLD_TO_SCREEN;
		
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.DynamicBody;
		bDef.position.set(400 * WORLD_TO_SCREEN + radius, radius + bottomHeight);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		FixtureDef fDef = new FixtureDef();
		
		fDef.shape = circle;
		fDef.friction = .1f;
		fDef.density = .3f;
		fDef.friction = .4f;
		fDef.restitution = 0;
		
		Body body = world.createBody(bDef);
		body.createFixture(fDef);
		body.setFixedRotation(true);
		// 0. Create a loader for the file saved from the editor.
//		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("box2d/test.json"));
//		
//	    // 4. Create the body fixture automatically by using the loader.
//	    loader.attachFixture(body, "ball", fDef, 2 * radius);
		
		ball = new BallObject(new Texture("box2d/ball.png"));
		ball.body = body;
		ball.stage = stage;
		ball.totalWidth = bg.getWidth();
		ball.totalHeight = bg.getHeight();
		ball.setSize(radius * 2.3f, radius * 2.3f);
		ball.setOrigin(ball.getWidth() * .5f, ball.getHeight() * .5f);
		ball.setPosition(200 * WORLD_TO_SCREEN + radius, radius + bottomHeight, Align.center);
		
		stage.addActor(ball);
		
		
	}
	
	private Array<Body> bodies = new Array<Body>();
	private int segmentsCount = 18;
	private void createSoftBall_(){
		 // Center is the position of circle that is in the center
	    Vector2 center = new  Vector2(580 * WORLD_TO_SCREEN, 184 * WORLD_TO_SCREEN);
	    
	    float radius_ =  5f * WORLD_TO_SCREEN;
	    
	    // Radius of the wheel
	    float radius = 35;
	    

	    CircleShape circleShape = new CircleShape();
	    
	    
//	    circleShape .setRadius(radius);
//	    FixtureDef fDef = new FixtureDef();
//	    fDef.shape = circleShape;
//	    fDef.density = 0;
	    
	    circleShape.setRadius(radius_);
	    
	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = circleShape;
	    fixtureDef.density = 0.1f;
//	    fixtureDef.restitution = 0.05f;
	    fixtureDef.friction = .03f;
	    
	    // The greater the number, the more springy
	    float springiness = 5.0f;
	    
	    // Delta angle to step by
	    float deltaAngle = (float) ((2.f * Math.PI) / segmentsCount);
	    

	    // Need to store the bodies so that we can refer back
	    // to it when we connect the joints
	    
	    // For each segment...
	    for (int i = 0; i < segmentsCount; i++) {
	        // Current angle
	        float theta = deltaAngle*i;
	        
//	        if(i >= 8 && i <=  10){
//	        	fixtureDef.density = 0.3f;
//	        }
	        
	        // Calcualte the x and y based on theta 
	        float x = (float) (radius*Math.cos(theta));
	        float y = (float) (radius*Math.sin(theta));
	        
	        // Remember to divide by PTM_RATIO to convert to Box2d coordinates
	        Vector2 circlePosition =  new Vector2(x * WORLD_TO_SCREEN, y* WORLD_TO_SCREEN);
	        
	        BodyDef bodyDef = new BodyDef();
	        bodyDef.fixedRotation = true;
	        bodyDef.type = BodyType.DynamicBody;
	        // Position should be relative to the center
	        bodyDef.position.set(center.cpy().add(circlePosition));//(center + circlePosition);
	        
	        // Create the body and fixture
	        Body body;
	        body = world.createBody(bodyDef);
	        body.setUserData("bodies");
	        body.createFixture(fixtureDef);
	        
	        // Add the body to the array to connect joints to it
	        // later. b2Body is a C++ object, so must wrap it
	        // in NSValue when inserting into it NSMutableArray
//	        [bodies addObject:[NSValue valueWithPointer:body]];
	        bodies.add(body);
	        
	    }
	    
	    // Circle at the center (inner circle)
	    BodyDef innerCircleBodyDef = new BodyDef();
//	    innerCircleBodyDef.fixedRotation = true;
	    innerCircleBodyDef.type = BodyType.DynamicBody;    
	    // Position is at the center
	    innerCircleBodyDef.position.set(center);    
	    Body innerCircleBody = world.createBody(innerCircleBodyDef);
	    innerCircleBody.setUserData("bodies");
	    
	    float scale = 3.8f;
	    
	    ((CircleShape)fixtureDef.shape).setRadius(radius_ * scale);
	    fixtureDef.density = fixtureDef.density / scale;
	    innerCircleBody.createFixture(fixtureDef);
	    
	    // Connect the joints    
	    DistanceJointDef jointDef = new DistanceJointDef();
	    for (int i = 0; i < segmentsCount; i++) {
	        // The neighbor.
	        int neighborIndex = (i + 1) % segmentsCount;
	        
	        // Get the current body and the neighbor
//	        b2Body *currentBody = (b2Body*)[[bodies objectAtIndex:i] pointerValue];
//	        b2Body *neighborBody = (b2Body*)[[bodies objectAtIndex:neighborIndex] pointerValue];

	        Body currentBody = bodies.get(i);
	        Body neighborBody = bodies.get(neighborIndex);

	        
	        // Connect the outer circles to each other
	        jointDef.initialize(currentBody, neighborBody,
	                            currentBody.getWorldCenter(), 
	                            neighborBody.getWorldCenter() );
	        jointDef.collideConnected = true;
	        jointDef.frequencyHz = springiness;
	        jointDef.dampingRatio = 0.5f;
	        
	        world.createJoint(jointDef);
	        
	        //////////////////////////////////
	        
//	        RopeJointDef wJ = new RopeJointDef();
//			
//			wJ.bodyA = currentBody;
//			wJ.bodyB = innerCircleBody;
//		    wJ.localAnchorA.set(0, 0);
//		    wJ.localAnchorB.set(0, 0);
//		
//			wJ.maxLength = innerCircleBody.getPosition().cpy().sub(currentBody.getPosition()).len();
//	        
//			world.createJoint(wJ);
	        ////////////////////////////////
	        
           float theta = deltaAngle*i;
	        
	        // Calcualte the x and y based on theta 
	        float x = (float) (Math.cos(theta));
	        float y = (float) (Math.sin(theta));
	        
          PrismaticJointDef pistJ = new PrismaticJointDef();
		    
		    pistJ.bodyA = innerCircleBody;
		    pistJ.bodyB = currentBody;
		    pistJ.localAnchorA.set(0, 0);
		    pistJ.localAnchorB.set(0, 0);
//		    pistJ.localAnchorA.set(1, 0);
		    pistJ.localAxisA.set(x, y);
	        
		    world.createJoint(pistJ);
		    
		    
	        
	        // Connect the center circle with other circles        
	        jointDef.initialize(currentBody, innerCircleBody, currentBody.getWorldCenter(), center);
	        jointDef.collideConnected = true;
	        jointDef.frequencyHz = springiness;
	        jointDef.dampingRatio = 0.5f;
	        
	        world.createJoint(jointDef);

	        
//	        RevoluteJointDef ristJ = new RevoluteJointDef();
//	        ristJ.bodyA = innerCircleBody;
//	        ristJ.bodyB = currentBody;
////		    ristJ.localAnchorA.set(currentBody.getWorldCenter());
//	        ristJ.localAnchorA.set(innerCircleBody.getLocalPoint(currentBody.getWorldCenter()));
//	        ristJ.localAnchorB.set(0, 0);
////		    ristJ.upperAngle = 10 * MathUtils.degreesToRadians;
//	        world.createJoint(ristJ);
	    }
	    
	    ball = new BallObject(new Texture("box2d/ball.png"));
		ball.body = innerCircleBody;
		ball.stage = stage;
		ball.totalWidth = bg.getWidth();
		ball.totalHeight = bg.getHeight();
		ball.setSize(radius_ * 2.3f * scale, radius_ * 2.3f * scale);
		ball.setOrigin(ball.getWidth() * .5f, ball.getHeight() * .5f);
		
		ball.setPosition(innerCircleBody.getPosition().x, innerCircleBody.getPosition().y, Align.center);
		ball.setRotation(innerCircleBody.getAngle() * MathUtils.radiansToDegrees);
		
		stage.addActor(ball);
	}
	private void createSoftBall_SKeleton(){
		// Center is the position of circle that is in the center
		Vector2 center = new  Vector2(580 * WORLD_TO_SCREEN, 184 * WORLD_TO_SCREEN);
		
		float radius_ =  5f * WORLD_TO_SCREEN;
		
		// Radius of the wheel
		float radius = 35;
		
		
		CircleShape circleShape = new CircleShape();
		
		
//	    circleShape .setRadius(radius);
//	    FixtureDef fDef = new FixtureDef();
//	    fDef.shape = circleShape;
//	    fDef.density = 0;
		
		circleShape.setRadius(radius_);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.1f;
//	    fixtureDef.restitution = 0.05f;
		fixtureDef.friction = .03f;
		
		// The greater the number, the more springy
		float springiness = 5.0f;
		
		// Delta angle to step by
		float deltaAngle = (float) ((2.f * Math.PI) / segmentsCount);
		
		
		// Need to store the bodies so that we can refer back
		// to it when we connect the joints
		
		// For each segment...
		for (int i = 0; i < segmentsCount; i++) {
			// Current angle
			float theta = deltaAngle*i;
			
//	        if(i >= 8 && i <=  10){
//	        	fixtureDef.density = 0.3f;
//	        }
			
			// Calcualte the x and y based on theta 
			float x = (float) (radius*Math.cos(theta));
			float y = (float) (radius*Math.sin(theta));
			
			// Remember to divide by PTM_RATIO to convert to Box2d coordinates
			Vector2 circlePosition =  new Vector2(x * WORLD_TO_SCREEN, y* WORLD_TO_SCREEN);
			
			BodyDef bodyDef = new BodyDef();
			bodyDef.fixedRotation = true;
			bodyDef.type = BodyType.DynamicBody;
			// Position should be relative to the center
			bodyDef.position.set(center.cpy().add(circlePosition));//(center + circlePosition);
			
			// Create the body and fixture
			Body body;
			body = world.createBody(bodyDef);
			body.setUserData("bodies");
			body.createFixture(fixtureDef);
			
			// Add the body to the array to connect joints to it
			// later. b2Body is a C++ object, so must wrap it
			// in NSValue when inserting into it NSMutableArray
//	        [bodies addObject:[NSValue valueWithPointer:body]];
			bodies.add(body);
			
		}
		
		// Circle at the center (inner circle)
		BodyDef innerCircleBodyDef = new BodyDef();
//	    innerCircleBodyDef.fixedRotation = true;
		innerCircleBodyDef.type = BodyType.DynamicBody;    
		// Position is at the center
		innerCircleBodyDef.position.set(center);    
		Body innerCircleBody = world.createBody(innerCircleBodyDef);
		innerCircleBody.setUserData("bodies");
		
		float scale = 3.8f;
		
		((CircleShape)fixtureDef.shape).setRadius(radius_ * scale);
		fixtureDef.density = fixtureDef.density / scale;
		innerCircleBody.createFixture(fixtureDef);
		
		ball = new BallObjectSkeleton(bodies, innerCircleBody);
		ball.stage = stage;
		ball.totalWidth = bg.getWidth();
		ball.totalHeight = bg.getHeight();
		
		// Connect the joints    
		DistanceJointDef jointDef = new DistanceJointDef();
		for (int i = 0; i < segmentsCount; i++) {
			// The neighbor.
			int neighborIndex = (i + 1) % segmentsCount;
			
			// Get the current body and the neighbor
//	        b2Body *currentBody = (b2Body*)[[bodies objectAtIndex:i] pointerValue];
//	        b2Body *neighborBody = (b2Body*)[[bodies objectAtIndex:neighborIndex] pointerValue];
			
			Body currentBody = bodies.get(i);
			Body neighborBody = bodies.get(neighborIndex);
			
			
			// Connect the outer circles to each other
			jointDef.initialize(currentBody, neighborBody,
					currentBody.getWorldCenter(), 
					neighborBody.getWorldCenter() );
			jointDef.collideConnected = true;
			jointDef.frequencyHz = springiness;
			jointDef.dampingRatio = 0.5f;
			
			world.createJoint(jointDef);
			
			//////////////////////////////////
			
//	        RopeJointDef wJ = new RopeJointDef();
//			
//			wJ.bodyA = currentBody;
//			wJ.bodyB = innerCircleBody;
//		    wJ.localAnchorA.set(0, 0);
//		    wJ.localAnchorB.set(0, 0);
//		
//			wJ.maxLength = innerCircleBody.getPosition().cpy().sub(currentBody.getPosition()).len();
//	        
//			world.createJoint(wJ);
			////////////////////////////////
			
			float theta = deltaAngle*i;
			
			// Calcualte the x and y based on theta 
			float x = (float) (Math.cos(theta));
			float y = (float) (Math.sin(theta));
			
			PrismaticJointDef pistJ = new PrismaticJointDef();
			
			pistJ.bodyA = innerCircleBody;
			pistJ.bodyB = currentBody;
			pistJ.localAnchorA.set(0, 0);
			pistJ.localAnchorB.set(0, 0);
//		    pistJ.localAnchorA.set(1, 0);
			pistJ.localAxisA.set(x, y);
			
			world.createJoint(pistJ);
			
			
			
			// Connect the center circle with other circles        
			jointDef.initialize(currentBody, innerCircleBody, currentBody.getWorldCenter(), center);
			jointDef.collideConnected = true;
			jointDef.frequencyHz = springiness;
			jointDef.dampingRatio = 0.5f;
			
			world.createJoint(jointDef);
			
			
//	        RevoluteJointDef ristJ = new RevoluteJointDef();
//	        ristJ.bodyA = innerCircleBody;
//	        ristJ.bodyB = currentBody;
////		    ristJ.localAnchorA.set(currentBody.getWorldCenter());
//	        ristJ.localAnchorA.set(innerCircleBody.getLocalPoint(currentBody.getWorldCenter()));
//	        ristJ.localAnchorB.set(0, 0);
////		    ristJ.upperAngle = 10 * MathUtils.degreesToRadians;
//	        world.createJoint(ristJ);
		}
		
//		ballSkeleton = new BallObjectSkeleton(bodies, innerCircleBody);
//		ballSkeleton.stage = stage;
//		ballSkeleton.totalWidth = bg.getWidth();
//		ballSkeleton.totalHeight = bg.getHeight();
		
		
		stage.addActor(ball);
	}
//	private void createSoftElipse(){
//		segmentsCount = 14;
//		// Center is the position of circle that is in the center
//		Vector2 center = new  Vector2(580 * WORLD_TO_SCREEN, 184 * WORLD_TO_SCREEN);
//		
//		float radius_ =  5.5f * WORLD_TO_SCREEN;
//		
//		CircleShape circleShape = new CircleShape();
//		circleShape.setRadius(radius_);
//		
//		FixtureDef fixtureDef = new FixtureDef();
//		fixtureDef.shape = circleShape;
//		fixtureDef.density = 0.1f;
////	    fixtureDef.restitution = 0.05f;
//		fixtureDef.friction = 0.0f;
//		
//		// The greater the number, the more springy
//		float springiness = 12.0f;
//		
//		// Delta angle to step by
//		float deltaAngle = (float) ((2.f * Math.PI) / segmentsCount);
//		
//		// Radius of the wheel
//		float a = 38;
//		float b = 30;
//		
//		// Need to store the bodies so that we can refer back
//		// to it when we connect the joints
//		
//		// For each segment...
//		for (int i = 0; i < segmentsCount; i++) {
//			// Current angle
//			float theta = deltaAngle*i;
//			
////	        if(i == 8 || i == 10){
////	        	fixtureDef.density = 0.3f;
////	        }
//			
//			// Calcualte the x and y based on theta 
//			float x = (float) (a*Math.cos(theta));
//			float y = (float) (b*Math.sin(theta));
//			
//			// Remember to divide by PTM_RATIO to convert to Box2d coordinates
//			Vector2 circlePosition =  new Vector2(x * WORLD_TO_SCREEN, y* WORLD_TO_SCREEN);
//			
//			BodyDef bodyDef = new BodyDef();
//			bodyDef.fixedRotation = true;
//			bodyDef.type = BodyType.DynamicBody;
//			// Position should be relative to the center
//			bodyDef.position.set(center.cpy().add(circlePosition));//(center + circlePosition);
//			
//			// Create the body and fixture
//			Body body;
//			body = world.createBody(bodyDef);
//			body.setUserData("bodies");
//			body.createFixture(fixtureDef);
//			
//			// Add the body to the array to connect joints to it
//			// later. b2Body is a C++ object, so must wrap it
//			// in NSValue when inserting into it NSMutableArray
////	        [bodies addObject:[NSValue valueWithPointer:body]];
//			bodies.add(body);
//			
//		}
//		
//		// Circle at the center (inner circle)
//		BodyDef innerCircleBodyDef = new BodyDef();
//		innerCircleBodyDef.fixedRotation = true;
//		innerCircleBodyDef.type = BodyType.DynamicBody;    
//		// Position is at the center
//		innerCircleBodyDef.position.set(center);    
//		Body innerCircleBody = world.createBody(innerCircleBodyDef);
//		innerCircleBody.setUserData("bodies");
//		innerCircleBody.createFixture(fixtureDef);
//		
//		// Connect the joints    
//		DistanceJointDef jointDef = new DistanceJointDef();
//		for (int i = 0; i < segmentsCount; i++) {
//			// The neighbor.
//			int neighborIndex = (i + 1) % segmentsCount;
//			
//			// Get the current body and the neighbor
////	        b2Body *currentBody = (b2Body*)[[bodies objectAtIndex:i] pointerValue];
////	        b2Body *neighborBody = (b2Body*)[[bodies objectAtIndex:neighborIndex] pointerValue];
//			
//			Body currentBody = bodies.get(i);
//			Body neighborBody = bodies.get(neighborIndex);
//			
//			
//			// Connect the outer circles to each other
//			jointDef.initialize(currentBody, neighborBody,
//					currentBody.getWorldCenter(), 
//					neighborBody.getWorldCenter() );
//			jointDef.collideConnected = true;
//			jointDef.frequencyHz = springiness;
//			jointDef.dampingRatio = 0.5f;
//			
//			world.createJoint(jointDef);
//			
//			//////////////////////////////////
//			
////	        RopeJointDef wJ = new RopeJointDef();
////			
////			wJ.bodyA = currentBody;
////			wJ.bodyB = innerCircleBody;
////		    wJ.localAnchorA.set(0, 0);
////		    wJ.localAnchorB.set(0, 0);
////		
////			wJ.maxLength = innerCircleBody.getPosition().cpy().sub(currentBody.getPosition()).len();
////	        
////			world.createJoint(wJ);
//			////////////////////////////////
//			  
//	           float theta = deltaAngle*i;
//		        
//		        // Calcualte the x and y based on theta 
//		        float x = (float) (Math.cos(theta));
//		        float y = (float) (Math.sin(theta));
////PrismaticJointDef pistJ = new PrismaticJointDef();
////		    
////		    pistJ.bodyA = innerCircleBody;
////		    pistJ.bodyB = currentBody;
////		    pistJ.localAnchorA.set(0, 0);
////		    pistJ.localAnchorB.set(0, 0);
//////		    pistJ.localAnchorA.set(1, 0);
////		    pistJ.localAxisA.set(x, y);
////	        
////		    world.createJoint(pistJ);
//		    
//		    
//	        
//	        // Connect the center circle with other circles        
//	        jointDef.initialize(currentBody, innerCircleBody, currentBody.getWorldCenter(), center);
//	        jointDef.collideConnected = true;
//	        jointDef.frequencyHz = springiness;
//	        jointDef.dampingRatio = 0.5f;
//	        
//	        world.createJoint(jointDef);
//
//	        
////	        RevoluteJointDef ristJ = new RevoluteJointDef();
////	        ristJ.bodyA = innerCircleBody;
////	        ristJ.bodyB = currentBody;
//////		    ristJ.localAnchorA.set(currentBody.getWorldCenter());
////	        ristJ.localAnchorA.set(innerCircleBody.getLocalPoint(currentBody.getWorldCenter()));
////	        ristJ.localAnchorB.set(0, 0);
//////		    ristJ.upperAngle = 10 * MathUtils.degreesToRadians;
////	        world.createJoint(ristJ);
//		}
//		
//		ball = new BallObject(new Texture("box2d/ball.png"));
//		ball.body = innerCircleBody;
//		ball.stage = stage;
//		ball.totalWidth = bg.getWidth();
//		ball.totalHeight = bg.getHeight();
//		ball.setSize(radius_ * 2.3f, radius_ * 2.3f);
//		ball.setOrigin(ball.getWidth() * .5f, ball.getHeight() * .5f);
//		
//		ball.setPosition(innerCircleBody.getPosition().x, innerCircleBody.getPosition().y, Align.center);
//		ball.setRotation(innerCircleBody.getAngle() * MathUtils.radiansToDegrees);
//		
//		stage.addActor(ball);
//	}
	
			
	public Body createWorldEdge(){
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;  
//		bd.position.y = bottomHeight;
		Body edge = world.createBody(bd);
		
		
		EdgeShape shape = new EdgeShape();
	
//		shape.setRadius(22);
		float friction = 1f;
		
		shape.set(bottomLeft, bottomRight);
		edge.createFixture(shape, 0).setFriction(friction);
		
		shape.set(bottomLeft, topLeft);
		edge.createFixture(shape, 0).setFriction(friction);
		
		shape.set(bottomRight, topRight);
		edge.createFixture(shape, 0).setFriction(friction);
		
		return edge;
	}
	
	private void blow(){
			float coef = .25f;
			for(Body b : bodies){
				direction.set(b.getPosition()).sub(ball.body.getPosition()).nor();
//				b.applyLinearImpulse(direction.scl(5),b.getWorldCenter(), true);
				b.applyForceToCenter(direction.scl(.12f), true);
				b.setGravityScale(coef);
			}
			ball.body.setGravityScale(coef);
		
	}
	
	private void goDown(){
		float coef = 2f;
		for(Body b : bodies){
			direction.set(b.getPosition()).sub(ball.body.getPosition()).nor();
			b.setGravityScale(coef);
			b.setAwake(true);
		}
		ball.body.setGravityScale(coef);
		ball.body.setAwake(true);
	
}
	private void goLeft(){
		if(ball.body.getLinearVelocity().x > -5){
			
			ball.body.applyForceToCenter(leftForce, true);
		}
		Gdx.app.debug("index", "!isKeyPressed LEFT [" + ball.body.getLinearVelocity().x + " , " +ball.body.getLinearVelocity().y + "]");
	
	}
	
	private void goRight(){
		if(ball.body.getLinearVelocity().x < 5){
			
			ball.body.applyForceToCenter(rightForce, true);
		}
	}
	
}
