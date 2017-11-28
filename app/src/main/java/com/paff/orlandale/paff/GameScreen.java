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

/**
 * Screen principale di gioco.
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
    GameObject finalScore;
    GameObject highScore;
    GameObject gameover;
    GameObject pause;
    List<GameObject> bubbles;
    GameObject []backgrounds = new GameObject[3];


    boolean newhighscorePlayed=false;


    /**
     * Il costruttore è incaricato di creare il mondo fisico e  di creare tutte le entità utili durante il gioco
     * @param game Implementazione dell'interfaccia Game
     */
    public GameScreen(Game game) {
        super(game);

        graphics= game.getGraphics();
        audio = game.getAudio();
        input = game.getInput();
        input.clearTouchEvents();
        animationPool = game.getAnimationPool();
        Input i = game.getInput();
        physicWorld = new PhysicWorld(PhysicToPixel.physicalSize,input);
        paff       = physicWorld.paff;
        bubbles    = physicWorld.activeBubbles;

        for (int k = 0; k < backgrounds.length; k++ ) {
            backgrounds[k] = setSimpleImage(new Position(0,-k * GlobalConstants.FRAME_BUFFER_HEIGHT), Assets.menu_background);
        }

        playBtn     = setButton(new Position(60, 1260), Assets.btn_resume, Assets.bubblexplosion, i);
        rematchBtn  = setButton(new Position(60, 1260), Assets.btn_replay, Assets.bubblexplosion, i);
        exitBtn     = setButton(new Position(640, 1260), Assets.btn_exit, Assets.bubblexplosion, i);
        gameover    = setSimpleImage(new Position(40, 40), Assets.gameover_text);
        pause       = setSimpleImage(new Position(40, 260), Assets.pause_text);

        score = setText(new Position(60,60),Assets.score, Assets.bubblexplosion,new Text("0"));
        finalScore = setText(new Position(100,900),Assets.scoreText, Assets.bubblexplosion,new Text("0"));
        highScore = setText(new Position(GlobalConstants.FRAME_BUFFER_WIDTH-55,60),Assets.highscore, Assets.bubblexplosion,new Text(""+Settings.highscore));

        animationPool.animationToExecute = 1;
        animationPool.getAnimationByID(1).currentImage=0;
      
        Assets.gamemenusoundtheme.setLooping(false);
        Assets.gamemenusoundtheme.pause();
        Assets.countdown.play();

    }

    /**
     * Il metodo viene richiamato dal fastrenderview un numero fissato di volte al secondo.
     * Il metodo osserva gli input di tipo touch e in base allo stato di gioco corrente effettua determinate azioni
     *
     * @param deltaTime il tempo che intercorre tra un frame e l'altro
     */
    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        switch (physicWorld.getGameState()){
            /**
             * Nel caso in cui ci si trovi nello stato di pausa vengono osservati gli eventi associati ai tasti relativi alla schermata di pausa
             */
            case PAUSED:
                for (int i = 0; i < touchEvents.size(); ++i) {
                    Input.TouchEvent event = touchEvents.get(i);
                    if (event.type == Input.TouchEvent.TOUCH_UP) {
                        if (playBtn.evtManager.inBounds(event)) {
                            playBtn.sound.play();
                            /**
                             * Nel caso in cui venga cliccato il tasto resume, e lo stato precedente allo stato di pausa era di SETUP, reinizializza lo screen di gioco
                             */
                            if(physicWorld.previousState == GameState.SETUP)

                                game.setScreen(new GameScreen(game));
                            /**
                             * Se lo stato precedente allo stato di pausa non era di SETUP, questi viene reimpostato come stato corrente di gioco
                             * e le bolle attive vengono aggiornate con il tempo di pausa trascorso
                             */
                            else {
                                physicWorld.pausedTime = System.nanoTime() - physicWorld.pausedTime;
                                for (int j = 0; j < bubbles.size(); j++)
                                    bubbles.get(j).physic.totalPausedTime += physicWorld.pausedTime / 1000000000.0f;
                                physicWorld.totalPausedTime += physicWorld.pausedTime / 1000000000.0f;
                                physicWorld.gameState = physicWorld.previousState;
                            }
                        }
                        /**
                         * Se viene cliccato il tasto di uscita si torna al menu di gioco
                         */
                        else if (exitBtn.evtManager.inBounds(event)) {
                            exitBtn.sound.play();
                            game.setScreen(new GameMenuScreen(game));
                        }
                    }
                }
                break;
            case GAME_OVER:
                /**
                 * Se siamo nella schermata di GAME OVER viene aggiornato l'highscore se necessario
                 */
                if(Integer.parseInt(score.text.toWrite)+physicWorld.scoreToAdd > Settings.highscore){
                    Settings.newHighscore((int) (Integer.parseInt(score.text.toWrite)+physicWorld.scoreToAdd)+1);
                }
                Assets.gamesoundtheme.setLooping(false);
                Assets.gamesoundtheme.pause();
                /**
                 * Nel caso in cui ci si trovi nello stato di game over vengono osservati gli eventi associati ai tasti relativi alla schermata
                 */
                for (int i = 0; i < touchEvents.size(); ++i) {
                    Input.TouchEvent event = touchEvents.get(i);
                    if (event.type == Input.TouchEvent.TOUCH_UP) {
                        /**
                         * Nel caso in cui venga cliccato il tasto rematch si reinizializza la schermata di gioco
                         */
                        if (rematchBtn.evtManager.inBounds(event)) {
                            Assets.gamesoundtheme.setLooping(false);
                            Assets.gamesoundtheme.pause();
                            rematchBtn.sound.play();
                            game.setScreen(new GameScreen(game));
                        }
                        /**
                         * Se viene cliccato il tasto di uscita si torna al menu di gioco
                         */
                        else if (exitBtn.evtManager.inBounds(event)) {
                            exitBtn.sound.play();
                            game.setScreen(new GameMenuScreen(game));
                        }
                    }
                }
                break;
            /**
             * Nello stato di SETUP viene inizializzata ed eseguita l'animazione del countdown al termine della quale si passa allo stato WAITING di gioco
             */
            case SETUP:
                if(animationPool.animationToExecute ==-1) {
                    for(int i=0; i< bubbles.size(); i++)
                        bubbles.get(i).physic.startTime = System.nanoTime();

                    physicWorld.gameState = GameState.WAITING;
                }
                break;
            /**
             * Il caso di default copre tutti gli stati effettivi di gioco. Viene quindi chiamato l'update del mondo fisico e
             * osservato l'evento di tipo touch sullo schermo
             */
            default:
                physicWorld.update();
                for (int i = 0; i < touchEvents.size(); ++i) {
                    Input.TouchEvent event = touchEvents.get(i);
                    if (event.type == Input.TouchEvent.TOUCH_UP) {
                        if (physicWorld.getGameState() == GameState.ROTATE)
                            physicWorld.gameState = GameState.SHOT;
                    }
                }
                /**
                 * Si calcola la posizione della camera in base alla posizione di paff e delle bolle
                 */
                Camera.computeVerticalMovement(paff);
                Camera.moveCameraVertically(paff);
                Camera.moveCameraVertically(bubbles);
                Camera.moveCameraVerticallyForEndlessBackground(backgrounds);
                break;

        }

    }

    /**
     * Il metodo viene richiamato dal fastrenderview un numero fissato di volte al secondo.
     * Il metodo si occupa di effettuare, in base ai vari stati di gioco il rendering delle diverse entità.
     *
     * @param deltaTime il tempo che intercorre tra un frame e l'altro
     */
    @Override
    public void present(float deltaTime) {

        for (int i = 0; i < backgrounds.length; i++ ) {
          graphics.drawGameObject(backgrounds[i]);
        }

        for (int i = 0; i < bubbles.size(); i++) {

            /**
             * Se la vita residua della bolla è minore o uguale ad un secondo, il colore viene impostato a rosso
             */
            if ((bubbles.get(i).physic.expirationTime - bubbles.get(i).physic.elapsedTime) <= 1)
                 ((PaffGraphics) graphics).drawBubble(bubbles.get(i), GlobalConstants.Colors.RED_DARK, GlobalConstants.ALPHA);
            /**
             * Se la vita residua della bolla è minore o uguale a tre secondi, il colore viene impostato a giallo
             */
            else if ((bubbles.get(i).physic.expirationTime - bubbles.get(i).physic.elapsedTime) <= 3)
                ((PaffGraphics) graphics).drawBubble(bubbles.get(i), GlobalConstants.Colors.ORANGE
                        , GlobalConstants.ALPHA);
            /**
             * Di default la bolla viene colorata di blu
             */
            else
                ((PaffGraphics) graphics).drawBubble(bubbles.get(i), GlobalConstants.Colors.BLUE, GlobalConstants.ALPHA);

        }

        ((PaffGraphics) graphics).drawBubble(paff, GlobalConstants.Colors.RED, GlobalConstants.ALPHA);

        /**
         * Si aggiunge allo score un punto alla volta attingendo da un buffer di punti totalizzati
         */
        if(physicWorld.scoreToAdd > 0){
            physicWorld.scoreToAdd--;
           score.text.toWrite=""+(Integer.parseInt(score.text.toWrite)+1);
        }
        ((PaffGraphics) graphics).drawText(score, GlobalConstants.Colors.GREY,80);
        /**
         * Se l'highscore non è stato battuto viene inserito colorato di grigio
         */
        if(Settings.highscore >= (Integer.parseInt(score.text.toWrite))) {
            ((PaffGraphics) graphics).drawText(highScore, GlobalConstants.Colors.GREY,80);
        }
        /**
         * Se l'highscore è stato battuto viene inserito colorato di verde e viene riprodotto un suono
         */
        else {
            if(!physicWorld.newhighscore) {
                Assets.newhighscore.play();
                physicWorld.newhighscore = true;
            }
            highScore.text.toWrite=score.text.toWrite;
            ((PaffGraphics) graphics).drawText(highScore, GlobalConstants.Colors.GREEN,80);
        }
        /**
         * In base allo stato di gioco vengono disegnate delle entità con determinate proprietà.
         */
        switch (physicWorld.getGameState()) {
            case PAUSED :
                ((PaffGraphics) graphics).drawFilter(GlobalConstants.Colors.GREY);
                graphics.drawGameObject(playBtn);
                graphics.drawGameObject(exitBtn);
                graphics.drawGameObject(pause);
                break;
            case GAME_OVER :
                ((PaffGraphics) graphics).drawFilter(GlobalConstants.Colors.GREY);
                graphics.drawGameObject(rematchBtn);
                graphics.drawGameObject(exitBtn);
                graphics.drawGameObject(gameover);
                finalScore.text.toWrite = score.text.toWrite;
                /**
                 * Nel caso in cui la partita sia terminata e l'highscore battuto viene riprodotto un suono e lo score riportato in verde
                 */
                if(physicWorld.newhighscore) {
                    if(!newhighscorePlayed) {
                        Assets.highscore_gameover.play();
                        newhighscorePlayed=true;
                    }
                    ((PaffGraphics) graphics).drawText(finalScore, GlobalConstants.Colors.GREEN_LIGHT, 280);
                }
                else
                     ((PaffGraphics) graphics).drawText(finalScore, GlobalConstants.Colors.WHITE,280);
                break;
            case SETUP:
                if(animationPool.animationToExecute !=-1) {
                    ((PaffGraphics) graphics).drawFilter(GlobalConstants.Colors.GREY);
                    animationPool.getAnimationByID(animationPool.animationToExecute).executeAnimation();
                }
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
