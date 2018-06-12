package org.nodomain.androidgametutorial;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void receiveTouch(MotionEvent event);
    public void terminate();
}
