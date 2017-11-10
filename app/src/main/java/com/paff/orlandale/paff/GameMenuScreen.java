package com.paff.orlandale.paff;

import android.graphics.Rect;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pixmap;
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

    GameState state = GameState.Waiting;


    public GameMenuScreen(Game game) {
        super(game);
        g = game.getGraphics();
        a = game.getAudio();

        animationPool = game.getAnimationPool();
        s=game.getSettings();
        if(s.music && game.getPreviousScreen()==null)
             Assets.gamesoundtheme.playLoop(0.2f);
        game.setPreviousScreen(game.getCurrentScreen());

    }

    enum GameState{
        Waiting,
        Play,
        Help,
        Settings,
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        switch(state){
            case Waiting:
                break;
            case Play:
               //TODO CAMBIARE IN GAMESCREEN
                break;
            case Help:
                game.setScreen(new SettingsScreen(game));//TODO CAMBIARE IN HELPSCREEN
                break;
            case Settings:
                game.setScreen(new SettingsScreen(game));
                break;
            default:
                break;

        }


        for (int i = 0; i < touchEvents.size(); ++i) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, 60, 760, Assets.btn_play.getWidth(), Assets.btn_play.getHeight()))
                    state = GameState.Play;
                else if (inBounds(event, 640, 960, Assets.btn_settings.getWidth(), Assets.btn_settings.getHeight()))
                    state = GameState.Settings;
                else if (inBounds(event, 240, 1360, Assets.btn_help.getWidth(), Assets.btn_help.getHeight()))
                    state = GameState.Help;

            }
        }

    }

    @Override
    public void present(float deltaTime) {

        switch(state){
            case Waiting:
                g.drawPixmap(Assets.menu_background, 0, 0);
                g.drawPixmap(Assets.logo, 262, 160);
                g.drawPixmap(Assets.btn_play, 60, 760);
                g.drawPixmap(Assets.btn_settings, 640, 960);
                g.drawPixmap(Assets.btn_help, 240, 1360);
                break;
            case Play:
                if(s.sounds)
                    Assets.bubblexplosion.play(1);
                animationPool.getAnimationByID(1).executeAnimation();
                break;
            case Help:
                g.drawPixmap(Assets.btn_help_click, 240, 1360);
                if(s.sounds)
                    Assets.bubblexplosion.play(1);
                break;
            case Settings:
                g.drawPixmap(Assets.btn_settings_click, 640, 960);
                if(s.sounds)
                    Assets.bubblexplosion.play(1);
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
