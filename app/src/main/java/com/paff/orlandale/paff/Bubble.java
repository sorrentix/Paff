package com.paff.orlandale.paff;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class Bubble extends GameObject{

    private static final String TAG = "Bubble";

    float radius;
    int color;

    Graphics graphics;

    public Bubble(Game game, Vec2 position, float radius, float density, BodyType bodyType, int color) {
        super(game);

        BodyDef bdef = new BodyDef();
        bdef.setPosition(position);
        bdef.setType(bodyType);
        this.body = game.getPhysicWorld().world.createBody(bdef);
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

        graphics = game.getGraphics();

        this.color = color;
    }

    public float getRadius(){
        return radius;
    }

    @Override
    public void draw() {
        graphics.drawCircle(PhysicToPixel.X(this.getX()),
                            PhysicToPixel.Y(this.getY()),
                            PhysicToPixel.XLength(this.getRadius()),
                            color, 255);

    }
}
