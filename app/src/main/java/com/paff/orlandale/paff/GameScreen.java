package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AccelerometerHandler;

import java.util.List;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class GameScreen extends Screen {

    Graphics graphics;
    Audio audio;
    Input input;
    AnimationPool animationPool;
    PhysicWorld physicWorld;
    AccelerometerHandler accelerometerHandler;

    GameObject paff;
    GameObject []bubbles;
    GameObject background;

    public GameScreen(Game game) {
        super(game);
        graphics= game.getGraphics();
        audio = game.getAudio();
        input = game.getInput();
        animationPool = game.getAnimationPool();

        physicWorld = new PhysicWorld( PhysicToPixel.physicalSize,new Box(0,0,GlobalConstants.FRAME_BUFFER_WIDTH,GlobalConstants.FRAME_BUFFER_HEIGHT),
                                       GlobalConstants.GRAVITY,
                                       input);
        paff       = physicWorld.paff;
        bubbles    = physicWorld.bubbles;
        background = setSimpleImage(new Position(0, 0), Assets.menu_background);
    }

    @Override
    public void update(float deltaTime) {
        physicWorld.update();

        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        for (int i = 0; i < touchEvents.size(); ++i) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (physicWorld.getGameState() == GameState.ROTATE)
                    physicWorld.gameState = GameState.SHOT;
            }
        }

    }

    @Override
    public void present(float deltaTime) {
        graphics.drawGameObject(background);

        for (GameObject bubble : bubbles) {
            ((PaffGraphics)graphics).drawBubble(bubble,GlobalConstants.Colors.BLUE,GlobalConstants.ALPHA);
        }
        ((PaffGraphics)graphics).drawBubble(paff,GlobalConstants.Colors.RED,GlobalConstants.ALPHA);

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
