package org.nodomain.androidgametutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mThread;
    private RectPlayer mPlayer;
    private Point mPlayerPoint;
    private ObstacleManager mObstacleManager;
    private boolean mMovingPlayer;
    private boolean mGameOver;
    private long mGameOverTime;
    private Rect mGameOverRect;

    public GamePanel(Context context) {
        super(context);

        this.getHolder().addCallback(this);
        this.setFocusable(true);

        this.mPlayer = new RectPlayer(new Rect(100, 100, 200, 200), Color.RED);
        this.mPlayerPoint = new Point(Constants.SCREEN_WIDTH/2, 3 * Constants.SCREEN_HEIGHT/4);
        this.mPlayer.update();
        this.mMovingPlayer = false;

        this.mGameOverRect = new Rect();

        this.mObstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new MainThread(this.getHolder(), this);

        mThread.setRunning(true);
        mThread.start();
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

        canvas.drawColor(Color.WHITE);
        mPlayer.draw(canvas);
        mObstacleManager.draw(canvas);

        if(mGameOver) {
            Paint textPaint;

            textPaint = new Paint();
            textPaint.setTextSize(100);
            textPaint.setColor(Color.MAGENTA);

            drawCenterText(canvas, textPaint, "Game Over");
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(mGameOverRect);
        int cHeight = mGameOverRect.height();
        int cWidth = mGameOverRect.width();
        paint.getTextBounds(text, 0, text.length(), mGameOverRect);
        float x = cWidth / 2f - mGameOverRect.width() / 2f - mGameOverRect.left;
        float y = cHeight / 2f + mGameOverRect.height() / 2f - mGameOverRect.bottom;
        canvas.drawText(text, x, y, paint);
    }

    public void update() {
        if(!mGameOver) {
            mPlayer.update(mPlayerPoint);
            mObstacleManager.update();

            if(mObstacleManager.playerCollide(mPlayer)){
                mGameOver = true;
                mGameOverTime = System.currentTimeMillis();
            }
        }
    }

    public void reset() {
        this.mPlayerPoint = new Point(Constants.SCREEN_WIDTH/2, 3 * Constants.SCREEN_HEIGHT/4);
        this.mPlayer.update();
        this.mObstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        mMovingPlayer = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!mGameOver && mPlayer.getRectangle().contains((int) event.getX(), (int) event.getY())) {
                    mMovingPlayer = true;
                }

                if(mGameOver && System.currentTimeMillis() - mGameOverTime >= 2000) {
                    reset();
                    mGameOver = false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if(!mGameOver && mMovingPlayer) {
                    mPlayerPoint.set((int) event.getX(), (int) event.getY());
                }
                break;

            case MotionEvent.ACTION_UP:
                mMovingPlayer = false;
                break;
        }

        return true;
    }
}
