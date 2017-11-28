package com.paff.orlandale.paff;

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
/**
 * Classe per la gestione del mondo fisico.
 */
public class PhysicWorld {
    World world;


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

    public float timeOfSpeedIncrement  = System.nanoTime()/1000000000.0f;
    public float gameSpeed = 0.02f;

    public float pausedTime = 0;
    public float totalPausedTime = 0;
  
    GameState gameState = GameState.SETUP;
    GameState previousState = GameState.WAITING;

    GameObject paff;
    Pool<GameObject>  bubblesPool;
    List<GameObject>  activeBubbles = new ArrayList<>();

    boolean fallFlag = true;
    boolean newhighscore = false;

    Box physicalSize;
  
    /**
     * Costruttore della classe:
     * <ul>
     *     <li>Crea il mondo fisico</li>
     *     <li>Crea paff</li>
     *     <li>Crea un Pool di bolle</li>
     *     <li>Inizializza il contact listener</li>
     * </ul>
     * @param  physicalSize  dimensioni del mondo fisico
     * @param  input  gestore degli input dell'utente (usato per la gestione dell'acceleromentro)
     */
    public PhysicWorld(Box physicalSize, Input input) {
        this.physicalSize = physicalSize;
        this.input = input;
        this.world = new World(GlobalConstants.GRAVITY.getX(), GlobalConstants.GRAVITY.getY());
        paff = Screen.setBubble(this,GlobalConstants.PAFF_RADIUS,new Vec2(6.0f, 2.8f - GlobalConstants.BUBBLE_BASIC_RADIUS - 0.05f),BodyType.dynamicBody, input,-1);
        paffPreviousPosition = paff.physic.getPosY();
        bubblesPool = initPool(input);
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
        /**
         * Se non sono attive all'interno del gioco abbastanza bolle, ne viene attivata una nuova dal Pool
         */
        if( GlobalConstants.BUBBLE_NUMBER > activeBubbles.size()){
            activeBubbles.add(bubblesPool.newObject());
        }
         /**
         * Verifica la condizione di game over --> paff è uscito dallo schermo.
         */
        if(paff.physic.getPosY()-paff.physic.getRadius()-0.2f > GlobalConstants.Physics.Y_MAX  ) {
            gameState = GameState.GAME_OVER;
            if(!newhighscore)
                Assets.fall_gameover.play();
        }

        paff.physic.body.setLinearDamping(0f);

        /**
         * Gestisce gli stati principali in cui può trovarsi Paff.
         * <ul>
         *     <li>SHOT: l'utente ha premuto sullo schermo, mentre Paff era legato ad una bolla
         *         richiedendo il lancio di Paff</li>
         *     <li>ROTATE: Paff è legato ad una bolla: viene gestito il movimento tramite acceleromentro</li>
         *     <li>WAITING: Paff è in volo tra due bolle</li>
         *     <li>JOINT: Paff ha appena effettuato una collisione con una bolla, viene creato un DistanceJoint</li>
         * </ul>
         */
        switch (gameState) {
            case SHOT:
                paff.physic.nullifyResidualVelocity();
                paff.physic.computeForce(450);//1000
                paff.physic.breakJoint();
                paff.physic.applyForce();
                world.setGravity(GlobalConstants.GRAVITY.getX(),GlobalConstants.GRAVITY.getY());
                fallFlag = false;
                gameState = GameState.WAITING;
                break;
            case ROTATE:
                currentAcceleration = input.getAccelX();
                int forceParameter =  Math.abs((int)currentAcceleration);
                paff.physic.computeForce(6 *forceParameter);//20
                paff.physic.computeForceDirection(currentAcceleration);
                paff.physic.applyForce();
                if(paff.evtManager.isAccelXOpposite(previousAcceleration)) {
                    paff.physic.body.setLinearDamping(5);
                }
                else  if (paff.evtManager.isAccelXLess(previousAcceleration)){
                    paff.physic.body.setLinearDamping(5f);
                }

                previousAcceleration = currentAcceleration;
                break;

            case WAITING:
                fallFlag = true;
                break;
            case JOINT:
                    world.setGravity(0,0);
                    paff.physic.setDistanceJoint(collidedBubble);
                    gameState = GameState.ROTATE;
                break;
            default:
                break;
            }

        paff.physic.checkToroidalWorld();

        /**
         * Le bolle esplose o uscite fuori dallo schermo vengono riaggiunte al Pool
         */
        for (int i = 0; i < activeBubbles.size(); i++){
            if (fallFlag)
                activeBubbles.get(i).physic.fallSmoothly();
            if (markAsRemovableFallenBubble(activeBubbles.get(i)) || markAsExplodedBubble(activeBubbles.get(i))){
                GameObject b = activeBubbles.get(i);
                activeBubbles.remove(b);
                bubblesPool.free(b);
            }
        }

        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

        /**
         * Incrementa la velocità del gioco ogni GlobalConstants.SPEEDUP_TIME secondi
         */
        if ( (System.nanoTime()/1000000000.0f)-timeOfSpeedIncrement-totalPausedTime > GlobalConstants.SPEEDUP_TIME){
            timeOfSpeedIncrement = (System.nanoTime()/1000000000.0f);
            gameSpeed += 0.01f;
        }
    }

