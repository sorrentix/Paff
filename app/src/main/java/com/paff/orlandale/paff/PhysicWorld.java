package com.paff.orlandale.paff;

import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Screen;
import com.google.fpl.liquidfun.BodyType;
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

    final Box physicalSize;

    private static final float TIME_STEP = GlobalConstants.FPS; //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;


    int currentAcceleration = 0;
    int previousAcceleration = 0;
    private Input input;
    private Physic collidedBubble;
    private PaffContactListener paffContactListener;

    GameState gameState = GameState.WAITING;

    GameObject paff;
    Pool<GameObject>  bubblesPool;
    List<GameObject>  activeBubbles = new ArrayList<>();
    List<GameObject>  toRemoveBubbles = new ArrayList<>();

    public PhysicWorld(Box physicalSize, Input input) {
        this.physicalSize = physicalSize;
        this.input = input;

        this.world = new World(GlobalConstants.GRAVITY.getX(), GlobalConstants.GRAVITY.getY());


        paff       = Screen.setBubble(this,GlobalConstants.PAFF_RADIUS,new Vec2(2.5f, 3.9f),BodyType.dynamicBody, input);

        bubblesPool = initPool(this, input);
        for( int i = 0; i < GlobalConstants.BUBBLE_NUMBER; i++){
            activeBubbles.add(bubblesPool.newObject());
        }/*
        bubbles[0] = Screen.setBubble(this,2.0f,new Vec2(2.5f, 2),BodyType.staticBody, input);
        bubbles[1] = Screen.setBubble(this,1.8f,new Vec2(7, -7),BodyType.staticBody, input);
        bubbles[2] = Screen.setBubble(this,2.0f,new Vec2(-3.5f, 8),BodyType.staticBody, input);
        bubbles[3] = Screen.setBubble(this,1.7f,new Vec2(-4.5f, -3),BodyType.staticBody, input);
        bubbles[4] = Screen.setBubble(this,2.0f,new Vec2(-6, -9),BodyType.staticBody, input);
*/
        paffContactListener = new PaffContactListener(this);
        world.setContactListener(paffContactListener);
    }


    public GameState getGameState() {
        return gameState;
    }

    public synchronized void update() {
        if( GlobalConstants.BUBBLE_NUMBER > activeBubbles.size()){
            activeBubbles.add(bubblesPool.newObject());
        }
        switch (gameState) {
            case SHOT:
                Log.e("SPARA", "SPARA");
                paff.physic.nullifyResidualVelocity();
                paff.physic.computeForce(1000);
                paff.physic.breakJoint();
                paff.physic.applyForce();

                gameState = GameState.WAITING;
                break;
            case ROTATE:
                paff.physic.computeForce(20);
                paff.physic.computeForceDirection(input.getAccelX());
                if(paff.evtManager.isAccelXOpposite(previousAcceleration))
                    paff.physic.applyExtraBrakingForce();
                else
                    paff.physic.applyForce();

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
        paff.physic.checkToroidalWorld();
        for (GameObject b : activeBubbles){
            b.physic.fallSmoothly();
            markAsRemovableFallenBubble(b);
        }
        for (GameObject b : toRemoveBubbles ){
            activeBubbles.remove(b);
            bubblesPool.free(b);
        }

        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
    }
    public void markAsRemovableFallenBubble(GameObject b){
        if(b.physic.getPosY() + GlobalConstants.BUBBLE_BASIC_RADIUS  >= GlobalConstants.Physics.Y_MAX ){
            toRemoveBubbles.add(b);
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

    private Pool initPool(PhysicWorld w, Input i){
        final Random generator = new Random();
        final PhysicWorld container = this;
        final Input in = i;
        Pool.PoolObjectFactory<GameObject> factory = new Pool.PoolObjectFactory<GameObject>() {
            @Override
            public GameObject createObject() {
                float radius = generator.nextFloat()/2.0f;
                radius += GlobalConstants.BUBBLE_BASIC_RADIUS;
                Vec2 startingPosition = new Vec2(0,14.0f);
                GameObject bubble = Screen.setBubble(container,radius, startingPosition,BodyType.staticBody, in);
                bubble.physic.body.setSleepingAllowed(true);
                return bubble;
            }
        };
        return new Pool<GameObject>(factory, 15){

            private Vec2 restPosition = new Vec2(0,14.0f);
            private Vec2 respawnPosition = new Vec2(0,0);
            final Random generator = new Random();

            public void free(GameObject object) {
                super.free(object);
                object.physic.body.setTransform(restPosition,0);
                object.physic.body.setSleepingAllowed(true);
            }
            public GameObject newObject() {
                GameObject g = super.newObject();
                respawnPosition.setX(generator.nextFloat()*20.0f - 10.0f);
                respawnPosition.setY(generator.nextFloat()*20.0f - 10.0f);
                g.physic.body.setTransform(respawnPosition,0);
                g.physic.body.setSleepingAllowed(false);
                return g;
            }
        };
    }
}
