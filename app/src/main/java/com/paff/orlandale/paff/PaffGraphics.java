package com.paff.orlandale.paff;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.impl.AndroidGraphics;

/**
 * Created by Yoshi on 18/11/2017.
 */

public class PaffGraphics extends AndroidGraphics {

    public PaffGraphics(AssetManager assets, Bitmap frameBuffer) {
        super(assets, frameBuffer);
    }

    public void drawBubble(GameObject bubble, int color, int alpha) {
        float y = PhysicToPixel.Y(bubble.physic.getPosY());
        float x = PhysicToPixel.X(bubble.physic.getPosX());
        float radius = PhysicToPixel.XLength(bubble.physic.getRadius());

        paint.setColor(color);
        paint.setAlpha(alpha / 2);
        paint.setStyle(Paint.Style.FILL);
        super.canvas.drawCircle(x, y, radius, paint);
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void drawFilter(int color) {
        paint.setColor(color);
        paint.setAlpha(150);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), paint);
    }

    public void drawText(GameObject object, int color) {

        if (paint.measureText(object.text.toWrite) + object.position.x + object.image.getWidth() > GlobalConstants.FRAME_BUFFER_WIDTH) {

            canvas.translate(-(paint.measureText(object.text.toWrite) + object.image.getWidth() + 20), 0);
            super.drawGameObject(object);
            paint.setColor(color);
            paint.setAlpha(255);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(4);
            paint.setTextSize(40);
            paint.setTypeface(Assets.font);
            canvas.drawText(object.text.toWrite, object.position.x + object.image.getWidth(), object.position.y + object.image.getHeight(), paint);
            canvas.translate(paint.measureText(object.text.toWrite) + object.image.getWidth() + 20, 0);
        } else
            super.drawGameObject(object);
            paint.setColor(color);
            paint.setAlpha(255);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(4);
            paint.setTextSize(40);
            paint.setTypeface(Assets.font);
            canvas.drawText(object.text.toWrite, object.position.x + object.image.getWidth(), object.position.y + object.image.getHeight(), paint);

    }
}
