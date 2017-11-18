package com.paff.orlandale.paff;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.impl.AndroidGraphics;

/**
 * Created by Yoshi on 18/11/2017.
 */

public class PaffGraphics extends AndroidGraphics {

    public PaffGraphics(AssetManager assets, Bitmap frameBuffer) {
        super(assets,frameBuffer);
    }
    public void drawBubble(GameObject bubble, int color, int alpha){
        float y      = PhysicToPixel.Y(bubble.physic.getPosY());
        float x      = PhysicToPixel.X(bubble.physic.getPosX());
        float radius = PhysicToPixel.XLength(bubble.physic.getRadius());

        paint.setColor(color);
        paint.setAlpha(alpha/2);
        paint.setStyle(Paint.Style.FILL);
        super.canvas.drawCircle(x,y,radius,paint);
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(x,y,radius,paint);
    }
}
