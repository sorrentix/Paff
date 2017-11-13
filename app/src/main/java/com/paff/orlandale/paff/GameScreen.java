package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
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

    List<Bubble> bubbles;

    public GameScreen(Game game) {
        super(game);
        graphics= game.getGraphics();
        audio = game.getAudio();
        animationPool = game.getAnimationPool();
        settings = game.getSettings();
        physicWorld = game.getPhysicWorld();

        //CORPO
        bubbles = physicWorld.getBubbles();
    }

    @Override
    public void update(float deltaTime) {
        physicWorld.update();

    }

    @Override
    public void present(float deltaTime) {
        graphics.drawPixmap(Assets.menu_background, 0, 0);

        for(int i = 0; i<bubbles.size(); i++) {
            graphics.drawCircle(PhysicToPixel.X(bubbles.get(i).getX()),
                    PhysicToPixel.Y(bubbles.get(i).getY()),
                    PhysicToPixel.XLength(bubbles.get(i).getRadius()),
                    0x3498db, 255);
        }
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
