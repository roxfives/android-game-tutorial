package org.nodomain.androidgametutorial;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {
    private Bitmap[] mFrames;
    private int mFrameIndex;
    private float mFrameTime;
    private long mLastFrame;

    private boolean mIsPlaying;

    public Animation(Bitmap[] frames, float animationTime) {
        mIsPlaying = false;

        mFrames = frames;
        mFrameIndex = 0;
        mFrameTime = animationTime/frames.length;
        mLastFrame = System.currentTimeMillis();
    }


    public void play() {
        mIsPlaying = true;
        mFrameIndex = 0;
        mLastFrame = System.currentTimeMillis();
    }

    public void update() {
        if(!mIsPlaying) {
            return;
        }

        if(System.currentTimeMillis()-mLastFrame > mFrameTime/1000) {
            mFrameIndex++;
            mFrameIndex %= mFrames.length;

            mLastFrame = System.currentTimeMillis();
        }
    }

    public void stop() {
        mIsPlaying = false;
    }

    public void draw(Canvas canvas, Rect destination) {
        if(!mIsPlaying) {
            return;
        }

        scaleRect(destination);
        canvas.drawBitmap(mFrames[mFrameIndex], null, destination, new Paint());
    }

    private void scaleRect(Rect rect) {
        float widthRatio;

        widthRatio = (float) (mFrames[mFrameIndex].getWidth())/mFrames[mFrameIndex].getHeight();
        if(rect.width() > rect.height()) {
            rect.left = rect.right - (int) (rect.height() * widthRatio);
        } else {
            rect.top = rect.bottom - (int) (rect.width() * (1/widthRatio));
        }
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }
}
