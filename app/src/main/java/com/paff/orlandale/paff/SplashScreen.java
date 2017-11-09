package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
/**
 * Created by sorrentix on 08/11/2017.
 */

class SplashScreen extends Screen {

    enum GameState{
        BasicLoading,
        CompleteLoading
    }
    GameState state = GameState.BasicLoading;

    public SplashScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

        Graphics g = game.getGraphics();
        Audio a = game.getAudio();
        switch (state){
            case BasicLoading:
                updateBasics(g, a);
                break;
            case CompleteLoading:
                updateAll(g, a);
                break;
            default:
                    break;
        }

    }

    @Override
    public void present(float deltaTime) {
    }

    private void updateBasics(Graphics g, Audio a){

        Assets.logo = g.newPixmap("logo.png", Graphics.PixmapFormat.ARGB4444);
        Assets.openBottle = a.newSound("openbottle.wav");
        Assets.bubbling = a.newSound("bubbling.wav");

        state = GameState.CompleteLoading;
    }

    private void updateAll(Graphics g, Audio a){

        if(Assets.openBottle.isLoaded() && Assets.bubbling.isLoaded() ) {

            g.clear(0xffffff);
            g.drawPixmap(Assets.logo, 262, 682);
            Assets.openBottle.play(1);
            Assets.bubbling.play(1);

            //load here all other assets
            Assets.menu_background = g.newPixmap("menu_background.jpg", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_play = g.newPixmap("play.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_help = g.newPixmap("help.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_settings = g.newPixmap("settings.png", Graphics.PixmapFormat.ARGB4444);


            game.setScreen(new GameMenuScreen(game));
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
