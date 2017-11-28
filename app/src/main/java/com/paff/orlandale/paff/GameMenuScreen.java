package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

/**
 * Created by sorrentix on 08/11/2017.
 */

/**
 * Screen del menu di gioco
 */
class GameMenuScreen extends Screen {
    Graphics g;
    Audio a;
    AnimationPool animationPool;
    Settings s;
    Input i;


    GameObject playBtn;
    GameObject playBtnClicked;
    GameObject settingsBtn;
    GameObject settingsBtnClicked;
    GameObject helpBtn;
    GameObject helpBtnClicked;
    GameObject background;
    GameObject logo;

    GameState state = GameState.WAITING;

    /**
     * Costruttore della classe. Inizializza tutte le entità necessarie al rendering.
     * @param game Implementazione dell'interfaccia Game
     */
    public GameMenuScreen(Game game) {
        super(game);
        i = game.getInput();
        i.clearTouchEvents();
        playBtn     = setButton(new Position(60, 760), Assets.btn_play, Assets.bubblexplosion, i);
        playBtnClicked     = setButton(new Position(60, 760), Assets.btn_play_click, Assets.bubblexplosion, i);
        settingsBtn = setButton(new Position(640, 960), Assets.btn_settings, Assets.bubblexplosion, i);
        settingsBtnClicked = setButton(new Position(640, 960), Assets.btn_settings_click, Assets.bubblexplosion, i);
        helpBtn     = setButton(new Position(240, 1360), Assets.btn_help, Assets.bubblexplosion, i);
        helpBtnClicked     = setButton(new Position(240, 1360), Assets.btn_help_click, Assets.bubblexplosion, i);
        logo        = setSimpleImage(new Position(262, 160), Assets.logo);
        background  = setSimpleImage(new Position(0, 0), Assets.menu_background);

        g = game.getGraphics();
        a = game.getAudio();
        animationPool = game.getAnimationPool();

        if (s.music && game.getPreviousScreen() == null) {
            Assets.gamesoundtheme.setLooping(false);
            Assets.gamesoundtheme.pause();
            Assets.gamemenusoundtheme.setLooping(true);
            Assets.gamemenusoundtheme.play();
        }
    }


    /**
     * Il metodo si occupa di effettuare il cambio di stato in base al tasto cliccato.
     * @param deltaTime il tempo intercorso tra due frame
     */
    @Override
    public void update(float deltaTime) {

        List<TouchEvent> touchEvents = i.getTouchEvents();

        for (int i = 0; i < touchEvents.size(); ++i) {
            TouchEvent event = touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_UP) {
                if (playBtn.evtManager.inBounds(event))
                    state = GameState.PLAY;
                else if (settingsBtn.evtManager.inBounds(event))
                    state = GameState.SETTINGS;
                else if (helpBtn.evtManager.inBounds(event))
                    state = GameState.HELP;
            }
        }

    }

    /**
     * Il metodo si occupa di effettuare il rendering in base allo stato attivo
     * @param deltaTime il tempo intercorso tra due frame
     */
    @Override
    public void present(float deltaTime) {

        switch(state){

            case WAITING:
                g.drawGameObject(background);
                g.drawGameObject(logo);
                g.drawGameObject(playBtn);
                g.drawGameObject(settingsBtn);
                g.drawGameObject(helpBtn);

                break;
            case PLAY:
                playBtn.sound.play();
                game.setScreen(new GameScreen(game));
                break;
            case HELP:
                helpBtn.sound.play();
                game.setScreen(new HelpScreen(game));
                break;
            case SETTINGS:
                settingsBtn.sound.play();
                game.setScreen(new SettingsScreen(game));
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
