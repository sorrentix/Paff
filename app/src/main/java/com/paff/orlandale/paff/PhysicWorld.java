package com.paff.orlandale.paff;

import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AccelerometerHandler;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
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

    //Bubble bubbles[] = new Bubble[5];
    int currentAcceleration = 0;
    int previousAcceleration = 0;
    Joint distanceJoint;
    Input input;
    private Physic collidedBubble;
    private PaffContactListener paffContactListener;

    GameState gameState = GameState.WAITING;

    GameObject paff;
    GameObject []bubbles = new GameObject[5];

    public PhysicWorld(Box physicalSize, Box screenSize, Vec2 gravity, Input input) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.gravity = gravity;
        this.framebufferWidth = screenSize.xmax;
        this.framebufferHeight = screenSize.ymax;
        this.input = input;

        this.world = new World(gravity.getX(), gravity.getY());
        Random generator = new Random();


        paff       = Screen.setBubble(this,1.0f,new Vec2(2.5f, 3.9f),BodyType.dynamicBody, input);
        bubbles[0] = Screen.setBubble(this,2.0f,new Vec2(2.5f, 2),BodyType.staticBody, input);
        bubbles[1] = Screen.setBubble(this,1.8f,new Vec2(7, -7),BodyType.staticBody, input);
        bubbles[2] = Screen.setBubble(this,2.0f,new Vec2(-3.5f, 8),BodyType.staticBody, input);
        bubbles[3] = Screen.setBubble(this,1.7f,new Vec2(-4.5f, -3),BodyType.staticBody, input);
        bubbles[4] = Screen.setBubble(this,2.0f,new Vec2(-6, -9),BodyType.staticBody, input);

        paffContactListener = new PaffContactListener(this);
        world.setContactListener(paffContactListener);
    }


    public GameState getGameState() {
        return gameState;
    }

    public synchronized void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

        switch (gameState) {
            case SHOT:
                Log.e("SPARA", "SPARA");
                paff.physic.nullifyResidualVelocity();
                paff.physic.computeForce(1);
                paff.physic.breakJoint();
                paff.physic.applyForce();

                gameState = GameState.WAITING;
                break;
            case ROTATE:
                paff.physic.computeForce(0.1f);
                paff.physic.computeForceDirection(input.getAccelX());
                paff.physic.applyForce();
                if(paff.evtManager.isAccelXOpposite(previousAcceleration))
                    paff.physic.applyForceWithInertia();
                previousAcceleration = currentAcceleration;

                Log.e("DATI ACCELEROMETRO : ", "X=" + input.getAccelX() + "\n Y=" + input.getAccelY());
                break;
            case WAITING:
                Log.e("WAITING", "WAITING");
            break;
            case JOINT:
                paff.physic.setDistanceJoint(collidedBubble);
                Log.e("JOINT", "JOINT");

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


    public void collisionDetected(Physic ba, Physic bb) {
        if (!ba.equals(paff.physic))
            collidedBubble = ba;
        else
            collidedBubble = bb;
        paff.sound.play();
        gameState = GameState.JOINT;
    }
}
