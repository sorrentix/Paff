package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;

/**
 * Created by sorrentix on 11/11/2017.
 */

public class HelpScreen extends Screen {
    Graphics g;

    public HelpScreen(Game game) {
        super(game);
        g = game.getGraphics();
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {
        g.drawPixmap(Assets.menu_background,0,0);
        g.drawPixmap(Assets.help_screen,0,0);
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
