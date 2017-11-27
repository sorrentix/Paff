package com.badlogic.androidgames.framework;

import android.graphics.Rect;

import com.paff.orlandale.paff.GameObject;

public interface Graphics {

    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);

    public void clear(int color);

    public void drawPixel(float x, float y, int color);

    public void drawLine(float x, float y, float x2, float y2, int color);

    public void drawRect(float x, float y, float width, float height, int color);

    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    public void drawScaledPixmap(Pixmap pixmap, Rect srcRect, Rect dstRect );


        public void drawPixmap(Pixmap pixmap, float x, float y);

    public void drawGameObject(GameObject g);

    public int getWidth();

    public int getHeight();

    public void drawCircle(float x, float y, float radius, int color, int alpha);
}
