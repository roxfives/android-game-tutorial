package org.nodomain.androidgametutorial;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameplayScene implements Scene {
    private RectPlayer mPlayer;
    private Point mPlayerPoint;
    private ObstacleManager mObstacleManager;
    private boolean mMovingPlayer;
    private boolean mGameOver;
    private long mGameOverTime;
    private Rect mGameOverRect;

    public GameplayScene() {
        this.mPlayer = new RectPlayer(new Rect(100, 100, 200, 200), Color.RED);
        this.mPlayerPoint = new Point(Constants.SCREEN_WIDTH/2, 3 * Constants.SCREEN_HEIGHT/4);
        this.mPlayer.update();
        this.mMovingPlayer = false;

        this.mGameOverRect = new Rect();

        this.mObstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
    }

    @Override
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

    @Override
    public void draw(Canvas canvas) {
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

    @Override
    public void receiveTouch(MotionEvent event) {
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

    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    public void reset() {
        this.mPlayerPoint = new Point(Constants.SCREEN_WIDTH/2, 3 * Constants.SCREEN_HEIGHT/4);
        this.mPlayer.update();
        this.mObstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        mMovingPlayer = false;
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
}
