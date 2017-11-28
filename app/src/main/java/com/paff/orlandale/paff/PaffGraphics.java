package com.paff.orlandale.paff;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.impl.AndroidGraphics;

/**
 * Created by Yoshi on 18/11/2017.
 */

/**
 * Classe che estende AndroidGraphics per disegnare gli oggetti specifici del gioco Paff
 */
public class PaffGraphics extends AndroidGraphics {

    public PaffGraphics(AssetManager assets, Bitmap frameBuffer) {
        super(assets, frameBuffer);
    }

    /**
     * Disegna una bolla sull'area di disegno
     * @param  bubble oggetto bolla da disegnare
     * @param  color  notazione rgb intera esadecimale, es 0xffffff = white
     * @param  alpha  valore intero tra [0..255] che indica la trasparenza
     */
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

    /**
     * Applica un filtro di scurimento sull'area di disegno
     * @param  color  notazione rgb intera esadecimale, es 0xffffff = white
     */
    public void drawFilter(int color) {
        paint.setColor(color);
        paint.setAlpha(150);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), paint);
    }


    /**
     * Disegna un GameObject che ha la componente testo sull'area di disegno
     * @param  object il GameObject di cui disegnare testo ed immagine
     * @param  color  notazione rgb intera esadecimale, es 0xffffff = white
     * @param  fontSize dimensione font
     */
    public void drawText(GameObject object, int color,int fontSize) {
        float width = 0;
        float height=0;
        if(object.image != null) {
            width = object.image.getWidth();
            height = object.image.getHeight();
        }

        if (paint.measureText(object.text.toWrite) + object.position.x +width > GlobalConstants.FRAME_BUFFER_WIDTH) {

            canvas.translate(-(paint.measureText(object.text.toWrite) + width), 0);

            if(object.image != null)
                super.drawGameObject(object);

            paint.setColor(color);
            paint.setAlpha(255);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(3);
            paint.setTextSize(fontSize);
            paint.setTypeface(Assets.font);
            canvas.drawText(object.text.toWrite, object.position.x + width, object.position.y + height, paint);
            canvas.translate(paint.measureText(object.text.toWrite) + width, 0);
        } else if(object.image != null)
            super.drawGameObject(object);

        paint.setColor(color);
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(3);
        paint.setTextSize(fontSize);
        paint.setTypeface(Assets.font);
        canvas.drawText(object.text.toWrite, object.position.x + width, object.position.y + height, paint);

    }
}
