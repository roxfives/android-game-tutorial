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

    private OrientationData mOrientationData;
    private long mFrameTime;

    public GameplayScene() {
        this.mPlayer = new RectPlayer(new Rect(100, 100, 200, 200), Color.RED);
        this.mPlayerPoint = new Point(Constants.SCREEN_WIDTH/2, 3 * Constants.SCREEN_HEIGHT/4);
        this.mPlayer.update();
        this.mMovingPlayer = false;

        this.mGameOverRect = new Rect();

        this.mObstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);

        this.mOrientationData = new OrientationData();
        mOrientationData.register();

        mFrameTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        int elapsedTime;
        float pitch;
        float roll;
        float xSpeed;
        float ySpeed;

        if(!mGameOver) {
            if(mFrameTime < Constants.INIT_TIME) {
                mFrameTime = Constants.INIT_TIME;
            }

            elapsedTime = (int) (System.currentTimeMillis() - mFrameTime);
            mFrameTime = System.currentTimeMillis();

            if(mOrientationData.getOrientation() != null
                    && mOrientationData.getStartOrientation() != null) {
                pitch = mOrientationData.getOrientation()[1] - mOrientationData.getStartOrientation()[2];
                roll = mOrientationData.getOrientation()[2] - mOrientationData.getStartOrientation()[1];

                xSpeed = 2 * roll * Constants.SCREEN_WIDTH/1000f;
                ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                mPlayerPoint.x += (Math.abs(xSpeed * elapsedTime) > 5)? xSpeed * elapsedTime : 0;
                mPlayerPoint.y -= (Math.abs(ySpeed * elapsedTime) > 5)? ySpeed * elapsedTime : 0;
            }

            mPlayerPoint.x = (mPlayerPoint.x < 0)? 0 : mPlayerPoint.x;
            mPlayerPoint.x = (mPlayerPoint.x > Constants.SCREEN_WIDTH)? Constants.SCREEN_WIDTH : mPlayerPoint.x;

            mPlayerPoint.y = (mPlayerPoint.y < 0)? 0 : mPlayerPoint.y;
            mPlayerPoint.y = (mPlayerPoint.y > Constants.SCREEN_HEIGHT)? Constants.SCREEN_HEIGHT : mPlayerPoint.y;

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
                    mOrientationData.newGame();
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
