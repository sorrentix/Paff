package com.paff.orlandale.paff;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoshi on 10/11/2017.
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

    public static interface onAnimationCompleteListener{
        public  abstract void onAnimationComplete(Animation a);
    }

    public Animation getAnimationByID(int id){
        return animations.get(id);
    }


}