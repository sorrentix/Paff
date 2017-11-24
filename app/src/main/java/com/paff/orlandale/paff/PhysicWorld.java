package com.paff.orlandale.paff;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
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

    private float paffPreviousPosition;
    public float scoreToAdd=0;


    float currentAcceleration = 0;
    float previousAcceleration = 0;
    private Input input;
    private Physic collidedBubble;
    private PaffContactListener paffContactListener;

    GameState gameState = GameState.WAITING;
    GameState previousState = GameState.WAITING;

    GameObject paff;
    Pool<GameObject>  bubblesPool;
    List<GameObject>  activeBubbles = new ArrayList<>();



    public PhysicWorld(Box physicalSize, Input input) {
        this.physicalSize = physicalSize;
        this.input = input;

        this.world = new World(GlobalConstants.GRAVITY.getX(), GlobalConstants.GRAVITY.getY());


        paff       = Screen.setBubble(this,GlobalConstants.PAFF_RADIUS,new Vec2(6.0f, 2.8f - GlobalConstants.BUBBLE_BASIC_RADIUS - 0.05f),BodyType.dynamicBody, input,-1);
        paffPreviousPosition = paff.physic.getPosY();
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
        if(paff.physic.getPosY()-paff.physic.getRadius()-0.2f > GlobalConstants.Physics.Y_MAX  )
            gameState = GameState.GAME_OVER;

        switch (gameState) {
            case SHOT:
              //  Log.e("SPARA", "SPARA");
                paff.physic.nullifyResidualVelocity();
                paff.physic.computeForce(480);//1000
                paff.physic.breakJoint();
                paff.physic.applyForce();

                world.setGravity(GlobalConstants.GRAVITY.getX(),GlobalConstants.GRAVITY.getY());
                gameState = GameState.WAITING;
                break;
            case ROTATE:
                currentAcceleration = input.getAccelX();
                float forceParameter =  Math.abs(currentAcceleration);
                paff.physic.computeForce(4 * forceParameter);//20
                paff.physic.computeForceDirection(input.getAccelX());
                paff.physic.applyForce();
                if(paff.evtManager.isAccelXOpposite(previousAcceleration)) {
                    paff.physic.computeForce(25 * forceParameter);
                    paff.physic.applyExtraBrakingForce();
                }
                previousAcceleration = currentAcceleration;

             //   Log.e("ROTATE : ", "X=" + input.getAccelX() + "\n Y=" + input.getAccelY());
                break;
            case WAITING:
            //    Log.e("WAITING", "WAITING");
            break;
            case JOINT:
                world.setGravity(0,0);
                paff.physic.setDistanceJoint(collidedBubble);
            //    Log.e("JOINT", "JOINT");

                gameState = GameState.ROTATE;
            break;
            default:
            break;
        }

        paff.physic.checkToroidalWorld();
        for (int i = 0; i < activeBubbles.size(); i++){
            activeBubbles.get(i).physic.fallSmoothly();
            if (markAsRemovableFallenBubble(activeBubbles.get(i)) || markAsExplodedBubble(activeBubbles.get(i))){
                GameObject b = activeBubbles.get(i);
                activeBubbles.remove(b);
                bubblesPool.free(b);
            }
        }

        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
    }
    public boolean markAsRemovableFallenBubble(GameObject b){
        boolean removable = (b.physic.getPosY() - b.physic.getRadius()  >= GlobalConstants.Physics.Y_MAX  );

    //    Log.e("BREAKING JOINT","1"+ " removable: "+ removable+ " jointpaff: "+paff.physic.joint);
        if (removable && paff.physic.joint != null){
          //  Log.e("BREAKING JOINT","2"+ "paff: "+paff.physic +" probable paff: "+paff.physic.joint.getBodyA().getUserData() +" probable other body:"+paff.physic.joint.getBodyB().getUserData()+ " other body: "+ b.physic );
            if (paff.physic.joint.getBodyB().getUserData().equals(b.physic) ) {
           //     Log.e("BREAKING JOINT","3");
                paff.physic.breakJoint();
                paff.physic.nullifyResidualLinearVelocity();
                world.setGravity(GlobalConstants.GRAVITY.getX(),GlobalConstants.GRAVITY.getY());
                gameState = GameState.WAITING;
            }
        }
        return removable;
    }

    public boolean markAsExplodedBubble(GameObject b) {
        b.physic.elapsedTime = (System.nanoTime()-b.physic.startTime)/1000000000.0f;
        boolean removable= ( b.physic.elapsedTime >= b.physic.expirationTime);

        if (removable && paff.physic.joint != null){
            //  Log.e("BREAKING JOINT","2"+ "paff: "+paff.physic +" probable paff: "+paff.physic.joint.getBodyA().getUserData() +" probable other body:"+paff.physic.joint.getBodyB().getUserData()+ " other body: "+ b.physic );
            if (paff.physic.joint.getBodyB().getUserData().equals(b.physic) ) {
                //     Log.e("BREAKING JOINT","3");
                paff.physic.breakJoint();
                paff.physic.nullifyResidualLinearVelocity();
                world.setGravity(GlobalConstants.GRAVITY.getX(),GlobalConstants.GRAVITY.getY());
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
        if( paff.physic.joint == null) {
            if (!ba.equals(paff.physic))
                collidedBubble = ba;
            else
                collidedBubble = bb;
            paff.sound.play();

            computeScore();

            gameState = GameState.JOINT;
        }
    }
    private void computeScore(){
        scoreToAdd += Math.abs((collidedBubble.getPosY()+GlobalConstants.Physics.Y_MAX)-(paffPreviousPosition+GlobalConstants.Physics.Y_MAX));

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
                float expiration = generator.nextFloat()*15.0f+5.0f;
                GameObject bubble = Screen.setBubble(container,radius, startingPosition,BodyType.staticBody, in,expiration);
                bubble.physic.body.setSleepingAllowed(true);
                return bubble;
            }
        };

        return new Pool<GameObject>(factory, 15){

            private Vec2 restPosition = new Vec2(0,30.0f);
            private Vec2 respawnPosition = new Vec2(0,0);
            private int bubbleCounter = 0;
            private GameObject highestBubble;
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
                g.physic.elapsedTime =0;
                g.physic.startTime =  System.nanoTime();
                if (bubbleCounter >= GlobalConstants.BUBBLE_NUMBER - 1)
                    highestBubble = g;
                return g;
            }

            private Vec2 setStartPosition(Vec2 respawnPosition){
                Vec2 respawn = respawnPosition;
                switch (bubbleCounter){
                    case 0:
                        respawn.setX(0.0f);
                        respawn.setY(14.0f);
                        break;
                    case 1:
                        respawn.setX(-7.0f);
                        respawn.setY(11.0f);
                        break;
                    case 2:
                        respawn.setX(-6.0f);
                        respawn.setY(-12.5f);
                        break;
                    case 3:
                        respawn.setX(6.0f);
                        respawn.setY(3.5f);
                        break;
                    case 4:
                        respawn.setX(-4.0f);
                        respawn.setY(-5.5f);
                        break;
                    case 5:
                        respawn.setX(6.0f);
                        respawn.setY(-8.5f);
                        break;
                    case 6:
                        respawn.setX(2.0f);
                        respawn.setY(-9.5f);
                        break;
                    case 7:
                        respawn.setX(-2.0f);
                        respawn.setY(-1.5f);
                        break;
                    case 8:
                        respawn.setX(-2.0f);
                        respawn.setY(-13.5f);
                        break;
                    case 9:
                        respawn.setX(-6.0f);
                        respawn.setY(-18.5f);
                        break;
                    case 10:
                        respawn.setX(6.5f);
                        respawn.setY(-17.5f);
                        break;
                    case 11:
                        respawn.setX(-1.5f);
                        respawn.setY(6.5f);
                        break;
                    default:
                        float y = generator.nextFloat()*(4.0f) +  (GlobalConstants.Physics.Y_MIN - 8.0f);
                        float x = (generator.nextFloat()*14.0f - (GlobalConstants.Physics.X_MAX - GlobalConstants.BUBBLE_BASIC_RADIUS - 2.0f));
                        float y2MinusY1Quadro = (highestBubble.physic.getPosY()- y) * (highestBubble.physic.getPosY()- y);
                        float wantedDistance = (4 * GlobalConstants.BUBBLE_BASIC_RADIUS) + 2.0f;

                        if (Math.sqrt((highestBubble.physic.getPosX()- x) * (highestBubble.physic.getPosX()- x)+ y2MinusY1Quadro) < wantedDistance ){
                            double partial =  Math.sqrt((wantedDistance*wantedDistance) - y2MinusY1Quadro);
                            x = (float)(highestBubble.physic.getPosX() - partial);
                            if (x + GlobalConstants.BUBBLE_BASIC_RADIUS + 2.0f > GlobalConstants.Physics.X_MAX ||  x - GlobalConstants.BUBBLE_BASIC_RADIUS - 2.0f < GlobalConstants.Physics.X_MIN)
                                x = (float) (highestBubble.physic.getPosX() + partial);
                       //     Log.e("RANDOM POSITION"," NOT GOOD");
                        }else{
                        //    Log.e("RANDOM POSITION"," GOOD");

                        }
                        respawn.setY(y);
                        respawn.setX(x);
                        //respawn.setX(generator.nextFloat()*11.0f - (GlobalConstants.Physics.X_MAX - GlobalConstants.BUBBLE_BASIC_RADIUS - 2.0f));00000000000000
                        //respawn.setY(GlobalConstants.Physics.Y_MIN - GlobalConstants.BUBBLE_BASIC_RADIUS - 0.2f);
                        break;
                }
                bubbleCounter++;
                return  respawn;
            }
        };


    }


}
