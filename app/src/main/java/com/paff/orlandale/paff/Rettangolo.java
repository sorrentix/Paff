package com.paff.orlandale.paff;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;

/**
 * Created by sorrentix on 15/11/2017.
 */

public class Rettangolo {

    Body body;
    float w,h;

    public Rettangolo(PhysicWorld physicWorld, Vec2 position, float w, float h) {

        BodyDef bdef = new BodyDef();
        bdef.setPosition(position);
        bdef.setType(BodyType.staticBody);
        this.body = physicWorld.world.createBody(bdef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(w/2, h/2); // last is rotation angle
        //body.createFixture(polygonShape, 0); // no density needed
        //polygonShape.setAsBox(w,h);
        this.w = w;
        this.h = h;

/*        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(polygonShape);
        fixtureDef.setDensity(5.0f);
*/
        this.body.createFixture(polygonShape,0);

        bdef.delete();
        polygonShape.delete();
        //fixtureDef.delete();

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

    public Vec2 getCenter(){
        return this.getBody().getPosition();
    }

    public float getW(){
        return w;
    }

    public float getH(){
        return h;
    }


}
