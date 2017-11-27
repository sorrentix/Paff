package com.paff.orlandale.paff;

import android.util.Log;

import com.badlogic.androidgames.framework.*;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class PaffGame extends AndroidGame {

    public static final String TAG = "PaffGame";

    @Override
    public Screen getStartScreen() {
        return new SplashScreen(this);
    }

    @Override
    public void onPause(){
        super.onPause();

        Screen currScreen = super.getCurrentScreen();
        if(currScreen instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) currScreen;
            if(gameScreen.physicWorld.getGameState()!=GameState.PAUSED && gameScreen.physicWorld.getGameState()!=GameState.GAME_OVER ) {
                gameScreen.physicWorld.previousState = gameScreen.physicWorld.getGameState();
                gameScreen.physicWorld.pausedTime = System.nanoTime();
                gameScreen.physicWorld.gameState = GameState.PAUSED;
            }
        }
        if (Assets.gamesoundtheme!=null)
            super.getAudio().pauseAll();

    }

    @Override
    public void onResume(){
        super.onResume();

        if(!Assets.areReady())
            getStartScreen();
        else if(Settings.music)
            super.getAudio().resumeAll();
    }

    @Override
    public void onBackPressed() {
        Screen currScreen = getCurrentScreen();

        if (currScreen instanceof GameMenuScreen)
            super.onBackPressed();

        else if(currScreen instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) currScreen;
            if(gameScreen.physicWorld.getGameState() == GameState.GAME_OVER)
                    setScreen(new GameMenuScreen(this));
            if(gameScreen.physicWorld.getGameState() == GameState.PAUSED)
                gameScreen.physicWorld.gameState=gameScreen.physicWorld.previousState;
            else {
                gameScreen.physicWorld.previousState = gameScreen.physicWorld.getGameState();
                gameScreen.physicWorld.pausedTime = System.nanoTime();
                gameScreen.physicWorld.gameState = GameState.PAUSED;
            }
        }
        else
            setScreen(new GameMenuScreen(this));

    }

}
