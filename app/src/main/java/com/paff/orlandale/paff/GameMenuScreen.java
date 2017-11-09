package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

/**
 * Created by sorrentix on 08/11/2017.
 */

class GameMenuScreen extends Screen {
    Graphics g;
    Audio a;
    GameState state = GameState.Waiting;

    public GameMenuScreen(Game game) {
        super(game);
        g = game.getGraphics();
        a = game.getAudio();
        Assets.gamesoundtheme.playLoop(1);
    }

    enum GameState{
        Waiting,
        Play,
        ChangeScreen
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        for (int i = 0; i < touchEvents.size(); ++i) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {

                if (inBounds(event, 60, 760, Assets.btn_play.getWidth(), Assets.btn_play.getHeight())) {
                    state = GameState.Play;
                }

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
                Assets.bubblexplosion.play(1);
                g.drawPixmap(Assets.btn_play_click, 60, 760);
                state = GameState.ChangeScreen;
                break;
            case ChangeScreen:
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

    public boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < (x + (width -1)) &&
                event.y > y && event.y < (y + (height - 1)))
            return true;
        else
            return false;
    }
}
