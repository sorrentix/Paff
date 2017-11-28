package com.paff.orlandale.paff;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoshi on 10/11/2017.
 */

/**
 * Classe che consente di gestire un pool di animazioni.
 * La singola animazione è recuperabile tramite un id univoco
 */
public class AnimationPool {
    Map<Integer, Animation> animations;
    public int animationToExecute = -1;

    public AnimationPool(){
        animations = new HashMap<>();
    }

    public void loadAnimation(Animation a){
        animations.put(a.getID(), a);
    }

    public interface onAnimationCompleteListener{
        void onAnimationComplete(Animation a);
    }

    public Animation getAnimationByID(int id){
        return animations.get(id);
    }


}