package com.paff.orlandale.paff;

import android.graphics.RectF;

import com.badlogic.androidgames.framework.Pool;
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

    List<Bubble> bubbles;

    public PhysicWorld(Box physicalSize, Box screenSize, Vec2 gravity, float framebufferWidth,float framebufferHeight){
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.gravity = gravity;
        this.framebufferWidth = framebufferWidth;
        this.framebufferHeight = framebufferHeight;

        this.world = new World(gravity.getX(),gravity.getY());

        Random generator = new Random();

        bubbles = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            bubbles.add(new Bubble(this,new Vec2(((float)generator.nextInt(160)/10.0f)-8.0f,((float)generator.nextInt(150)/10.0f)+15.0f),(generator.nextFloat()%4)+1));
        }// int randomNum = rand.nextInt((max - min) + 1) + min;

    }

    public synchronized void update(){
        world.step(TIME_STEP,VELOCITY_ITERATIONS,POSITION_ITERATIONS,PARTICLE_ITERATIONS);

/*        for (int i = 0; i < 5; i++){
            if (bubbles.get(i).getY()+bubbles.get(i).getRadius()<=physicalSize.ymin)
                bubbles.get(i).getBody().
        }*/
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

    public List<Bubble> getBubbles(){
        return bubbles;
    }
}
