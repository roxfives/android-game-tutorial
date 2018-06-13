package org.nodomain.androidgametutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mThread;
    private SceneManager mManager;

    public GamePanel(Context context) {
        super(context);

        this.getHolder().addCallback(this);
        Constants.CURRENT_CONTEXT = context;
        this.setFocusable(true);

        this.mManager = new SceneManager();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new MainThread(this.getHolder(), this);
        mThread.setRunning(true);
        mThread.start();

        Constants.INIT_TIME = System.currentTimeMillis();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry;

        retry = true;
        while(retry) {
            try {
                mThread.setRunning(false);
                mThread.join();
                retry = false;
            } catch (Exception e) {
                retry = true;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mManager.draw(canvas);
    }

    public void update() {
        mManager.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mManager.receiveTouch(event);
        return true;
    }
}
