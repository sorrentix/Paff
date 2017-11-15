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
       // world.setContactListener(paffContactListener);
        /* for (int i = 0; i < 5; i++) {
            bubbles[i] = new Bubble(this, new Vec2(((float) generator.nextInt(160) / 10.0f) - 8.0f, ((float) generator.nextInt(150) / 10.0f) + 15.0f), (generator.nextFloat() % 4) + 1);
        }*/// int randomNum = rand.nextInt((max - min) + 1) + min;
        provaBubble =new Bubble(this, new Vec2(3, 3), 2,0);
        paff = new Bubble(this, new Vec2(6, 3), 1,0.5f);


        Log.e("DIMENSIONI:","Paff: \n x: "+paff.getX()+
                                            "\ny:"+paff.getY()+
                                            "\nBubble:"+
                                            "\nx:"+provaBubble.getX()+
                                            "\ny:"+provaBubble.getY());


        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.setBodyA(paff.getBody());
        distanceJointDef.setBodyB(provaBubble.getBody());
        //distanceJointDef.setLocalAnchorA(paff.getX(), paff.getY());
        //distanceJointDef.setLocalAnchorB(provaBubble.getX(), provaBubble.getY());
        distanceJointDef.setFrequencyHz(0);
        distanceJointDef.setLength(paff.getRadius()+provaBubble.getRadius());
        distanceJoint = world.createJoint(distanceJointDef);

        distanceJointDef.delete();
    }

    public GameState getGameState() {
        return gameState;
    }

    public synchronized void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

        Log.e("DIMENSIONI:","Paff: \n x: "+paff.getX()+
                "\ny:"+paff.getY()+
                "\nBubble:"+
                "\nx:"+provaBubble.getX()+
                "\ny:"+provaBubble.getY());

/*        for (int i = 0; i < 5; i++){
            if (bubbles.get(i).getY()+bubbles.get(i).getRadius()<=physicalSize.ymin)
                bubbles.get(i).getBody().
        }*/

        // paff.getBody().applyForce(new Vec2(0,-accelerometerHandler.getAccelX()),new Vec2(paff.getX(),paff.getY()),false);


        switch (gameState) {
            case Spara:
                //Log.e("SPARA", "SPARA");
                Vec2 vec = new Vec2(paff.getX() - bubbles[bubbles.length - 1].getX(), paff.getY() - bubbles[bubbles.length - 1].getY());
                world.destroyJoint(distanceJoint);
                paff.getBody().setAngularVelocity(0);
                paff.getBody().setLinearVelocity(new Vec2(paff.getX(), paff.getY()));
                paff.getBody().applyLinearImpulse(vec, new Vec2(paff.getX(), paff.getY()), false);
                gameState = GameState.Waiting;
                break;
            case Ruota:
               /* //Log.e("RUOTA", "RUOTA");
                currentAcceleration = (int) accelerometerHandler.getAccelX();
                float torque = paff.getBody().getAngularVelocity();
                // Log.e("TORQUE", "" + torque);
                if (Math.abs(torque) > 2)
                    paff.getBody().applyTorque(-2 * torque, false);


                if (currentAcceleration > 0) { // FLAG = TRUE


                    paff.getBody().applyTorque(15, false);
                } else if (currentAcceleration < 0) { // FLAG = FALSE

                    paff.getBody().applyTorque(-15, false);
                } else {
                    if (torque != 0) {
                        torque = paff.getBody().getAngularVelocity();
                        paff.getBody().applyTorque(-torque, false);
                    } else
                        paff.getBody().applyTorque(0, false);
                }
                if (accelerometerHandler.isAccelXOpposite(previousAcceleration) /*&& Math.abs(torque) > 3) {
                  //  Log.e("CAMBIOO", " OK ");
                    paff.getBody().applyTorque(-20 * torque, false);

                }

                previousAcceleration = currentAcceleration;
                // Log.e("DATI ACCELEROMETRO : ", "X=" + accelerometerHandler.getAccelX() + "\n Y=" + accelerometerHandler.getAccelY());
               */
               paff.getBody().setAngularVelocity(0);
                break;
            case Waiting:
              //  Log.e("WAITING", "WAITING");
                break;
            case JOINT:
                //Log.e("JOINT", "JOINT");
                DistanceJointDef distanceJointDef = new DistanceJointDef();
                distanceJointDef.setBodyA(paff.getBody());
                distanceJointDef.setBodyB(collidedBubble);
                distanceJointDef.setLocalAnchorA(paff.getX(), paff.getY());
                distanceJointDef.setLocalAnchorB(collidedBubble.getPositionX(), collidedBubble.getPositionY());
                Joint distanceJoint2 = world.createJoint(distanceJointDef);

                distanceJointDef.delete();
                gameState = GameState.Ruota;
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
        if(!(collidedBubble.equals(bubbles[bubbles.length-1])))
                gameState = GameState.JOINT;
    }
}
