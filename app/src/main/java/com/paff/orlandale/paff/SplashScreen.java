package com.paff.orlandale.paff;

import android.graphics.Rect;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Screen;
/**
 * Created by sorrentix on 08/11/2017.
 */

class SplashScreen extends Screen {


    GameState state = GameState.BASIC_LOADING;
    long startTime;


    public SplashScreen(Game game) {
        super(game);
        startTime = System.nanoTime();
    }

    @Override
    public void update(float deltaTime) {

        AnimationPool animationPool = game.getAnimationPool();
        Graphics g = game.getGraphics();
        Audio a = game.getAudio();

        switch (state){
            case BASIC_LOADING:
                updateBasics(g, a);
                break;
            case COMPLETE_LOADING:
                updateAll(g, a, animationPool);
                break;
            case COMPLETE_ANIMATION:
                waitForAnimationComplete();
            default:
                    break;
        }

    }

    @Override
    public void present(float deltaTime) {
    }

    private void updateBasics(Graphics g, Audio a){

        Assets.logo = g.newPixmap("logo.png", Graphics.PixmapFormat.ARGB4444);
        Assets.splashsound = a.newMusic("splashsound.ogg");

        state = GameState.COMPLETE_LOADING;
    }

    private void updateAll(Graphics g, Audio a, AnimationPool animationPool){
            g.clear(0xffffff);
            g.drawPixmap(Assets.logo, 262, 682);
            if(Settings.sounds)
                Assets.splashsound.play();

            //load here all other assets

            //Graphics
            Assets.menu_background = g.newPixmap("menu_background.jpg", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_play = g.newPixmap("play.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_play_click =  g.newPixmap("play_click.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_help = g.newPixmap("help.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_help_click =  g.newPixmap("help_click.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_settings = g.newPixmap("settings.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_settings_click =  g.newPixmap("settings_click.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_on = g.newPixmap("on.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_off = g.newPixmap("off.png", Graphics.PixmapFormat.ARGB4444);
            Assets.sounds_text = g.newPixmap("sounds.png", Graphics.PixmapFormat.ARGB4444);
            Assets.music_text = g.newPixmap("music.png", Graphics.PixmapFormat.ARGB4444);
            Assets.help_screen = g.newPixmap("help_screen.png", Graphics.PixmapFormat.ARGB4444);

            //Animations
            Pixmap images[] = new Pixmap[]{Assets.btn_play,
                    Assets.btn_play,Assets.btn_play_click,Assets.btn_play_click,
                    Assets.btn_play_click,Assets.btn_play,Assets.btn_play,
                    Assets.btn_play,Assets.btn_play_click};
            Rect btn = new Rect(60, 760, 60+Assets.btn_play.getWidth(), 760+Assets.btn_play.getHeight());
            Rect positions[] = new Rect[]{ btn, btn, btn, btn, btn, btn, btn, btn, btn };

            Animation anim = new Animation(g, images, positions,1);
            anim.addListener(new AnimationPool.onAnimationCompleteListener() {
                @Override
                public void onAnimationComplete(Animation anim) {
                    //fai qualcos quando l'animazione a è completa

                    game.setScreen(new SettingsScreen(game));
                    System.out.println("animation complete !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            });
            animationPool.loadAnimation(anim);


            //Sounds
            Assets.bubblexplosion = a.newSound("bubblexplosion.ogg");
            Assets.gamesoundtheme = a.newMusic("gamesoundtheme.ogg");
            Assets.flagReady = true;
            state = GameState.COMPLETE_ANIMATION;


    }

    public void waitForAnimationComplete(){
        if(!Assets.splashsound.isPlaying()){
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
