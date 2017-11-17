package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

/**
 * Created by sorrentix on 10/11/2017.
 */

public class SettingsScreen extends Screen {
    Graphics g;
    Audio a;
    GameState state = GameState.SETUP;
    Settings s;



    public SettingsScreen(Game game) {
        super(game);
        g = game.getGraphics();
        a = game.getAudio();
        s = game.getSettings();
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        switch(state){
            case SETUP:

                break;
            case SOUND_ON:
                s.setSounds(true);
                state = GameState.WAITING;
                break;
            case SOUND_OFF:
                s.setSounds(false);
                state = GameState.WAITING;
                break;
            case MUSIC_ON:
                s.setMusic(true);
                state = GameState.WAITING;
                break;
            case MUSIC_OFF:
                s.setMusic(false);
                state = GameState.WAITING;
                break;
            case WAITING:
                break;
            default:
                break;
        }

        for (int i = 0; i < touchEvents.size(); ++i) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(event, 600, 1200, Assets.btn_on.getWidth(), Assets.btn_on.getHeight())){
                    if (s.music)
                        state = GameState.MUSIC_OFF;
                    else
                        state = GameState.MUSIC_ON;
                }
                else if (inBounds(event, 600, 800, Assets.btn_on.getWidth(), Assets.btn_on.getHeight())){
                    if (s.sounds)
                        state = GameState.SOUND_OFF;
                    else
                        state = GameState.SOUND_ON;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        switch(state){
            case SETUP:
                g.drawPixmap(Assets.menu_background, 0, 0);
                g.drawPixmap(Assets.logo, 262, 160);
                g.drawPixmap(Assets.sounds_text, 100, 900);
                g.drawPixmap((s.sounds)?Assets.btn_on:Assets.btn_off, 600, 800);
                g.drawPixmap(Assets.music_text, 100, 1300);
                g.drawPixmap((s.music)?Assets.btn_on:Assets.btn_off, 600, 1200);
                break;
            case SOUND_ON:
                g.drawPixmap(Assets.btn_on, 600, 800);
               // if (s.sounds)
                    Assets.bubblexplosion.play(1);
                break;
            case SOUND_OFF:
                g.drawPixmap(Assets.btn_off, 600, 800);
                break;
            case MUSIC_ON:
                g.drawPixmap(Assets.btn_on, 600, 1200);
                Assets.gamesoundtheme.setLooping(true);
                Assets.gamesoundtheme.play();
                if (s.sounds)
                    Assets.bubblexplosion.play(1);
                break;
            case MUSIC_OFF:
                g.drawPixmap(Assets.btn_off, 600, 1200);
                Assets.gamesoundtheme.stop();
                if (s.sounds)
                    Assets.bubblexplosion.play(1);
                break;
            case WAITING:
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
