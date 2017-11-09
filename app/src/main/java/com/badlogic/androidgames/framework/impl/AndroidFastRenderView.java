package com.badlogic.androidgames.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    FPSCounter fpsCounter = new FPSCounter();
    AndroidGame game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    
    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }

    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }      
    
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while(running) {  
            if(!holder.getSurface().isValid())
                continue;           
            
            float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().present(deltaTime);
            fpsCounter.logFrame();
            Canvas canvas = holder.lockCanvas();
            /*canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);*/
            //Nel modo seguente invece di stretchare allarghiamo mantenendo le proporzioni sulla x e tagliamo la parte in più sotto
            canvas.scale((float)game.getScaleFactorX(),(float)game.getScaleFactorX());
            canvas.drawBitmap(framebuffer,0,0,null);
            holder.unlockCanvasAndPost(canvas);
            while ((System.nanoTime()-startTime) / 1000000000.0f < 0.02f ){
                //wait to make a new render
            }
        }
    }

    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }        
}

class FPSCounter {
    long startTime = System.nanoTime();
    int frames = 0;
    public void logFrame() {
        frames++;
        if(System.nanoTime() - startTime >= 1000000000) {
            Log.d("FPSCounter", "fps: " + frames);
            frames = 0;
            startTime = System.nanoTime();
        }
    }
}