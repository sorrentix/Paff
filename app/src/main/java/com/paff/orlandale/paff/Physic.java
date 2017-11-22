package com.paff.orlandale.paff;

import android.util.Log;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.Vec2;

public class Physic implements Component{

    PhysicWorld world;
    public Shape shape;
    public Body body;
    public float expirationTime=-1;
    public float elapsedTime=0;
    public float startTime = System.nanoTime();
    public Joint joint;
    public Vec2 force;
    private Vec2 nullForce = new Vec2(0,0);
    private Vec2 toroidalMovement = new Vec2(0,0);
    private Vec2 fallingMovement = new Vec2(0,0);
    private float radius;
    public float oldPosX;
    public double perlinSeed;


    public Physic(PhysicWorld wld, float expirationTime){
        world = wld;
        this.expirationTime = expirationTime;
        force = new Vec2(0,0);
    }



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

    public void CircleShape(float radius){
        CircleShape c =  new CircleShape();
        c.setPosition(0.0f,0.0f);
        c.setRadius(radius);
        this.radius = radius;
        shape = c;
    }

    public void EdgeShape(){}

    public void PolygonShape(){}

    public void Body( Vec2 physicPosition, float density, BodyType bodyType){
        BodyDef bdef = new BodyDef();
        bdef.setPosition(physicPosition);
        bdef.setType(bodyType);
        this.body = world.world.createBody(bdef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);
        this.body.setBullet(true);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.setFriction(0.1f);
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

    public void computeForce(float powerMultiplier){
        if(joint != null) {
            Physic elementJoined = ((Physic) joint.getBodyA().getUserData() == this) ?
                                    (Physic) joint.getBodyB().getUserData() : (Physic) joint.getBodyA().getUserData();

            float x = (this.getPosX() - elementJoined.getPosX() );
            float y = (this.getPosY() - elementJoined.getPosY() );

            float ratio = Math.abs(x/y);
            if( ratio < 1.0f ){
                x = x * ratio * powerMultiplier;
                y = y * powerMultiplier;
            } else {
                x = x * powerMultiplier;
                y = y * (1/ratio) * powerMultiplier;
            }
            force.setX( x );
            force.setY( y );

        }else{
            Log.e("RUOTA", "stai tentando di far ruotare paff anche se non è agganciato ad una bolla");
        }
    }

    public void computeForceDirection(float value){
        if(value > 0)
            force.rotate(-90);
        else if(value < 0)
            force.rotate(90);
    }

    public  void applyForce(){
        this.body.applyForce(force,this.body.getPosition(),false);
    }

    public void nullifyResidualVelocity(){
        this.body.setAngularVelocity(0);
        this.body.setLinearVelocity(nullForce);
    }

    public void  nullifyResidualLinearVelocity(){
        this.body.setLinearVelocity(nullForce);
    }

    public  void applyExtraBrakingForce(){
        //this.body.setLinearVelocity(this.body.getAngularVelocity()/20.0f);
        //this.body.setAngularVelocity(this.body.getAngularVelocity()/20.0f);
        force.setX(force.getX());
        force.setY(force.getY());
        this.body.applyForce(force,this.body.getPosition(),false);
    }

    public void breakJoint(){
        world.world.destroyJoint(joint);
        joint = null;
    }

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

    public void fallSmoothly(){
        this.perlinSeed += 0.007;
        fallingMovement.setX(this.oldPosX + ImprovedNoise.map((float)ImprovedNoise.noise(this.perlinSeed,0,0),(float)-Math.sqrt(0.25),(float)Math.sqrt(0.25),-1f,1f));
        fallingMovement.setY(this.getPosY() + 0.05f);
        this.body.setTransform(fallingMovement,0);
    }
}
