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

    public PhysicWorld(Box physicalSize, Input input) {
        this.physicalSize = physicalSize;
        this.input = input;

        this.world = new World(GlobalConstants.GRAVITY.getX(), GlobalConstants.GRAVITY.getY());


        paff       = Screen.setBubble(this,GlobalConstants.PAFF_RADIUS,new Vec2(6.0f, -0.5f - GlobalConstants.BUBBLE_BASIC_RADIUS - 0.02f),BodyType.dynamicBody, input);

        bubblesPool = initPool(this, input);
        for( int i = 0; i < GlobalConstants.BUBBLE_NUMBER; i++){
            activeBubbles.add(bubblesPool.newObject());
        }

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
                paff.physic.computeForce(400);//1000
                paff.physic.breakJoint();
                paff.physic.applyForce();

                world.setGravity(GlobalConstants.GRAVITY.getX(),GlobalConstants.GRAVITY.getY());
                gameState = GameState.WAITING;
                break;
            case ROTATE:
                paff.physic.computeForce(5);//20
                paff.physic.computeForceDirection(input.getAccelX());
                paff.physic.applyForce();
                if(paff.evtManager.isAccelXOpposite(previousAcceleration)) {
                    paff.physic.computeForce(20);
                    paff.physic.applyExtraBrakingForce();
                }


                previousAcceleration = currentAcceleration;

                Log.e("ROTATE : ", "X=" + input.getAccelX() + "\n Y=" + input.getAccelY());
                break;
            case WAITING:
                Log.e("WAITING", "WAITING");
            break;
            case JOINT:
                world.setGravity(0,0);
                paff.physic.setDistanceJoint(collidedBubble);
                Log.e("JOINT", "JOINT");

                gameState = GameState.ROTATE;
            break;
            default:
            break;
        }

        paff.physic.checkToroidalWorld();
        for (int i = 0; i < activeBubbles.size(); i++){
            activeBubbles.get(i).physic.fallSmoothly();
            if (markAsRemovableFallenBubble(activeBubbles.get(i))){
                GameObject b = activeBubbles.get(i);
                activeBubbles.remove(b);
                bubblesPool.free(b);
            }
        }

        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
    }
    public boolean markAsRemovableFallenBubble(GameObject b){
        boolean removable = (b.physic.getPosY() - b.physic.getRadius()  >= GlobalConstants.Physics.Y_MAX  );
        Log.e("BREAKING JOINT","1"+ " removable: "+ removable+ " jointpaff: "+paff.physic.joint);
        if (removable && paff.physic.joint != null){
            Log.e("BREAKING JOINT","2"+ "paff: "+paff.physic +" probable paff: "+paff.physic.joint.getBodyA().getUserData() +" probable other body:"+paff.physic.joint.getBodyB().getUserData()+ " other body: "+ b.physic );
            if (paff.physic.joint.getBodyB().getUserData().equals(b.physic) ) {
                Log.e("BREAKING JOINT","3");
                paff.physic.breakJoint();
                gameState = GameState.WAITING;
            }
        }
        return removable;
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

            private Vec2 restPosition = new Vec2(0,30.0f);
            private Vec2 respawnPosition = new Vec2(0,0);
            private int bubbleCounter = 0;
            final Random generator = new Random();

            public void free(GameObject object) {
                super.free(object);
                object.physic.body.setTransform(restPosition,0);
                object.physic.body.setSleepingAllowed(true);
            }
            public GameObject newObject() {
                GameObject g = super.newObject();
                respawnPosition = setStartPosition(respawnPosition);
                g.physic.perlinSeed = generator.nextInt()*100;
                g.physic.body.setTransform(respawnPosition,0);
                g.physic.oldPosX = respawnPosition.getX();
                g.physic.body.setSleepingAllowed(false);
                return g;
            }

            private Vec2 setStartPosition(Vec2 respawnPosition){
                Vec2 respawn = respawnPosition;
                switch (bubbleCounter){
                    case 0:
                        respawn.setX(0.0f);
                        respawn.setY(15.0f);
                        break;
                    case 1:
                        respawn.setX(-8.0f);
                        respawn.setY(13.0f);
                        break;
                    case 2:
                        respawn.setX(8.0f);
                        respawn.setY(12.5f);
                        break;
                    case 3:
                        respawn.setX(6.0f);
                        respawn.setY(0.5f);
                        break;
                    case 4:
                        respawn.setX(-4.0f);
                        respawn.setY(-5.5f);
                        break;
                    default:
                        respawn.setX(generator.nextFloat()*11.0f - (GlobalConstants.Physics.X_MAX - GlobalConstants.BUBBLE_BASIC_RADIUS - 2.0f));
                        respawn.setY(GlobalConstants.Physics.Y_MIN - GlobalConstants.BUBBLE_BASIC_RADIUS - 0.2f);
                        break;
                }
                bubbleCounter++;
                return  respawn;
            }
        };


    }


}
