package com.test1;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UIStage extends Stage{

    public UIStage(){
        super(new ScreenViewport());

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

    }

    @Override
    public boolean keyDown(int keyCode) {

        switch (keyCode) {
            case Input.Keys.BACK:
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                break;
            default:
                break;
        }
        return super.keyDown(keyCode);
    }

}


