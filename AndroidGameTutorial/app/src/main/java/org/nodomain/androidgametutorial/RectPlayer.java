package org.nodomain.androidgametutorial;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;

public class RectPlayer implements GameObject {
    private Rect mRectangle;
    private int mColor;

    public RectPlayer(Rect rect, int color) {
        this.mRectangle = rect;
        this.mColor = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint;

        paint = new Paint();
        paint.setColor(mColor);
        canvas.drawRect(mRectangle, paint);
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
        mRectangle.set(point.x - mRectangle.width()/2,
                point.y - mRectangle.height()/2,
                point.x + mRectangle.width()/2,
                point.y + mRectangle.height()/2);
    }

    public Rect getRectangle() {
        return this.mRectangle;
    }
}
