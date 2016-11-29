package com.test1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.io.IOException;

import box2d_.Test1_Box2d_Water4;
import com.badlogic.gdx.utils.ObjectMap;

public class Test1_Game extends Game{
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
//		setScreen(new Test1_AssetManagerSample());

		ObjectMap o = new ObjectMap();
		o.put("1", "a");

		ObjectMap<Integer, String> ss = o;

		Gdx.app.debug("========== ??? ======", ss.get(1) +"");

		setScreen(new Test1_Box2d_Water4());
//		setScreen(new Test1_FrameBuffer());
	}

}
