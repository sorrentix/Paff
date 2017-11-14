package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import java.util.List;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class GameScreen extends Screen {

    Graphics graphics;
    Audio audio;
    AnimationPool animationPool;
    Settings settings;
    PhysicWorld physicWorld;

    Bubble bubbles[];
    Bubble paff;

    public GameScreen(Game game) {
        super(game);
        graphics= game.getGraphics();
        audio = game.getAudio();
        animationPool = game.getAnimationPool();
        settings = game.getSettings();
        physicWorld = game.getPhysicWorld();

        //CORPO
        bubbles = physicWorld.getBubbles();
        paff = physicWorld.getPaff();
    }

    @Override
    public void update(float deltaTime) {
        physicWorld.update();

        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        for (int i = 0; i < touchEvents.size(); ++i) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                physicWorld.setSpara(true);
            }
        }

    }

    @Override
    public void present(float deltaTime) {
        graphics.drawPixmap(Assets.menu_background, 0, 0);

     /*   for(int i = 0; i<bubbles.size(); i++) {
            graphics.drawCircle(PhysicToPixel.X(bubbles.get(i).getX()),
                    PhysicToPixel.Y(bubbles.get(i).getY()),
                    PhysicToPixel.XLength(bubbles.get(i).getRadius()),
                    0x3498db, 255);
        }*/
        graphics.drawCircle(PhysicToPixel.X(bubbles[bubbles.length-1].getX()),
                PhysicToPixel.Y(bubbles[bubbles.length-1].getY()),
                PhysicToPixel.XLength(bubbles[bubbles.length-1].getRadius()),
                0x3498db, 255);

        graphics.drawCircle(PhysicToPixel.X(paff.getX()),PhysicToPixel.Y(paff.getY()),PhysicToPixel.YLength(paff.getRadius()),0xe74c3c,255);
       // graphics.drawLine((int)paff.getX(),(int)paff.getY(),(int)(paff.getX()-physicWorld.accelerometerHandler.getAccelX()*10),(int)(paff.getY()+physicWorld.accelerometerHandler.getAccelY()*10),0xe74c3c);
            graphics.drawLine(0,0,100,100,0xe74c3c
            );
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
