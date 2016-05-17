package com.test1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import box2d_.Test1_Box2d_Water4;

public class Test1_Game extends Game{
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		setScreen(new Test1_PreferencesSample());
//		setScreen(new Test1_Box2d_Water4());
//		setScreen(new Test1_FrameBuffer());
	}

}
