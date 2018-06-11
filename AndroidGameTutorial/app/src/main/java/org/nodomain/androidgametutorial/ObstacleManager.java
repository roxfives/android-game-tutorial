package org.nodomain.androidgametutorial;

import android.graphics.Canvas;

import java.util.ArrayList;

public class ObstacleManager {
    private ArrayList<Obstacle> mObstacles;
    private int mPlayerGap;
    private int mObstacleGap;
    private int mObstacleHeight;
    private int mColor;

    private long mStartTime;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.mPlayerGap = playerGap;
        this.mObstacles = new ArrayList<>();
        this.mObstacleGap = obstacleGap;
        this.mObstacleHeight = obstacleHeight;
        this.mColor = color;

        this.mStartTime = System.currentTimeMillis();
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

        elapsedTime = (int) (System.currentTimeMillis() - mStartTime);
        mStartTime = System.currentTimeMillis();

        speed = Constants.SCREEN_HEIGHT/10000.0f;
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
        }

    }

    public void draw(Canvas canvas) {
        for(Obstacle obst: mObstacles) {
            obst.draw(canvas);
        }
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
