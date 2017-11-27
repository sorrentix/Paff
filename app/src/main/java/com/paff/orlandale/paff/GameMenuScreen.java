package com.paff.orlandale.paff;


import android.util.Log;

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

class GameMenuScreen extends Screen {
    Graphics g;
    Audio a;
    AnimationPool animationPool;
    Settings s;
    Input i;


    GameObject playBtn;
    GameObject settingsBtn;
    GameObject helpBtn;
    GameObject background;
    GameObject logo;

    GameState state = GameState.WAITING;


    public GameMenuScreen(Game game) {
        super(game);
        i = game.getInput();
        i.clearTouchEvents();
        playBtn     = setButton(new Position(60, 760), Assets.btn_play, Assets.bubblexplosion, i);
        settingsBtn = setButton(new Position(640, 960), Assets.btn_settings, Assets.bubblexplosion, i);
        helpBtn     = setButton(new Position(240, 1360), Assets.btn_help, Assets.bubblexplosion, i);
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
                animationPool.getAnimationByID(1).executeAnimation();
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
