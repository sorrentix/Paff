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

    GameObject paff;
    GameObject playBtn;
    GameObject exitBtn;
    GameObject rematchBtn;
    GameObject score;
    GameObject highScore;
    List<GameObject> bubbles;
    GameObject []backgrounds = new GameObject[3];

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

        for (int k = 0; k < backgrounds.length; k++ ) {
            backgrounds[k] = setSimpleImage(new Position(0,-k * GlobalConstants.FRAME_BUFFER_HEIGHT), Assets.menu_background);
        }

        playBtn     = setButton(new Position(60, 760), Assets.btn_play, Assets.bubblexplosion, i);
        rematchBtn  = setButton(new Position(60, 760), Assets.btn_play, Assets.bubblexplosion, i);
        exitBtn     = setButton(new Position(640, 960), Assets.btn_settings, Assets.bubblexplosion, i);

        score = setText(new Position(60,60),Assets.score, Assets.bubblexplosion,new Text("0"));
        highScore = setText(new Position(GlobalConstants.FRAME_BUFFER_WIDTH-55,60),Assets.score, Assets.bubblexplosion,new Text(""+Settings.highscore));

    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        switch (physicWorld.getGameState()){
            case PAUSED:
                for (int i = 0; i < touchEvents.size(); ++i) {
                    Input.TouchEvent event = touchEvents.get(i);
                    if (event.type == Input.TouchEvent.TOUCH_UP) {
                        if (playBtn.evtManager.inBounds(event))
                            physicWorld.gameState = physicWorld.previousState;
                        else if (exitBtn.evtManager.inBounds(event))
                            game.setScreen(new GameMenuScreen(game));
                    }
                }
                break;
            case GAME_OVER:
                if(Integer.parseInt(score.text.toWrite)+physicWorld.scoreToAdd > Settings.highscore){
                    Settings.newHighscore((int) (Integer.parseInt(score.text.toWrite)+physicWorld.scoreToAdd)+1);
                }
                for (int i = 0; i < touchEvents.size(); ++i) {
                    Input.TouchEvent event = touchEvents.get(i);
                    if (event.type == Input.TouchEvent.TOUCH_UP) {
                        if (rematchBtn.evtManager.inBounds(event))
                            game.setScreen(new GameScreen(game));
                        else if (exitBtn.evtManager.inBounds(event))
                            game.setScreen(new GameMenuScreen(game));
                    }
                }
                break;
            default:
                physicWorld.update();
                for (int i = 0; i < touchEvents.size(); ++i) {
                    Input.TouchEvent event = touchEvents.get(i);
                    if (event.type == Input.TouchEvent.TOUCH_UP) {
                        if (physicWorld.getGameState() == GameState.ROTATE)
                            physicWorld.gameState = GameState.SHOT;
                    }
                }
                Camera.computeVerticalMovement(paff);
                Camera.moveCameraVertically(paff);
                Camera.moveCameraVertically(bubbles);
                Camera.moveCameraVerticallyForEndlessBackground(backgrounds);
                break;

        }

    }

    @Override
    public void present(float deltaTime) {


        for (int i = 0; i < backgrounds.length; i++ ) {
          graphics.drawGameObject(backgrounds[i]);
        }

        for (int i = 0; i < bubbles.size(); i++) {
            if ((bubbles.get(i).physic.expirationTime - bubbles.get(i).physic.elapsedTime) < 1)
                 ((PaffGraphics) graphics).drawBubble(bubbles.get(i), GlobalConstants.Colors.RED_DARK, GlobalConstants.ALPHA);
            else
                ((PaffGraphics) graphics).drawBubble(bubbles.get(i), GlobalConstants.Colors.BLUE, GlobalConstants.ALPHA);

        }

        ((PaffGraphics) graphics).drawBubble(paff, GlobalConstants.Colors.RED, GlobalConstants.ALPHA);

        if(physicWorld.scoreToAdd > 0){
            physicWorld.scoreToAdd--;
           score.text.toWrite=""+(Integer.parseInt(score.text.toWrite)+1);
        }
        ((PaffGraphics) graphics).drawText(score, GlobalConstants.Colors.GREY);
        if(Settings.highscore >= (Integer.parseInt(score.text.toWrite))) {
            ((PaffGraphics) graphics).drawText(highScore, GlobalConstants.Colors.GREY);
        }
        else {
            ((PaffGraphics) graphics).drawText(highScore, GlobalConstants.Colors.RED);
        }

        switch (physicWorld.getGameState()) {
            case PAUSED :
                ((PaffGraphics) graphics).drawFilter(GlobalConstants.Colors.GREY);
                graphics.drawGameObject(playBtn);
                graphics.drawGameObject(exitBtn);
                break;
            case GAME_OVER :
                ((PaffGraphics) graphics).drawFilter(GlobalConstants.Colors.GREY);
                graphics.drawGameObject(rematchBtn);
                graphics.drawGameObject(exitBtn);
                break;
            default:
                break;
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
