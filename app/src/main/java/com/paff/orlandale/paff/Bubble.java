package com.paff.orlandale.paff;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class Bubble{

    Body body;
    float radius;

    public Bubble(PhysicWorld physicWorld, Vec2 position, float radius,float density,BodyType bodyType) {

        BodyDef bdef = new BodyDef();
        bdef.setPosition(position);
        bdef.setType(bodyType);
        this.body = physicWorld.world.createBody(bdef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);
        this.body.setBullet(true);

        CircleShape circleshape = new CircleShape();
        circleshape.setPosition(0,0);
        circleshape.setRadius(radius);
        this.radius = radius;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(circleshape);
        fixtureDef.setFriction(0.1f);
        fixtureDef.setDensity(density);

        this.body.createFixture(fixtureDef);

        fixtureDef.delete();
        bdef.delete();
        circleshape.delete();

    }

    public Body getBody(){
        return body;
    }

    public float getX(){
        return this.getBody().getPositionX();
    }

    public float getY(){
        return this.getBody().getPositionY();
    }

    public float getRadius(){
        return radius;
    }

    public Vec2 getCenter(){
        return this.getBody().getPosition();
    }


}
