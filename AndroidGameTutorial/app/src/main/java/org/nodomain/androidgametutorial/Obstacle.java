package org.nodomain.androidgametutorial;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject {
    private Rect mRectangle;
    private Rect mRectangle2;
    private int color;

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        this.mRectangle = new Rect(0, startY, startX, startY+rectHeight);
        this.mRectangle2 = new Rect(startX+playerGap, startY, Constants.SCREEN_WIDTH, startY+rectHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint;

        paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(mRectangle, paint);
        canvas.drawRect(mRectangle2, paint);
    }

    @Override
    public void update() {

    }

    public boolean playerCollide(RectPlayer rectPlayer) {
        return Rect.intersects(mRectangle, rectPlayer.getRectangle())
                || Rect.intersects(mRectangle2, rectPlayer.getRectangle());
    }

    public void incrementY(float y) {
        mRectangle.top += y;
        mRectangle.bottom += y;
        mRectangle2.top += y;
        mRectangle2.bottom += y;
    }

    public Rect getRectangle() {
        return this.mRectangle;
    }
}
