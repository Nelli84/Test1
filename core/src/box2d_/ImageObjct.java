package box2d_;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class ImageObjct extends Image{

	public Body body;
	public Stage stage;
	public float totalWidth;
	public float totalHeight;
	public Array<Body> bodies;
}
