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

    GameObject soundsBtn;
    GameObject musicBtn;
    GameObject logo;
    GameObject background;
    GameObject soundText;
    GameObject musicText;

    public SettingsScreen(Game game) {
        super(game);

        Input i = game.getInput();
        i.clearTouchEvents();
        soundsBtn   = setButton( new Position(600, 800),
                                 Settings.sounds ? Assets.btn_on : Assets.btn_off,
                                 Assets.bubblexplosion, i);
        musicBtn    = setButton( new Position(600, 1200),
                                 Settings.music ? Assets.btn_on : Assets.btn_off,
                                 Assets.bubblexplosion, i);
        logo        = setSimpleImage(new Position(262, 160), Assets.logo);
        background  = setSimpleImage(new Position(0, 0), Assets.menu_background);
        soundText   = setSimpleImage(new Position( 100, 900), Assets.sounds_text);
        musicText   = setSimpleImage(new Position( 100, 1300), Assets.music_text);

        g = game.getGraphics();
        a = game.getAudio();
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        switch(state){
            case SOUND_ON:
                Settings.sounds = true;
                state = GameState.WAITING;
                break;
            case SOUND_OFF:
                Settings.sounds = false;
                state = GameState.WAITING;
                break;
            case MUSIC_ON:
                Settings.music = true;
                state = GameState.WAITING;
                break;
            case MUSIC_OFF:
                Settings.music = false;
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
                if (musicBtn.evtManager.inBounds(event)){
                    if (Settings.music) {
                        state = GameState.MUSIC_OFF;
                        musicBtn.image = Assets.btn_off;
                    } else{
                        state = GameState.MUSIC_ON;
                        musicBtn.image = Assets.btn_on;
                    }
                    Settings.setMusic(!Settings.music);
                }
                else if (soundsBtn.evtManager.inBounds(event)){
                    if (Settings.sounds) {
                        state = GameState.SOUND_OFF;
                        soundsBtn.image = Assets.btn_off;
                    } else {
                        state = GameState.SOUND_ON;
                        soundsBtn.image = Assets.btn_on;
                    }
                    Settings.setSounds(!Settings.sounds);
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        g.drawGameObject(background);
        g.drawGameObject(logo);

        g.drawGameObject(soundText);
        g.drawGameObject(soundsBtn);

        g.drawGameObject(musicBtn);
        g.drawGameObject(musicText);

        switch(state){
            case SOUND_ON:
                soundsBtn.sound.play();
                break;
            case MUSIC_ON:
                Assets.gamemenusoundtheme.setLooping(true);
                Assets.gamemenusoundtheme.play();
                musicBtn.sound.play();
                break;
            case MUSIC_OFF:
                Assets.gamemenusoundtheme.pause();
                musicBtn.sound.play();
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
