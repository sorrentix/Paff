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

    public PhysicWorld(Vec2 gravity, AccelerometerHandler accelerometerHandler) {
        this.gravity = gravity;
        this.accelerometerHandler = accelerometerHandler;

        this.world = new World(gravity.getX(), gravity.getY());

        //CONTACT LISTENER SETUP
        paffContactListener = new PaffContactListener(this);
        world.setContactListener(paffContactListener);

        gameObjects = new ArrayList<>();

    }

    public GameState getGameState() {
        return gameState;
    }

    public synchronized void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

        switch (gameState) {
            case SHOT:
                Log.e("SPARA", "SPARA");
                Vec2 vec = new Vec2(paff.getX() - collidedBubble.getPositionX()*10, (paff.getY() - collidedBubble.getPositionY())*10);
                world.destroyJoint(distanceJoint);
                paff.getBody().setAngularVelocity(0);
                //paff.getBody().setLinearVelocity(new Vec2(paff.getX(), paff.getY()));
                paff.getBody().applyLinearImpulse(vec, new Vec2(paff.getX(), paff.getY()), false);
                gameState = GameState.WAITING;
                break;
            case ROTATE:
                //Log.e("RUOTA", "RUOTA");
                currentAcceleration = (int) accelerometerHandler.getAccelX();
                Vec2 vec2 = new Vec2(paff.getX() - collidedBubble.getPositionX()*10, (paff.getY() - collidedBubble.getPositionY())*10);

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
                //Log.e("DATI ACCELEROMETRO : ", "X=" + accelerometerHandler.getAccelX() + "\n Y=" + accelerometerHandler.getAccelY());
                break;
            case WAITING:
                //Log.e("WAITING", "WAITING");
                break;
            case JOINT:
                Log.e("JOINT", "JOINT");
                createHardDistanceJoint(paff,(GameObject)collidedBubble.getUserData());
                paff.getBody().setAngularVelocity(0);
                paff.getBody().setLinearVelocity(new Vec2(paff.getX(), paff.getY()));
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
