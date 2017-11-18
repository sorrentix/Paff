package com.paff.orlandale.paff;

import android.util.Log;

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

    private static final String TAG = "GameScreen";

    Graphics graphics;
    Audio audio;
    AnimationPool animationPool;
    Settings settings;
    PhysicWorld physicWorld;

    List<GameObject> gameObjects;
    GameObject paff;

    public GameScreen(Game game) {
        super(game);
        graphics= game.getGraphics();
        audio = game.getAudio();
        animationPool = game.getAnimationPool();
        settings = game.getSettings();
        physicWorld = game.getPhysicWorld();

        gameObjects = physicWorld.getGameObjects();
        paff = physicWorld.getPaff();
    }

    @Override
    public void update(float deltaTime) {
        physicWorld.update();

        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        for (int i = 0; i < touchEvents.size(); ++i) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (physicWorld.getGameState() == GameState.ROTATE)
                    physicWorld.setSpara(GameState.SHOT);
            }
        }

    }

    @Override
    public void present(float deltaTime) {
        graphics.drawPixmap(Assets.menu_background, 0, 0);

        //Oggetti del mondo
        for (int i = 0; i < gameObjects.size(); i++){
            gameObjects.get(i).draw();
        }
        paff.draw();
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
