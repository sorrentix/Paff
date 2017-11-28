package com.paff.orlandale.paff;

import android.util.Log;

import com.badlogic.androidgames.framework.*;
import com.badlogic.androidgames.framework.impl.AndroidGame;

/**
 * Activity principale del gioco. Si occupa unicamente di gestire gli screen in base allo stato dell'applicazione (pause, resume, stop)
 */

public class PaffGame extends AndroidGame {

    public static final String TAG = "PaffGame";

    /**
     * Restituisce lo screen iniziale.
     * @return lo splashscreen
     */
    @Override
    public Screen getStartScreen() {
        return new SplashScreen(this);
    }

    /**
     * Quando l'applicazione viene messa in pausa, se si è nella schermata di gioco quest'ultimo viene messo in pausa.
     * Inoltre, a prescindere dallo screen in cui ci si trova, vengono interrotti tutti i suoni in esecuzione.
     */
    @Override
    public void onPause(){
        super.onPause();

        Screen currScreen = super.getCurrentScreen();
        if(currScreen instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) currScreen;
            /**
             * Lo stato di pausa del gioco viene impostato se siamo nel GameScreen e se lo stato di gioco è diverso da PAUSA o GAME OVER
             */
            if(gameScreen.physicWorld.getGameState()!=GameState.PAUSED && gameScreen.physicWorld.getGameState()!=GameState.GAME_OVER ) {
                gameScreen.physicWorld.previousState = gameScreen.physicWorld.getGameState();
                gameScreen.physicWorld.pausedTime = System.nanoTime();
                gameScreen.physicWorld.gameState = GameState.PAUSED;
            }
        }
        if (Assets.gamesoundtheme!=null)
            super.getAudio().pauseAll();

    }

    /**
     * Riattiva i suoni interrotti a meno di particolari condizioni
     */
    @Override
    public void onResume(){
        super.onResume();

        Screen currScreen = super.getCurrentScreen();
        if(currScreen instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) currScreen;
            /**
             * Se siamo in GameScreen e lo stato di gioco al momento della chiamata al meotodo onPause() era di SETUP, non ripristinare i suoni
             */
            if (Settings.sounds && gameScreen.physicWorld.previousState != GameState.SETUP)
                super.getAudio().resumeAll();
        }
        else {
            if (!Assets.areReady())
                getStartScreen();
            else if (Settings.music)
                super.getAudio().resumeAll();
        }
    }

    /**
     * Alla chiusura dell'applicazione, se necessario viene salvato l'highscore
     */
    @Override
    public void onStop() {

        Screen currScreen = super.getCurrentScreen();
        if (currScreen instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) currScreen;
            if (Integer.parseInt(gameScreen.score.text.toWrite) + gameScreen.physicWorld.scoreToAdd > Settings.highscore) {
                Settings.newHighscore((int) (Integer.parseInt(gameScreen.score.text.toWrite) + gameScreen.physicWorld.scoreToAdd) + 1);
            }

        }
        super.onStop();
    }

    /**
     * Gestisce la pressione del tasto indietro per renderne coerente il comportamento rispetto allo screen in cui ci si trova
     */
    @Override
    public void onBackPressed() {
        Screen currScreen = getCurrentScreen();

        if (currScreen instanceof GameMenuScreen)
            super.onBackPressed();


        else if(currScreen instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) currScreen;
            /**
             * Se il tasto back viene cliccato nella schermata di Game Over si torna al Menu di gioco
             */
            if(gameScreen.physicWorld.getGameState() == GameState.GAME_OVER)
                    setScreen(new GameMenuScreen(this));
            /**
             * Se il tasto back viene cliccato nel menu di pausa si torna al gioco se lo stato precedente non era di SETUP.
             * In questo caso si rilancia il gamescreen per re-inizializzare il gioco
             */
           else if(gameScreen.physicWorld.getGameState() == GameState.PAUSED) {
                if (gameScreen.physicWorld.previousState == GameState.SETUP)
                    setScreen(new GameScreen(this));

                else
                    gameScreen.physicWorld.gameState = gameScreen.physicWorld.previousState;
            }
            /**
             * Se il tasto back viene cliccato durante il gioco si entra nello stato di pausa, salvando lo stato corrente di gioco al momento della pressione
             * Inoltre viene conteggiato il tempo trascorso in pausa per effettuare calcoli corretti sulla vita delle bolle
             */
            else {
                if (gameScreen.physicWorld.getGameState() == GameState.SETUP)
                    Assets.countdown.stop();
                gameScreen.physicWorld.previousState = gameScreen.physicWorld.getGameState();
                gameScreen.physicWorld.pausedTime = System.nanoTime();
                gameScreen.physicWorld.gameState = GameState.PAUSED;
            }
        }
        /**
         * Se il tasto back viene cliccato nello screen dei settings o delle info si fa ritorno al menu di gioco
         */
        else
            setScreen(new GameMenuScreen(this));

    }

}
