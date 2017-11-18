package com.paff.orlandale.paff;

import android.util.Log;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.Vec2;

public class Physic implements Component{

    PhysicWorld world;
    public Shape shape;
    public Body body;
    public Joint joint;
    public Vec2 force;

    private float radius;


    public Physic(PhysicWorld wld){
        world = wld;
        force = new Vec2(0,0);
    }



    public void setDistanceJoint(Physic elementB){
        System.out.println("bolle a buon fine");
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.setBodyA(this.body);
        jointDef.setBodyB(elementB.body);
        ((DistanceJointDef) jointDef).setFrequencyHz(0);
        ((DistanceJointDef) jointDef).setDampingRatio(0);
        ((DistanceJointDef) jointDef).setLength(this.getRadius()+elementB.getRadius());
        System.out.println("distance jointdef:"+jointDef);
        joint = world.world.createJoint(jointDef);
        System.out.println("joint a buon fine4");
        System.out.println("distance jointdef:"+jointDef);

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

        System.out.println("physic fixture id: "+this);
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
            Physic other = ((Physic) joint.getBodyA().getUserData() == this) ?
                    (Physic) joint.getBodyB().getUserData() : (Physic) joint.getBodyA().getUserData();

            force.setX((this.getPosX() - other.getPosX())* powerMultiplier);
            force.setY((this.getPosY() - other.getPosY() )* 7 * powerMultiplier);
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
        this.body.applyForceToCenter(force, false);
    }
    public void nullifyResidualVelocity(){
        this.body.setAngularVelocity(0);
        this.body.setLinearVelocity(new Vec2(this.getPosX(), this.getPosY()));
    }

    public  void applyForceWithInertia(){
        float inertia = this.body.getInertia();
        force.setX(force.getX()+inertia);
        force.setY(force.getY()+inertia);
        this.body.applyForceToCenter(force, false);
    }

    public void breakJoint(){
        world.world.destroyJoint(joint);
        joint = null;
    }
}
