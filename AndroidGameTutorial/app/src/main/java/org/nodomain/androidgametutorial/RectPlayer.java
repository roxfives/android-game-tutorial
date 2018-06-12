package org.nodomain.androidgametutorial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {
    private Rect mRectangle;
    private int mColor;

    private Animation mIdle;
    private Animation mWalkRight;
    private Animation mWalkLeft;

    private AnimationManager mAnimationManager;

    public RectPlayer(Rect rect, int color) {
        BitmapFactory bf;
        Bitmap idle;
        Bitmap walk1;
        Bitmap walk2;
        Bitmap walk1_left;
        Bitmap walk2_left;
        Matrix matrix;

        this.mRectangle = rect;
        this.mColor = color;

        bf = new BitmapFactory();
        matrix = new Matrix();
        matrix.preScale(-1, 1);
        idle = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue);
        walk1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk1);
        walk2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk2);
        walk1_left = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), matrix, false);
        walk2_left = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), matrix, false);

        mIdle = new Animation(new Bitmap[]{idle}, 2);
        mWalkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);
        mWalkLeft = new Animation(new Bitmap[]{walk1_left, walk2_left}, 0.5f);



        mAnimationManager = new AnimationManager(new Animation[]{mIdle, mWalkRight, mWalkLeft});
    }

    @Override
    public void draw(Canvas canvas) {
//        Paint paint;
//
//        paint = new Paint();
//        paint.setColor(mColor);
//        canvas.drawRect(mRectangle, paint);
        mAnimationManager.draw(canvas, mRectangle);
    }

    @Override
    public void update() {
        mAnimationManager.update();
    }

    public void update(Point point) {
        int state;
        float oldLeft;

        oldLeft = mRectangle.left;
        mRectangle.set(point.x - mRectangle.width()/2,
                point.y - mRectangle.height()/2,
                point.x + mRectangle.width()/2,
                point.y + mRectangle.height()/2);

        state = 0;
        if(mRectangle.left - oldLeft > 1) {
            state = 1;
        } else if(mRectangle.left - oldLeft < -1) {
            state = 2;
        }

        mAnimationManager.playAnimation(state);
        mAnimationManager.update();
    }

    public Rect getRectangle() {
        return this.mRectangle;
    }
}
