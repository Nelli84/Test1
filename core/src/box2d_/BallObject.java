package box2d_;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class BallObject extends ImageObjct{
//	public Body body;
//	public Stage stage;
//	public float totalWidth;
//	public float totalHeight;
	
	/** Creates an image with no region or patch, stretched, and aligned center. */
	public BallObject () {
		this((Drawable)null);
	}

	/** Creates an image stretched, and aligned center.
	 * @param patch May be null. */
	public BallObject (NinePatch patch) {
		this(new NinePatchDrawable(patch), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center.
	 * @param region May be null. */
	public BallObject (TextureRegion region) {
		this(new TextureRegionDrawable(region), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center. */
	public BallObject (Texture texture) {
		this(new TextureRegionDrawable(new TextureRegion(texture)));
	}

	/** Creates an image stretched, and aligned center. */
	public BallObject (Skin skin, String drawableName) {
		this(skin.getDrawable(drawableName), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center.
	 * @param drawable May be null. */
	public BallObject (Drawable drawable) {
		this(drawable, Scaling.stretch, Align.center);
	}

	/** Creates an image aligned center.
	 * @param drawable May be null. */
	public BallObject (Drawable drawable, Scaling scaling) {
		this(drawable, scaling, Align.center);
	}

	/** @param drawable May be null. */
	public BallObject (Drawable drawable, Scaling scaling, int align) {
		setDrawable(drawable);
		this.setScaling(scaling);
		this.setAlign(align);
		setSize(getPrefWidth(), getPrefHeight());
	}
	
	private float deltaX = 0;
	private boolean boolX = false;
	
	private float deltaY = 0;
	private boolean boolY = false;
	
	@Override
	public void act(float delta) {
		if(body != null){
			if(stage != null){
				
				if(Math.abs(deltaX) > .01f){
					boolX = true;
					deltaX = 0;
				}
				if(boolX){
					stage.getCamera().position.x += body.getPosition().x - getX(Align.center);
				}else{
					deltaX += body.getPosition().x - getX(Align.center);
				}
				
				if(body.getPosition().x - getX(Align.center) == 0){
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
			setPosition(body.getPosition().x, body.getPosition().y, Align.center);
//			
			setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		}
		super.act(delta);
	}

}
