package org.nodomain.androidgametutorial;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {
    private ArrayList<Obstacle> mObstacles;
    private int mPlayerGap;
    private int mObstacleGap;
    private int mObstacleHeight;
    private int mColor;

    private long mStartTime;
    private long mInitTime;

    private int mScore;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.mPlayerGap = playerGap;
        this.mObstacles = new ArrayList<>();
        this.mObstacleGap = obstacleGap;
        this.mObstacleHeight = obstacleHeight;
        this.mColor = color;

        this.mStartTime = System.currentTimeMillis();
        this.mInitTime = System.currentTimeMillis();

        this.mScore = 0;

        this.populateObstables();
    }

    private void populateObstables() {
        int currY;

        currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0) {
            int startX;

            startX = (int) (Math.random() * (Constants.SCREEN_WIDTH-mPlayerGap));
            mObstacles.add(new Obstacle(mObstacleHeight, mColor, startX, currY, mPlayerGap));
            currY += mObstacleHeight + mObstacleGap;
        }

    }

    public void update() {
        int elapsedTime;
        float speed;

        if(mStartTime < Constants.INIT_TIME) {
            mStartTime = Constants.INIT_TIME;
        }

        elapsedTime = (int) (System.currentTimeMillis() - mStartTime);
        mStartTime = System.currentTimeMillis();

        speed = (float) 0.1;
//        speed = (float) (1.0 + ((Math.sqrt(mStartTime-mInitTime) / 1000.0) * Constants.SCREEN_HEIGHT/10000.0f));
        for(Obstacle obst: mObstacles) {
            obst.incrementY(speed*elapsedTime);
        }

        if(mObstacles.get(mObstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int startX;

            startX = (int) (Math.random() * (Constants.SCREEN_WIDTH-mPlayerGap));
            mObstacles.add(0, new Obstacle(mObstacleHeight,
                    mColor,
                    startX,
                    mObstacles.get(0).getRectangle().top - mObstacleHeight - mObstacleGap,
                    mPlayerGap));
            mObstacles.remove(mObstacles.size() - 1);

            mScore++;
        }
    }

    public void draw(Canvas canvas) {
        Paint paint;

        for(Obstacle obst: mObstacles) {
            obst.draw(canvas);
        }

        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText("Score: " + mScore, 50, 50+paint.descent(), paint);
    }

    public boolean playerCollide(RectPlayer rectPlayer) {
        for(Obstacle obst: mObstacles) {
            if(obst.playerCollide(rectPlayer)) {
                return true;
            }
        }
        return false;
    }
}
