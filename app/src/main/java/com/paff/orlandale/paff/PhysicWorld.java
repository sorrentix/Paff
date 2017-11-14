package com.paff.orlandale.paff;

import android.graphics.RectF;

import com.badlogic.androidgames.framework.Input;
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

    private static final float X_MIN = -10.0f;
    private static final float X_MAX = 10.0f;
    private static final float Y_MIN = -15.0f;
    private static final float Y_MAX = 15.0f;

    private Vec2 gravity;
    private float framebufferWidth;
    private float framebufferHeight;

    List<Bubble> bubbles = new ArrayList<Bubble>();
    private static final int POS_ALLOWED = 17;
    Random generator;

    Vec2 []bubbleUsableStartingPositions = new Vec2[POS_ALLOWED];
    boolean [] isUsablePosition = new boolean[POS_ALLOWED];


    public PhysicWorld(Box physicalSize, Box screenSize, Vec2 gravity, float framebufferWidth,float framebufferHeight){
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.gravity = gravity;
        this.framebufferWidth = framebufferWidth;
        this.framebufferHeight = framebufferHeight;

        this.world = new World(gravity.getX(),gravity.getY());

        this.setStartingBubblesPositions();
        generator = new Random();

        for (int i = 0; i < isUsablePosition.length; i++){
            isUsablePosition[i]=true;
        }
        for (int i = 0; i < 5; i++){
            int posIndex = generator.nextInt(bubbleUsableStartingPositions.length-1);
            while(isUsablePosition[posIndex] == false){
                System.out.println("mammt2");

                posIndex++;
                if (posIndex >= POS_ALLOWED)
                    posIndex = 0;
            }
            Vec2 pos = bubbleUsableStartingPositions[posIndex];
            isUsablePosition[posIndex] = false;

            bubbles.add(new Bubble(this, pos ,(generator.nextFloat()%2)+2, posIndex));
        }

    }

    public synchronized void update(){
        world.step(TIME_STEP,VELOCITY_ITERATIONS,POSITION_ITERATIONS,PARTICLE_ITERATIONS);
        checkBubblesPosition();

/*        for (int i = 0; i < 5; i++){
            if (bubbles.get(i).getY()+bubbles.get(i).getRadius()<=physicalSize.ymin)
                bubbles.get(i).getBody().
        }*/
    }

    private void checkBubblesPosition(){
        for( Bubble bubble: bubbles){
            if ( bubble.body.getPositionY() + bubble.getRadius() < Y_MIN ){
                isUsablePosition[bubble.id]=true;

                int posIndex = generator.nextInt(bubbleUsableStartingPositions.length-1);
                while(isUsablePosition[posIndex] == false){
                    System.out.println("mammt1");
                    posIndex++;
                    if (posIndex >= POS_ALLOWED)
                        posIndex = 0;
                }

                Vec2 pos = bubbleUsableStartingPositions[posIndex];
                isUsablePosition[posIndex] = false;

                bubble.id = posIndex;
                bubble.body.setTransform(bubbleUsableStartingPositions[posIndex].getX(),bubbleUsableStartingPositions[posIndex].getY(),0);

            }
        }
    }

    private void setStartingBubblesPositions(){

        Random generator = new Random();
        float x,y;

        y = Y_MAX + 4.0f;
        for( int i = -8; i < 9; i++){
            x = generator.nextFloat()/2.0f + i;
            bubbleUsableStartingPositions[i+8]= new Vec2(x,y);
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

    public List<Bubble> getBubbles(){
        return bubbles;
    }
}
