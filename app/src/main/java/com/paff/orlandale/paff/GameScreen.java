package com.paff.orlandale.paff;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.google.fpl.liquidfun.Vec2;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class GameScreen extends Screen {

    Graphics graphics;
    Audio audio;
    AnimationPool animationPool;
    Settings settings;
    PhysicWorld physicWorld;

    Bubble bubble;

    public GameScreen(Game game) {
        super(game);
        graphics= game.getGraphics();
        audio = game.getAudio();
        animationPool = game.getAnimationPool();
        settings = game.getSettings();
        physicWorld = game.getPhysicWorld();

        //CORPO
        bubble = new Bubble(physicWorld, new Vec2(0,0),2);
    }

    @Override
    public void update(float deltaTime) {
        physicWorld.update();

    }

    @Override
    public void present(float deltaTime) {
        graphics.drawPixmap(Assets.menu_background, 0, 0);

        graphics.drawCircle(PhysicToPixel.X(bubble.getX()),
                            PhysicToPixel.Y(bubble.getY()),
                            PhysicToPixel.XLength(bubble.getRadius()),
                            0x3498db,255);

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
