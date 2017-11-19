package com.paff.orlandale.paff;

import android.graphics.Color;
import android.graphics.RectF;
import android.support.v4.math.MathUtils;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
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

    private static final float TIME_STEP = GlobalConstants.FPS; //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    private Vec2 gravity;

    int currentAcceleration = 0;
    int previousAcceleration = 0;
    Joint distanceJoint;
    AccelerometerHandler accelerometerHandler;
    private PaffContactListener paffContactListener;

    GameState gameState = GameState.ROTATE;

    private List<GameObject> gameObjects;
    private GameObject paff;
    private Body collidedBubble;
    private Random generator;

    private double t = 0;

    public PhysicWorld(Vec2 gravity, AccelerometerHandler accelerometerHandler) {
        this.gravity = gravity;
        this.accelerometerHandler = accelerometerHandler;

        this.world = new World(gravity.getX(), gravity.getY());

        //CONTACT LISTENER SETUP
        paffContactListener = new PaffContactListener(this);
        world.setContactListener(paffContactListener);

        gameObjects = new ArrayList<>();
        generator = new Random();
    }

    public GameState getGameState() {
        return gameState;
    }

    public synchronized void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);


        for (int i = 0; i < gameObjects.size(); i++){
            if (gameObjects.get(i).getY()>GlobalConstants.Physics.Y_MAX){
                gameObjects.get(i).getBody().setTransform(generator.nextInt(15) -7,GlobalConstants.Physics.Y_MIN-4,0);//TODO - getRadius
            }
            else {
                //Log.e("PERLIN NOISE", "getX: "+gameObjects.get(0).getX()+"  perlin:"+ImprovedNoise.map((float)ImprovedNoise.noise(t,0,0),(float)-Math.sqrt(0.25),(float)Math.sqrt(0.25),-2f,2f)+"  somma: "+(ImprovedNoise.map((float)ImprovedNoise.noise(t,0,0),(float)-Math.sqrt(0.25),(float)Math.sqrt(0.25),-2f,2f) + gameObjects.get(0).getX()));
                getGameObjects().get(i).perlinSeed+=0.01;
                gameObjects.get(i).getBody().setTransform(gameObjects.get(i).oldPos+ImprovedNoise.map((float)ImprovedNoise.noise(getGameObjects().get(i).perlinSeed,0,0),(float)-Math.sqrt(0.25),(float)Math.sqrt(0.25),-2f,2f), gameObjects.get(i).getY() + 0.05f, 0);
            }
        }

        switch (gameState) {
            case SHOT:
                Log.e("SPARA", "SPARA");
                Vec2 vec = new Vec2((paff.getX() - collidedBubble.getPositionX())*15, (paff.getY() - collidedBubble.getPositionY())*15);
                world.destroyJoint(distanceJoint);
                paff.getBody().setAngularVelocity(0);
                paff.getBody().applyLinearImpulse(vec, paff.getCenter(), false);
                world.setGravity(0,10);
                gameState = GameState.WAITING;
                break;
            case ROTATE:
                //Log.e("RUOTA", "RUOTA");
                currentAcceleration = (int) accelerometerHandler.getAccelX();
                Vec2 velocity = new Vec2( (paff.getX() - collidedBubble.getPositionX())*10*Math.abs(currentAcceleration), (paff.getY() - collidedBubble.getPositionY())*10*Math.abs(currentAcceleration));
                if(currentAcceleration > 0) {
                    velocity.rotate(-90);
                    paff.getBody().applyForce(velocity, paff.getCenter(), false);
                }
                    else if(currentAcceleration < 0) {
                    velocity.rotate(90);
                    paff.getBody().applyForce(velocity,paff.getCenter(),false);
                }
                if(accelerometerHandler.isAccelXOpposite(previousAcceleration)){
                    paff.getBody().applyForce(new Vec2(velocity.getX()*25,velocity.getY()*25),paff.getCenter(),false);
                }

                previousAcceleration = currentAcceleration;
                //Log.e("DATI ACCELEROMETRO : ", "X=" + accelerometerHandler.getAccelX() + "\n Y=" + accelerometerHandler.getAccelY());
                break;
            case WAITING:
                //Log.e("WAITING", "WAITING");
                if (paff.getX()>GlobalConstants.Physics.X_MAX)
                    paff.getBody().setTransform(new Vec2(GlobalConstants.Physics.X_MIN,paff.getY()),0);
                else if (paff.getX()<GlobalConstants.Physics.X_MIN)
                    paff.getBody().setTransform(new Vec2(GlobalConstants.Physics.X_MAX,paff.getY()),0);
                break;
            case JOINT:
                Log.e("JOINT", "JOINT");
                world.setGravity(0,0);
                createHardDistanceJoint(paff,(GameObject)collidedBubble.getUserData());
                paff.getBody().setAngularVelocity(0);
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

    public void setSpara(GameState state) {
        gameState = state;
    }

    public void collisionDetected(Body ba, Body bb) {
        if (ba.equals(paff.getBody()))
            collidedBubble = bb;
        else
            collidedBubble = ba;
        Log.e("CONTATTO", "entrato in collided");
        gameState = GameState.JOINT;
    }

    public List<GameObject> getGameObjects(){
        return gameObjects;
    }

    public void createHardDistanceJoint(GameObject a, GameObject b){
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.setBodyA(a.getBody());
        distanceJointDef.setBodyB(b.getBody());
        distanceJointDef.setFrequencyHz(0);
        distanceJointDef.setDampingRatio(0);

        if (a instanceof Bubble && b instanceof Bubble) {
            Bubble bubbleA = (Bubble) a;
            Bubble bubbleB = (Bubble) b;
            distanceJointDef.setLength(bubbleA.getRadius() + bubbleB.getRadius());
            distanceJoint = world.createJoint(distanceJointDef);
        }else {
            distanceJointDef.setLocalAnchorA(0,0);
            distanceJointDef.setLocalAnchorB(0,0);
        }
        distanceJointDef.delete();
    }

    public GameObject getPaff(){
        return paff;
    }

    public void setPaff(GameObject p){
        paff = p;
    }

}
