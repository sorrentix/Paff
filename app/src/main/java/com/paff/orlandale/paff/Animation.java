package com.paff.orlandale.paff;

import android.graphics.Rect;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoshi on 10/11/2017.
 */


public class Animation {
    Graphics g;
    Pixmap [] images;
    Rect srcR;
    Rect r[];
    int id;
    int currentImage = 0;

    private List<AnimationPool.onAnimationCompleteListener> listeners = new ArrayList<AnimationPool.onAnimationCompleteListener>();

    public void addListener(AnimationPool.onAnimationCompleteListener toAdd) {
        listeners.add(toAdd);
    }

    public Animation(Graphics g, Pixmap[] images, Rect srcR, Rect []dstR, int id){
        this.images = images;
        this.srcR = srcR;
        this.r = dstR;
        this.id = id;
        this.g = g;
    }

    public int getID(){
        return id;
    }

    public void executeAnimation(){
        g.drawScaledPixmap(images[currentImage],srcR ,r[currentImage]);
        currentImage++;
        // Notify everybody that may be interested.
        if( currentImage == images.length-1 ){
        for (AnimationPool.onAnimationCompleteListener animationListener : listeners)
            animationListener.onAnimationComplete(this);
            currentImage = 0;
        }
    }
}




