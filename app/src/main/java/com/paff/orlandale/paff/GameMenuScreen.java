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


    GameObject playBtn;
    GameObject settingsBtn;
    GameObject helpBtn;
    GameObject background;
    GameObject logo;

    GameState state = GameState.WAITING;


    public GameMenuScreen(Game game) {
        super(game);
        Input i = game.getInput();
        playBtn     = setButton(new Position(60, 760), Assets.btn_play, Assets.bubblexplosion, i);
        settingsBtn = setButton(new Position(640, 960), Assets.btn_settings, Assets.bubblexplosion, i);
        helpBtn     = setButton(new Position(240, 1360), Assets.btn_help, Assets.bubblexplosion, i);
        logo        = setSimpleImage(new Position(262, 160), Assets.logo);
        background  = setSimpleImage(new Position(0, 0), Assets.menu_background);

        g = game.getGraphics();
        a = game.getAudio();
        animationPool = game.getAnimationPool();

        if (s.music && game.getPreviousScreen() == null) {
            Assets.gamesoundtheme.setLooping(true);
            Assets.gamesoundtheme.play();
        }
        game.setPreviousScreen(game.getCurrentScreen());
    }



    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        switch(state){
            case WAITING:
                Log.e("TOUCH EVENT:"," waiting update");
                break;
            case PLAY:
                game.setScreen(new GameScreen(game));
                Log.e("TOUCH EVENT:"," play update");
                break;
            case HELP:
                Log.e("TOUCH EVENT:"," help update");
                game.setScreen(new HelpScreen(game));
                break;
            case SETTINGS:
                Log.e("TOUCH EVENT:"," settings update");
                game.setScreen(new SettingsScreen(game));
                break;
            default:
                Log.e("TOUCH EVENT:"," deafault update");
                break;

        }

        for (int i = 0; i < touchEvents.size(); ++i) {
            TouchEvent event = touchEvents.get(i);

            if (event.type == TouchEvent.TOUCH_UP) {
                Log.e("TOUCH EVENT:"," touched");
                if (playBtn.evtManager.inBounds(event)) {
                    Log.e("TOUCH EVENT:"," play touch");
                    state = GameState.PLAY;
                } else if (settingsBtn.evtManager.inBounds(event)) {
                    Log.e("TOUCH EVENT:"," settings touch");
                    state = GameState.SETTINGS;
                }else if (helpBtn.evtManager.inBounds(event)) {
                    Log.e("TOUCH EVENT:"," help touch");
                    state = GameState.HELP;
                }
            }
        }

    }

    @Override
    public void present(float deltaTime) {

        switch(state){
            case WAITING:
                Log.e("TOUCH EVENT:"," waiting pres");
                g.drawGameObject(background);
                g.drawGameObject(logo);
                g.drawGameObject(playBtn);
                g.drawGameObject(settingsBtn);
                g.drawGameObject(helpBtn);
                break;
            case PLAY:
                Log.e("TOUCH EVENT:"," play pres");
                playBtn.sound.play();
                animationPool.getAnimationByID(1).executeAnimation();
                break;
            case HELP:
                Log.e("TOUCH EVENT:"," help pres");
                helpBtn.sound.play();
                break;
            case SETTINGS:
                Log.e("TOUCH EVENT:"," settings pres");
                settingsBtn.sound.play();
                break;
            default:
                Log.e("TOUCH EVENT:"," default pres");
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
