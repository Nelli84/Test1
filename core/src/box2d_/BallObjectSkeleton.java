package box2d_;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

public class BallObjectSkeleton extends ImageObjct{


//	public Body body;
//	public Array<Body> bodies;
	public Array<Bone> bones;
//	
//	public Stage stage;
//	public float totalWidth;
//	public float totalHeight;
	
		
	private float deltaX = 0;
	private boolean boolX = false;
	
	private float deltaY = 0;
	private boolean boolY = false;
	Bone bone;
	
	Skeleton skeleton;
	
	AnimationState state;
	TextureAtlas atlas;
	SkeletonMeshRenderer renderer;
	SkeletonRendererDebug debugRenderer;
	
	public BallObjectSkeleton(Array<Body> bodies_ , Body body_){
		this.bodies = bodies_;
		this.body = body_;
		
		renderer = new SkeletonMeshRenderer();
		renderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
		debugRenderer = new SkeletonRendererDebug();
		debugRenderer.setBoundingBoxes(false);
		debugRenderer.setRegionAttachments(false);

		
		atlas = new TextureAtlas(Gdx.files.internal("box2d/skeleton1.atlas"));
		SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
		json.setScale(Test1_Box2d.WORLD_TO_SCREEN * .4f); // Load the skeleton at 60% the size it was in Spine.
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("box2d/skeleton1.json"));

		skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
		//skeleton.setPosition(250, 20);
//		skeleton.setPosition(580 * Test1_Box2d.WORLD_TO_SCREEN, 184 * Test1_Box2d.WORLD_TO_SCREEN);

		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
//		stateData.setMix("run", "jump", 0.2f);
//		stateData.setMix("jump", "run", 0.2f);

		state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
//		state.setTimeScale(0.5f); // Slow all animations down to 50% speed.

		// Queue animations on track 0.
//		state.setAnimation(0, "run", true);
//		state.addAnimation(0, "jump", false, 2); // Jump after 2 seconds.
//		state.addAnimation(0, "run", true, 0); // Run after the jump.
		
		
		bones = skeleton.getBones();
		for(Bone bone : bones){
			String name =bone.getData().getName();
			
			if(name.equals("body")){
				this.bone = bone;
				body.getPosition().set(bone.getWorldX(), bone.getWorldY());
//				break;
				
			}else{
                   if(!name.equals("root")){
					
					String num = name.substring(name.lastIndexOf('y') + 1, name.length() );
					
					int index = Integer.parseInt(num);
					
					bodies.get(index).getPosition().set(bone.getWorldX(), bone.getWorldY());
				}

			}
			
		}
		
	}
	
	public void act(float delta) {
		if(body != null){
			if(stage != null){
				
				if(Math.abs(deltaX) > .01f){
					boolX = true;
					deltaX = 0;
				}
				if(boolX){
					stage.getCamera().position.x += body.getPosition().x - bone.getWorldX();
				}else{
					deltaX += body.getPosition().x - bone.getWorldY();
				}
				
				if(body.getPosition().x - bone.getWorldX() == 0){
					boolX = false;
				}
				
				
//				if(Math.abs(deltaY) > .01f){
//					boolY = true;
//					deltaY = 0;
//				}
//				if(boolY){
//					stage.getCamera().position.y += body.getPosition().y - getY(Align.center);
//				}else{
//					deltaY += body.getPosition().y - getY(Align.center);
//				}
//				
//				if(body.getPosition().y - getY(Align.center) == 0){
//					boolY = false;
//				}
				
//				stage.getCamera().position.x = MathUtils.clamp(stage.getCamera().position.x, stage.getWidth() * .5f* ((OrthographicCamera)stage.getCamera()).zoom, totalWidth *  - stage.getWidth() * .5f * ((OrthographicCamera)stage.getCamera()).zoom);
//				stage.getCamera().position.y = MathUtils.clamp(stage.getCamera().position.y, stage.getHeight() * .5f * ((OrthographicCamera)stage.getCamera()).zoom, totalHeight - stage.getHeight() * .5f * ((OrthographicCamera)stage.getCamera()).zoom);
				
			}
			
			for(Bone bone : bones){
				String name =bone.getData().getName();
				
				if(!name.equals("body") && !name.equals("root")){
					
					String num = name.substring(name.lastIndexOf('y') + 1, name.length() );
					
					int index = Integer.parseInt(num);
					
					bone.setPosition(bodies.get(index).getPosition().x, bodies.get(index).getPosition().y);
//					bone.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
				}
				
			}
			bone.setPosition(body.getPosition().x, body.getPosition().y);
//			bone.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
			
			
		}
		
		state.update(delta); // Update the animation time.


		state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
		skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		renderer.draw((PolygonSpriteBatch)batch, skeleton);
//		debugRenderer.draw(skeleton); // Draw debug lines.
	}

	public void dispos() {
		atlas.dispose();

	}
}
