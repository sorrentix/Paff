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
    public PhysicPosition pos;
    public int id;

    public Bubble(PhysicWorld physicWorld, Vec2 position, float radius, int id) {
        this.id = id;
        pos = new PhysicPosition(position);

        BodyDef bdef = new BodyDef();
        pos.setBodyDef(bdef);
        bdef.setType(BodyType.dynamicBody);
        this.body = physicWorld.world.createBody(bdef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);

        CircleShape circleshape = new CircleShape();
        pos.setCircleShape(circleshape);
        circleshape.setRadius(radius);
        this.radius = radius;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(circleshape);
        fixtureDef.setFriction(0.1f);
        fixtureDef.setDensity(0.5f);

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


}
