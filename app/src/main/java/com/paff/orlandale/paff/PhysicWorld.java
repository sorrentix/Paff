package com.paff.orlandale.paff;

import android.graphics.RectF;

import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class PhysicWorld {
    World world;

    //List<GameObject> gameObjects;

    final Box physicalSize, screenSize;

    private static final float TIME_STEP = 1/60f; //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    private Vec2 gravity;
    private float framebufferWidth;
    private float framebufferHeight;


    public PhysicWorld(Box physicalSize, Box screenSize, Vec2 gravity, float framebufferWidth,float framebufferHeight){
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.gravity = gravity;
        this.framebufferWidth = framebufferWidth;
        this.framebufferHeight = framebufferHeight;

        this.world = new World(gravity.getX(),gravity.getY());
    }

    public synchronized void update(){
        world.step(TIME_STEP,VELOCITY_ITERATIONS,POSITION_ITERATIONS,PARTICLE_ITERATIONS);
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

}
