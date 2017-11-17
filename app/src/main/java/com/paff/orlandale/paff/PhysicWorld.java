package com.paff.orlandale.paff;

import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.impl.AccelerometerHandler;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
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

    private static final float TIME_STEP = GlobalConstants.FPS; //60 fps
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
    Joint revoluteJoint;
    AccelerometerHandler accelerometerHandler;
    private Body collidedBubble;
    private PaffContactListener paffContactListener;



    GameState gameState = GameState.ROTATE;

    public Bubble provaBubble;
    public Bubble provaBubble2;

    public PhysicWorld(Box physicalSize, Box screenSize, Vec2 gravity, AccelerometerHandler accelerometerHandler) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.gravity = gravity;
        this.framebufferWidth = screenSize.xmax;
        this.framebufferHeight = screenSize.ymax;
        this.accelerometerHandler = accelerometerHandler;

        this.world = new World(gravity.getX(), gravity.getY());
        Random generator = new Random();
        paffContactListener = new PaffContactListener(this);
        world.setContactListener(paffContactListener);
        /* for (int i = 0; i < 5; i++) {
            bubbles[i] = new Bubble(this, new Vec2(((float) generator.nextInt(160) / 10.0f) - 8.0f, ((float) generator.nextInt(150) / 10.0f) + 15.0f), (generator.nextFloat() % 4) + 1);
        }*/// int randomNum = rand.nextInt((max - min) + 1) + min;
        provaBubble =new Bubble(this, new Vec2(0, 4), 2,1.0f, BodyType.staticBody);
        provaBubble2 =new Bubble(this, new Vec2(1, -8), 2,1.0f, BodyType.staticBody);
        paff = new Bubble(this, new Vec2(0, -4), 1,1.0f,BodyType.dynamicBody);


        Log.e("DIMENSIONI:","Paff: \n x: "+paff.getX()+
                                            "\ny:"+paff.getY()+
                                            "\nBubble:"+
                                            "\nx:"+provaBubble.getX()+
                                            "\ny:"+provaBubble.getY());


        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.setBodyA(paff.getBody());
        distanceJointDef.setBodyB(provaBubble.getBody());
        //distanceJointDef.setLocalAnchorA(0, 0);
        //distanceJointDef.setLocalAnchorB(0, 0);
        distanceJointDef.setFrequencyHz(0);
        distanceJointDef.setDampingRatio(0);
        distanceJointDef.setLength(paff.getRadius()+provaBubble.getRadius());
        distanceJoint = world.createJoint(distanceJointDef);

        distanceJointDef.delete();

/*
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.setBodyA(paff.getBody());
        revoluteJointDef.setBodyB(provaBubble.getBody());
        float tempX, tempY;
        if (paff.getX()<provaBubble.getX())
            tempX = provaBubble.getX() - paff.getX();
        else
            tempX = paff.getX() - provaBubble.getX();
        if (paff.getY()<provaBubble.getY())
            tempY = paff.getX() - provaBubble.getY();
        else
            tempY = provaBubble.getX() - provaBubble.getY();
            revoluteJointDef.setLocalAnchorA(tempX,tempY);
        revoluteJointDef.setLocalAnchorB(0,0);
        revoluteJoint = world.createJoint(revoluteJointDef);
        revoluteJointDef.delete();*/

    }

    public GameState getGameState() {
        return gameState;
    }

    public synchronized void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

        /*Log.e("DIMENSIONI:","Paff: \n x: "+paff.getX()+
                "\ny:"+paff.getY()+
                "\nBubble:"+
                "\nx:"+provaBubble.getX()+
                "\ny:"+provaBubble.getY());*/

/*        for (int i = 0; i < 5; i++){
            if (bubbles.get(i).getY()+bubbles.get(i).getRadius()<=physicalSize.ymin)
                bubbles.get(i).getBody().
        }*/

        // paff.getBody().applyForce(new Vec2(0,-accelerometerHandler.getAccelX()),new Vec2(paff.getX(),paff.getY()),false);


        switch (gameState) {
            case SHOT:
                Log.e("SPARA", "SPARA");
                Vec2 vec = new Vec2(paff.getX() - provaBubble.getX(), (paff.getY() - provaBubble.getY())*10);
                world.destroyJoint(distanceJoint);
                //world.destroyJoint(revoluteJoint);
                paff.getBody().setAngularVelocity(0);
                paff.getBody().setLinearVelocity(new Vec2(paff.getX(), paff.getY()));
                paff.getBody().applyLinearImpulse(vec, new Vec2(paff.getX(), paff.getY()), false);
                gameState = GameState.WAITING;
                break;
            case ROTATE:
                Log.e("RUOTA", "RUOTA");
                currentAcceleration = (int) accelerometerHandler.getAccelX();
                Vec2 vec2 = new Vec2(paff.getX() - provaBubble.getX(), (paff.getY() - provaBubble.getY())*10);

                if(currentAcceleration > 0) {
                    vec2.rotate(-90);
                    paff.getBody().applyForceToCenter(vec2, false);
                }
                    else if(currentAcceleration < 0) {
                    vec2.rotate(90);
                    paff.getBody().applyForceToCenter(vec2,false);
                }
                if(accelerometerHandler.isAccelXOpposite(previousAcceleration)){
                    float inertia = paff.getBody().getInertia();
                    paff.getBody().applyForceToCenter(new Vec2(vec2.getX()*inertia,vec2.getY()*inertia),false);
                }
                previousAcceleration = currentAcceleration;
                Log.e("DATI ACCELEROMETRO : ", "X=" + accelerometerHandler.getAccelX() + "\n Y=" + accelerometerHandler.getAccelY());
                break;
            case WAITING:
            Log.e("WAITING", "WAITING");
            break;
            case JOINT:
                Log.e("JOINT", "JOINT");
                DistanceJointDef distanceJointDef = new DistanceJointDef();
                distanceJointDef.setBodyA(paff.getBody());
                distanceJointDef.setBodyB(collidedBubble);
                distanceJointDef.setFrequencyHz(0);
                distanceJointDef.setDampingRatio(0);
                Bubble collidedCuccleobj = (Bubble) collidedBubble.getUserData();
                distanceJointDef.setLength(paff.getRadius()+collidedCuccleobj.getRadius());
                // distanceJointDef.setLocalAnchorA(paff.getX(), paff.getY());
                //distanceJointDef.setLocalAnchorB(collidedBubble.getPositionX(), collidedBubble.getPositionY());
                Joint distanceJoint2 = world.createJoint(distanceJointDef);

              distanceJointDef.delete();
               /* RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.setBodyA(paff.getBody());
             revoluteJointDef.setBodyB(provaBubble2.getBody());
             revoluteJointDef.setLocalAnchorA(provaBubble2.getX(),provaBubble2.getY());
             revoluteJointDef.setLocalAnchorB(0,0);
             revoluteJoint = world.createJoint(revoluteJointDef);

                revoluteJointDef.delete();*/
                gameState = GameState.ROTATE;
            break;
            default:
            break;
    }
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
        //if(collidedBubble.equals(provaBubble2.getBody())){
            Log.e("CONTATTO", "entrato in collided");
            gameState = GameState.JOINT;
        //}
    }
}
