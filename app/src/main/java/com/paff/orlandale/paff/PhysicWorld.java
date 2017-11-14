package com.paff.orlandale.paff;

import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.impl.AccelerometerHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.RevoluteJointDef;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class PhysicWorld{
    World world;

    final Box physicalSize, screenSize;

    private static final float TIME_STEP = 1/60f; //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    private Vec2 gravity;
    private float framebufferWidth;
    private float framebufferHeight;

    Bubble bubbles[] = new Bubble[5];
    Bubble paff;
    boolean spara=false;
    Joint revoluteJoint;
    AccelerometerHandler accelerometerHandler;

    public PhysicWorld(Box physicalSize, Box screenSize, Vec2 gravity, AccelerometerHandler accelerometerHandler){
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.gravity = gravity;
        this.framebufferWidth = screenSize.xmax;
        this.framebufferHeight = screenSize.ymax;
        this.accelerometerHandler = accelerometerHandler;

        this.world = new World(gravity.getX(),gravity.getY());
        Random generator = new Random();

        for (int i = 0; i < 5; i++){
            bubbles[i]=new Bubble(this,new Vec2(((float)generator.nextInt(160)/10.0f)-8.0f,((float)generator.nextInt(150)/10.0f)+15.0f),(generator.nextFloat()%4)+1);
        }// int randomNum = rand.nextInt((max - min) + 1) + min;
        bubbles[bubbles.length-1]=(new Bubble(this,new Vec2(0,0),2));
        paff= new Bubble(this,new Vec2(3,0),1);


        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.setBodyA(paff.getBody());
        revoluteJointDef.setBodyB(bubbles[bubbles.length-1].getBody());
        revoluteJointDef.setLocalAnchorA(paff.getX(),paff.getY());
        revoluteJointDef.setLocalAnchorB(bubbles[bubbles.length-1].getX(),bubbles[bubbles.length-1].getY());
        revoluteJoint =  world.createJoint(revoluteJointDef);

        revoluteJointDef.delete();
    }

    public synchronized void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

/*        for (int i = 0; i < 5; i++){
            if (bubbles.get(i).getY()+bubbles.get(i).getRadius()<=physicalSize.ymin)
                bubbles.get(i).getBody().
        }*/

        // paff.getBody().applyForce(new Vec2(0,-accelerometerHandler.getAccelX()),new Vec2(paff.getX(),paff.getY()),false);

        if (spara) {
            //CALCOLA VETTORE
            //RIMUOVI JOINT
            //APPLICA LA FORZA NELLA DIREZIONE DEL VETTORE (SPARA)
            Vec2 vec =new Vec2(paff.getX()-bubbles[bubbles.length-1].getX(),paff.getY()-bubbles[bubbles.length-1].getY());
           world.destroyJoint(revoluteJoint);
           paff.getBody().applyLinearImpulse(vec,new Vec2(paff.getX(),paff.getY()),false);
           setSpara(false);

        } else {
            float torque = paff.getBody().getAngularVelocity();
            Log.e("TORQUE", "" + torque);
            if (Math.abs(torque) > 3)
                paff.getBody().applyTorque(-3 * torque, false);


            if (((int) accelerometerHandler.getAccelX()) > 0) // FLAG = TRUE

                paff.getBody().applyTorque(13, false);

            else if ((((int) accelerometerHandler.getAccelX()) < 0)) // FLAG = FALSE
                paff.getBody().applyTorque(-13, false);
            else {
                if (torque != 0) {
                    torque = paff.getBody().getAngularVelocity();
                    paff.getBody().applyTorque(-torque, false);
                } else
                    paff.getBody().applyTorque(0, false);


            }
            Log.e("DATI ACCELEROMETRO : ", "X=" + accelerometerHandler.getAccelX() + "\n Y=" + accelerometerHandler.getAccelY());
        }
    }
    public synchronized void setGravity(float x, float y)
    {
        world.setGravity(x, y);
    }

    @Override
    public void finalize()
    {
        world.delete();
    }

    public Bubble[] getBubbles(){
        return bubbles;
    }

    public Bubble getPaff() { return paff; }

    public void setSpara(boolean flag) { spara=flag;}
}
