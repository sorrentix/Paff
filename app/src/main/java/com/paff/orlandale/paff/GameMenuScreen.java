package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;

/**
 * Created by sorrentix on 08/11/2017.
 */

class GameMenuScreen extends Screen {
    public GameMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {

        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.menu_background, 0, 0);
        g.drawPixmap(Assets.logo, 262, 160);
        g.drawPixmap(Assets.btn_play, 60, 760);
        g.drawPixmap(Assets.btn_settings, 640, 960);
        g.drawPixmap(Assets.btn_help, 240, 1360);


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