    /**
     * Verifica se un GameObject è uscito fuori dai limiti del mondo fisico
     * @param  b GameObject su cui è effettuata la verifica
     */
    public boolean markAsRemovableFallenBubble(GameObject b){
        boolean removable = (b.physic.getPosY() - b.physic.getRadius()  >= GlobalConstants.Physics.Y_MAX  );

        if (removable && paff.physic.joint != null){
            if (paff.physic.joint.getBodyB().getUserData().equals(b.physic) ) {
                paff.physic.breakJoint();
                paff.physic.nullifyResidualLinearVelocity();
                world.setGravity(GlobalConstants.GRAVITY.getX(),GlobalConstants.GRAVITY.getY());
                gameState = GameState.WAITING;
            }
        }
        return removable;
    }

    /**
     * Verifica se un GameObject ha terminato la sua vita
     * @param  b GameObject su cui è effettuata la verifica
     */
    public boolean markAsExplodedBubble(GameObject b) {
        b.physic.elapsedTime = ((System.nanoTime()-b.physic.startTime)/1000000000.0f) - b.physic.totalPausedTime;
        boolean removable= ( b.physic.elapsedTime >= b.physic.expirationTime);

        if (removable && paff.physic.joint != null){
            if (paff.physic.joint.getBodyB().getUserData().equals(b.physic) ) {
                paff.physic.breakJoint();
                paff.physic.nullifyResidualLinearVelocity();
                world.setGravity(GlobalConstants.GRAVITY.getX(),GlobalConstants.GRAVITY.getY());
                gameState = GameState.WAITING;
            }
        }
        return removable;
    }

    @Override
    public void finalize() {
        world.delete();
    }

    /**
     * Gestisce le collisioni:
     * <ul>
     *     <li>Esegue il suono di collisione</li>
     *     <li>Calcola lo score</li>
     *     <li>Imposta lo stato del mondo fisico a JOINT, in modo da avviare al prossimo world step
     *         le operazioni per la creazione del joint tra due GameObject</li>
     * </ul>
     * @param  ba componente fisica del primo GameObject su cui si verifica la collisione
     * @param  bb componente fisica del secondo GameObject su cui si verifica la collisione
     */
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

    /**
     * Incrementa il punteggio
     */
    private void computeScore(){
        scoreToAdd += Math.abs((collidedBubble.getPosY()+GlobalConstants.Physics.Y_MAX)-(paffPreviousPosition+GlobalConstants.Physics.Y_MAX))+Camera.getVerticalSpace();
    }

    /**
     * Metoodo che inizializza il  Pool di bolle
     * @param  i gestore degli eventi
     */
    private Pool<GameObject> initPool(Input i){
        final Random generator = new Random();
        final PhysicWorld container = this;
        final Input in = i;

        /**
         * Classe Anonima che estende PoolObjectFactory, in particolare fa override del metoodo
         * createObject, per creare bolle in una posizione di riposo fuori campo rispetto al mondo fisico
         */
        Pool.PoolObjectFactory<GameObject> factory = new Pool.PoolObjectFactory<GameObject>() {
            @Override
            public GameObject createObject() {
                float radius = generator.nextFloat() * GlobalConstants.BUBBLE_VARIATION_RADIUS;
                radius += GlobalConstants.BUBBLE_BASIC_RADIUS;
                Vec2 startingPosition = new Vec2(0,14.0f);
                float expiration = generator.nextFloat()*15.0f+5.0f;
                GameObject bubble = Screen.setBubble(container,radius, startingPosition,BodyType.staticBody, in,expiration);
                bubble.physic.body.setSleepingAllowed(true);
                return bubble;
            }
        };

        /**
         * Classe anonima per la creazione di un Pool di GameObject.
         * In particolare ridefinisce i metodi newObject e free per eseguire
         * alcune operazioni di inizializzazione sugli oggetti
         */
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
                g.physic.totalPausedTime = 0;
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
                        }
                        respawn.setY(y);
                        respawn.setX(x);
                        break;
                }
                bubbleCounter++;
                return  respawn;
            }
        };


    }


}
