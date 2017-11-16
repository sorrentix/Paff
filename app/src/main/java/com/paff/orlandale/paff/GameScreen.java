package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.fpl.liquidfun.Vec2;

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
    Bubble paff, provaBubble;

    public GameScreen(Game game) {
        super(game);
        graphics= game.getGraphics();
        audio = game.getAudio();
        animationPool = game.getAnimationPool();
        settings = game.getSettings();
        physicWorld = game.getPhysicWorld();

        //CORPO
        //bubbles = physicWorld.getBubbles();
        paff = physicWorld.getPaff();
        provaBubble = physicWorld.provaBubble;

    }

    @Override
    public void update(float deltaTime) {
        physicWorld.update();

        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        for (int i = 0; i < touchEvents.size(); ++i) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (physicWorld.getGameState() == PhysicWorld.GameState.Ruota)
                    physicWorld.setSpara(PhysicWorld.GameState.Spara);
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
        graphics.drawCircle(PhysicToPixel.X(provaBubble.getX()),
                PhysicToPixel.Y(provaBubble.getY()),
                PhysicToPixel.XLength(provaBubble.getRadius()),
                0x3498db, 255);

        graphics.drawCircle(PhysicToPixel.X(paff.getX()),PhysicToPixel.Y(paff.getY()),PhysicToPixel.XLength(paff.getRadius()),0xe74c3c,255);

        graphics.drawLine(PhysicToPixel.X(provaBubble.getX()),
                          PhysicToPixel.Y(provaBubble.getY()),
                          PhysicToPixel.X(paff.getX()),
                          PhysicToPixel.Y(paff.getY()),0xffffff);
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
