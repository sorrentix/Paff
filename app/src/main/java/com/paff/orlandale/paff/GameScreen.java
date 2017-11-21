package com.paff.orlandale.paff;

import android.util.Log;

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
    GameObject playBtn;
    GameObject exitBtn;
    GameObject rematchBtn;
    List<GameObject> bubbles;
    GameObject background;

    public GameScreen(Game game) {
        super(game);
        graphics= game.getGraphics();
        audio = game.getAudio();
        input = game.getInput();
        animationPool = game.getAnimationPool();
        Input i = game.getInput();
        physicWorld = new PhysicWorld( PhysicToPixel.physicalSize, input);
        paff       = physicWorld.paff;
        bubbles    = physicWorld.activeBubbles;
        playBtn     = setButton(new Position(60, 760), Assets.btn_play, Assets.bubblexplosion, i);
        rematchBtn     = setButton(new Position(60, 760), Assets.btn_play, Assets.bubblexplosion, i);
        exitBtn = setButton(new Position(640, 960), Assets.btn_settings, Assets.bubblexplosion, i);
        background = setSimpleImage(new Position(0, 0), Assets.menu_background);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (physicWorld.getGameState() != GameState.PAUSED && physicWorld.getGameState() != GameState.GAME_OVER) {
            physicWorld.update();
            for (int i = 0; i < touchEvents.size(); ++i) {
                Input.TouchEvent event = touchEvents.get(i);
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    if (physicWorld.getGameState() == GameState.ROTATE)
                        physicWorld.gameState = GameState.SHOT;
                }
            }
        } else if (physicWorld.getGameState() == GameState.PAUSED) {
            for (int i = 0; i < touchEvents.size(); ++i) {
                Input.TouchEvent event = touchEvents.get(i);
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    if (playBtn.evtManager.inBounds(event))
                        physicWorld.gameState = physicWorld.previousState;
                    else if (exitBtn.evtManager.inBounds(event))
                        game.setScreen(new GameMenuScreen(game));
                }
            }
        } else {
            for (int i = 0; i < touchEvents.size(); ++i) {
                Input.TouchEvent event = touchEvents.get(i);
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    if (rematchBtn.evtManager.inBounds(event))
                        game.setScreen(new GameScreen(game));
                    else if (exitBtn.evtManager.inBounds(event))
                        game.setScreen(new GameMenuScreen(game));
                }
            }
        }
    }




    @Override
    public void present(float deltaTime) {


            graphics.drawGameObject(background);

            //for (GameObject bubble : bubbles) {
            for (int i = 0; i < bubbles.size(); i++) {
                ((PaffGraphics) graphics).drawBubble(bubbles.get(i), GlobalConstants.Colors.BLUE, GlobalConstants.ALPHA);
            }
            ((PaffGraphics) graphics).drawBubble(paff, GlobalConstants.Colors.RED, GlobalConstants.ALPHA);

        if(physicWorld.getGameState()==GameState.PAUSED) {
            graphics.drawGameObject(playBtn);
            graphics.drawGameObject(exitBtn);
        }
        if(physicWorld.getGameState()==GameState.GAME_OVER) {
            graphics.drawGameObject(rematchBtn);
            graphics.drawGameObject(exitBtn);
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
