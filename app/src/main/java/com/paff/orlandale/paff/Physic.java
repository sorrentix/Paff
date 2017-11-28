package com.paff.orlandale.paff;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.Vec2;

/**
 * Classe che gestisce la componente fisica di un GameObject
 */
public class Physic implements Component{

    PhysicWorld world;
    public Shape shape;
    public Body body;
    public float expirationTime=-1;
    public float elapsedTime=0;
    public float startTime = System.nanoTime();
    public float totalPausedTime = 0;
    public Joint joint;
    public Vec2 force;
    private Vec2 nullForce = new Vec2(0,0);
    private Vec2 toroidalMovement = new Vec2(0,0);
    private Vec2 fallingMovement = new Vec2(0,0);
    private float radius;
    public float oldPosX;
    public double perlinSeed;


    /**
     * Costruttore della classe, inizializza i campi.
     * @param  wld  istanza del PhysicWorld associato al gioco
     * @param  expirationTime tempo di vita associato al GameObject
     */
    public Physic(PhysicWorld wld, float expirationTime){
        world = wld;
        this.expirationTime = expirationTime;
        force = new Vec2(0,0);
    }

    /**
     * Imposta un distance joint tra il GameObject corrente e quello in input
     * @param  elementB componente fisica del GameObject a cui applicare il distance joint
     */
    public void setDistanceJoint(Physic elementB){
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.setBodyA(this.body);
        jointDef.setBodyB(elementB.body);
        ((DistanceJointDef) jointDef).setFrequencyHz(0);
        ((DistanceJointDef) jointDef).setDampingRatio(0);
        ((DistanceJointDef) jointDef).setLength(this.getRadius()+elementB.getRadius());
        joint = world.world.createJoint(jointDef);
        jointDef.delete();
    }

    /**
     * Imposta come shape della componente fisica un cerchio
     * @param  radius raggio del cerchio
     */
    public void CircleShape(float radius){
        CircleShape c =  new CircleShape();
        c.setPosition(0.0f,0.0f);
        c.setRadius(radius);
        this.radius = radius;
        shape = c;
    }

    public void EdgeShape(){}

    public void PolygonShape(){}

    /**
     * Creazione del corpo fisico di un GameObject
     * @param  physicPosition  posizione di partenza nel mondo fisico
     * @param  density densità del corpo fisico, influisce sulla massa
     * @param  bodyType tipo di corpo (statico, cinematico, dinamico..)
     */
    public void Body( Vec2 physicPosition, float density, BodyType bodyType){
        BodyDef bdef = new BodyDef();
        bdef.setPosition(physicPosition);
        bdef.setType(bodyType);
        this.body = world.world.createBody(bdef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);
        this.body.setBullet(true);
        this.body.setLinearDamping(0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.setFriction(1);
        fixtureDef.setDensity(density);
        this.body.createFixture(fixtureDef);

        fixtureDef.delete();
        bdef.delete();
        shape.delete();
    }

    public float getPosX(){
        return body.getPositionX();
    }

    public float getRadius(){
        return radius;
    }

    public float getPosY(){
        return body.getPositionY();
    }


    /**
     * Calcola il vettore forza tra due GameObject, inteso come vettore distanza tra i due centri
     * moltiplicato per una costante.
     * @param  powerMultiplier costante moltiplicativa della forza
     */
    public void computeForce(float powerMultiplier){
        if(joint != null) {
            Physic elementJoined = ( joint.getBodyA().getUserData() == this) ?
                                    (Physic) joint.getBodyB().getUserData() : (Physic) joint.getBodyA().getUserData();

            float x = (this.getPosX() - elementJoined.getPosX());
            float y = (this.getPosY() - elementJoined.getPosY());

            /**
             * Per evitare che il vettore forza sia troppo elevato, viene inserito un tetto massimo
             */
            double forceValue = Math.sqrt(x*x + y*y);
            double forceCap = GlobalConstants.BUBBLE_BASIC_RADIUS + this.radius + GlobalConstants.BUBBLE_VARIATION_RADIUS;
            if( forceValue > forceCap){
                x = x * (float) (forceCap/ forceValue);
                y = y * (float) (forceCap/ forceValue);
            }

            x *= powerMultiplier;
            y *= powerMultiplier;
            force.set(x, y);
        }
    }

    /**
     * Calcola la direzione in cui applicare una forza per il GameObject
     * @param  value valore che indica la direzione della forza
     */
    public void computeForceDirection(float value){
        if(value > 0)
            force.rotate(-90);
        else if(value < 0)
            force.rotate(90);
    }


    /**
     * Applica la forza calcolata dal metodo computeForce al GameObject corrente
     */
    public  void applyForce(){
        this.body.applyForce(force,this.body.getPosition(),false);
    }


    /**
     * Azzera le velocità del corpo
     */
    public void nullifyResidualVelocity(){
        this.body.setAngularVelocity(0);
        this.body.setLinearVelocity(nullForce);
    }

    /**
     * Azzera la velocità lineare del corpo
     */
    public void  nullifyResidualLinearVelocity(){
        this.body.setLinearVelocity(nullForce);
    }


    /**
     * Distrugge il joint del corpo
     */
    public void breakJoint(){
        world.world.destroyJoint(joint);
        joint = null;
    }

    /**
     * Controlla che un GameObject si sposti nel mondo in modo toroidale.
     */
    public void checkToroidalWorld(){
        if( this.joint == null ) {
            if (this.getPosX() > world.physicalSize.xmax + radius) {
                toroidalMovement.setX(world.physicalSize.xmin - radius + 0.1f);
                toroidalMovement.setY(this.getPosY());
                this.body.setTransform(toroidalMovement, 0);
            } else if (this.getPosX() < world.physicalSize.xmin - radius) {
                toroidalMovement.setX(world.physicalSize.xmax + radius - 0.1f);
                toroidalMovement.setY(this.getPosY());
                this.body.setTransform(toroidalMovement, 0);
            }
        }
    }
    /**
     * Muove ad ogni step del mondo fisico il GameObject nel seguente modo
     * Verticalmente in modo rettilineo uniforme verso il basso
     * Orizzontalmente applicando il rumore di Perlin al GameObject, ottentedo un effetto ondulato
     */
    public void fallSmoothly(){
        this.perlinSeed += 0.007;
        fallingMovement.setX(this.oldPosX + ImprovedNoise.map((float)ImprovedNoise.noise(this.perlinSeed,0,0),(float)-Math.sqrt(0.25),(float)Math.sqrt(0.25),-1f,1f));
        fallingMovement.setY(this.getPosY() + world.gameSpeed);
        this.body.setTransform(fallingMovement,0);
    }
}
