package com.paff.orlandale.paff;

import android.graphics.Rect;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoshi on 10/11/2017.
 */

/**
 * Classe per la gestione di un'animazione con sprite
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

    /**
     * Costruttore della classe, inizializza i campi.
     * @param  g  istanza di una classe che implementa Graphics, consente di
     *            disegnare lo step corrente dell'animazione
     * @param  images  sequenza di immagini che compongono l'animazione
     * @param  srcR  Rect delle immagini in input da prendere
     * @param  dstR  Rect di destinazione sul disegno dell'animazione
     * @param  id    identificativo univoco per l'animazione
     */
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


    /**
     * Disegna uno step dell'animazione e poi passa allo step successivo.
     * Quando l'animazione è terminata, avvisa tutti gli ovserver della conclusione
     */
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




