package org.nodomain.androidgametutorial;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private static final int MAX_FPS = 30;
    private static final long TARGET_TIME = 1000/MAX_FPS;

    public static Canvas mCanvas;

    private double mAverageFPS;
    private boolean mRunning;
    private SurfaceHolder mSurfaceHolder;
    private GamePanel mGamePanel;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();

        System.out.println("Creating main thread");
        this.mSurfaceHolder = surfaceHolder;
        this.mGamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long totalTime;
        long waitTime;
        int frameCount;
        System.out.println("Started running: " + mRunning);
        totalTime = 0;
        frameCount = 0;
        while(mRunning) {
            startTime = System.nanoTime();
            mCanvas = null;


            try {
                mCanvas = this.mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    this.mGamePanel.update();
                    this.mGamePanel.draw(mCanvas);
                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                 try {
                     mSurfaceHolder.unlockCanvasAndPost(mCanvas);

                 } catch (Exception e) {
                    e.printStackTrace();
                 }
            }

            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = TARGET_TIME - timeMillis;
            try {
                if(waitTime > 0) {
                    this.sleep(waitTime);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS) {
                mAverageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;

                System.out.println("FPS:" + mAverageFPS);
            }
        }
    }

    public void setRunning(boolean running) {
        System.out.println("Setting running: " + running);
        this.mRunning = running;
    }
}
