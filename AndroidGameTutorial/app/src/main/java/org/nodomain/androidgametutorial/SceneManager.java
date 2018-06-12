package org.nodomain.androidgametutorial;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    public static int ACTIVE_SCENE;

    private ArrayList<Scene> mScenes;

    public SceneManager() {
        ACTIVE_SCENE = 0;
        mScenes = new ArrayList<Scene>();
        mScenes.add(new GameplayScene());
    }

    public void update() {
        mScenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas) {
        mScenes.get(ACTIVE_SCENE).draw(canvas);
    }

    public void receiveTouch(MotionEvent event) {
        mScenes.get(ACTIVE_SCENE).receiveTouch(event);
    }
}
