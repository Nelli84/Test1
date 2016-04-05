package com.test1;

import box2d_.Test1_Box2d_1;
import box2d_.Test1_Box2d_Water4;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Test1_Game extends Game{
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		setScreen(new Test1_Box2d_Water4());
//		setScreen(new Test1_FrameBuffer());
	}

}
