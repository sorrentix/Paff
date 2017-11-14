package com.paff.orlandale.paff;

import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;

/**
 * Created by Yoshi on 14/11/2017.
 */

public class PhysicPosition {
    private Vec2 position;
    BodyDef bdef;
    CircleShape circleShape;

    public PhysicPosition(Vec2 position){
        this.position = position;

    }

    public void setPosition(Vec2 position){
        this.position = position;
        this.bdef.setPosition(position);
        this.circleShape.setPosition(position.getX(),position.getY());
    }

    public BodyDef getBodyDef(){
        return bdef;
    }

    public void setBodyDef(BodyDef bdef){
        this.bdef = bdef;
        this.bdef.setPosition(position);
    }

    public CircleShape getCircleShape(){
        return circleShape;
    }

    public void setCircleShape(CircleShape circleShape){
        this.circleShape = circleShape;
        this.circleShape.setPosition(position.getX(),position.getY());
    }
}
