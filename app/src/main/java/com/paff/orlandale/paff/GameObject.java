package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Game;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;

/**
 * Created by sorrentix on 18/11/2017.
 */

public abstract class GameObject {

    protected final Game game;
    protected Body body;
    public float oldPos;
    public double perlinSeed;

    public GameObject(Game game){
        this.game = game;
    }

    public Body getBody(){
        return body;
    }

    public Vec2 getCenter(){
        return this.getBody().getPosition();
    }

    public float getX(){
        return this.getBody().getPositionX();
    }

    public float getY(){
        return this.getBody().getPositionY();
    }

    public abstract void draw();
}
