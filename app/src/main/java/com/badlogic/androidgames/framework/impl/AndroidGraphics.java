package com.badlogic.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.paff.orlandale.paff.GameObject;
import com.paff.orlandale.paff.PhysicToPixel;

public class AndroidGraphics implements Graphics {
    protected AssetManager assets;
    protected Bitmap frameBuffer;
    protected Canvas canvas;
    protected Paint paint;
    protected Rect srcRect = new Rect();
    protected Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config = null;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawPixel(float x, float y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(float x, float y, float x2, float y2, int color) {
        paint.setColor(color);
        paint.setAlpha(255);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,null);
    }

    @Override
    public void drawGameObject(GameObject g){
        canvas.drawBitmap(((AndroidPixmap) g.image).bitmap, g.position.x, g.position.y, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, float x, float y) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
    }

    @Override
    public void drawScaledPixmap(Pixmap pixmap, Rect srcRect, Rect dstRect ) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, srcRect, dstRect, null);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }

    @Override
    public void drawCircle(float x, float y, float radius, int color, int alpha){
        paint.setColor(color);
        paint.setAlpha(alpha/2);
        paint.setStyle(Style.FILL);
        canvas.drawCircle(x,y,radius,paint);
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(x,y,radius,paint);
    }
}
