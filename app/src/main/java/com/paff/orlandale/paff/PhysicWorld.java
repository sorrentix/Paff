package com.paff.orlandale.paff;

import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.impl.AccelerometerHandler;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.RevoluteJointDef;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class PhysicWorld {
    World world;

    final Box physicalSize, screenSize;

    private static final float TIME_STEP = 1 / 60f; //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    private Vec2 gravity;
    private float framebufferWidth;
    private float framebufferHeight;

    Bubble bubbles[] = new Bubble[5];
    Bubble paff;
    int currentAcceleration = 0;
    int previousAcceleration = 0;
    Joint distanceJoint;
    AccelerometerHandler accelerometerHandler;
    private Body collidedBubble;
    private PaffContactListener paffContactListener;

    enum GameState {
        Waiting,
        Spara,
        Ruota,
        JOINT
    }

    GameState gameState = GameState.Ruota;

    public Bubble provaBubble;
    public Rettangolo rettangolo;

    public PhysicWorld(Box physicalSize, Box screenSize, Vec2 gravity, AccelerometerHandler accelerometerHandler) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.gravity = gravity;
        this.framebufferWidth = screenSize.xmax;
        this.framebufferHeight = screenSize.ymax;
        this.accelerometerHandler = accelerometerHandler;

        this.world = new World(gravity.getX(), gravity.getY());
  //      provaBubble =new Bubble(this, new Vec2(3.0f, -10.0f), 2.0f,5.0f);
        paff = new Bubble(this, new Vec2(-3.0f, -10.0f), 1.0f,5.0f);

        rettangolo = new Rettangolo(this,new Vec2(0.0f,2.0f),7.0f,7.0f);
/*
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.setBodyA(paff.getBody());
        distanceJointDef.setBodyB(provaBubble.getBody());
        //distanceJointDef.setLocalAnchorA(paff.getX(), paff.getY());
        //distanceJointDef.setLocalAnchorB(provaBubble.getX(), provaBubble.getY());
        distanceJointDef.setFrequencyHz(0);
        distanceJointDef.setLength(paff.getRadius()+provaBubble.getRadius());
        distanceJoint = world.createJoint(distanceJointDef);

        distanceJointDef.delete();*/

        paffContactListener = new PaffContactListener(this);
        world.setContactListener(paffContactListener);
    }

    public GameState getGameState() {
        return gameState;
    }

    public synchronized void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

    }

    public synchronized void setGravity(float x, float y) {
        world.setGravity(x, y);
    }

    @Override
    public void finalize() {
        world.delete();
    }

    public Bubble[] getBubbles() {
        return bubbles;
    }

    public Bubble getPaff() {
        return paff;
    }

    public void setSpara(GameState state) {
        gameState = state;
    }

    public void collisionDetected(Body ba, Body bb) {
        if (ba.equals(paff.getBody()))
            collidedBubble = bb;
        else
            collidedBubble = ba;
        Log.e("PROVALUNGHEZZE","paff position nel mondo, x:"+paff.getX()+"; y: "+paff.getY()+
                                        "\nrect(?) position nel mondo, x:"+collidedBubble.getPositionX()+"; y: "+collidedBubble.getPositionY());
        Log.e("PROVALUNGHEZZE","paff position nel buffer, x:"+PhysicToPixel.X(paff.getX())+"; y: "+PhysicToPixel.Y(paff.getY())+
                "\nrect(?) position nel mondo, x:"+PhysicToPixel.X(collidedBubble.getPositionX())+"; y: "+PhysicToPixel.Y(collidedBubble.getPositionY()));
    }
}
