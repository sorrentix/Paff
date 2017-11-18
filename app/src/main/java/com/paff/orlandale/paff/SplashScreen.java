package com.paff.orlandale.paff;

import android.graphics.Rect;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Vec2;

import java.util.List;

/**
 * Created by sorrentix on 08/11/2017.
 */

class SplashScreen extends Screen {


    GameState state = GameState.BASIC_LOADING;

    AnimationPool animationPool;
    Graphics graphics;
    Audio audio;
    PhysicWorld physicWorld;

    public SplashScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

        animationPool = game.getAnimationPool();
        graphics = game.getGraphics();
        audio = game.getAudio();
        physicWorld = game.getPhysicWorld();

        switch (state){
            case BASIC_LOADING:
                updateBasics();
                break;
            case COMPLETE_LOADING:
                updateAll();
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

    private void updateBasics(){

        Assets.logo = graphics.newPixmap("logo.png", Graphics.PixmapFormat.ARGB4444);
        Assets.splashsound = audio.newMusic("splashsound.ogg");

        state = GameState.COMPLETE_LOADING;
    }

    private void updateAll(){
            Settings s=game.getSettings();
            graphics.clear(0xffffff);
            graphics.drawPixmap(Assets.logo, 262, 682);
            if(s.sounds)
                Assets.splashsound.play();

            //load here all other assets

            //Graphics
            Assets.menu_background = graphics.newPixmap("menu_background.jpg", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_play = graphics.newPixmap("play.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_play_click =  graphics.newPixmap("play_click.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_help = graphics.newPixmap("help.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_help_click =  graphics.newPixmap("help_click.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_settings = graphics.newPixmap("settings.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_settings_click =  graphics.newPixmap("settings_click.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_on = graphics.newPixmap("on.png", Graphics.PixmapFormat.ARGB4444);
            Assets.btn_off = graphics.newPixmap("off.png", Graphics.PixmapFormat.ARGB4444);
            Assets.sounds_text = graphics.newPixmap("sounds.png", Graphics.PixmapFormat.ARGB4444);
            Assets.music_text = graphics.newPixmap("music.png", Graphics.PixmapFormat.ARGB4444);
            Assets.help_screen = graphics.newPixmap("help_screen.png", Graphics.PixmapFormat.ARGB4444);

            //Physic World Objects
            List<GameObject> gameObjects = physicWorld.getGameObjects();
            gameObjects.add(new Bubble(game,new Vec2(0f,0f),2f,1, BodyType.staticBody,GlobalConstants.Colors.BLUE));
            gameObjects.add(new Bubble(game,new Vec2(0f,-9f),2f,1, BodyType.staticBody,GlobalConstants.Colors.BLUE));
            gameObjects.add(new Bubble(game,new Vec2(-8f,-3f),2f,1, BodyType.staticBody,GlobalConstants.Colors.BLUE));
            gameObjects.add(new Bubble(game,new Vec2(-6f,4f),1f,1, BodyType.staticBody,GlobalConstants.Colors.BLUE));
            gameObjects.add(new Bubble(game,new Vec2(-8f,-15f),1f,1, BodyType.staticBody,GlobalConstants.Colors.BLUE));
            gameObjects.add(new Bubble(game,new Vec2(6f,-12f),2f,1, BodyType.staticBody,GlobalConstants.Colors.BLUE));
            gameObjects.add(new Bubble(game,new Vec2(6f,12f),2f,1, BodyType.staticBody,GlobalConstants.Colors.BLUE));
            gameObjects.add(new Bubble(game,new Vec2(-6f,12f),1f,1, BodyType.staticBody,GlobalConstants.Colors.BLUE));
            physicWorld.setPaff(new Bubble(game,new Vec2(3f,0f),1f,1, BodyType.dynamicBody,GlobalConstants.Colors.RED));

            //Animations
            Pixmap images[] = new Pixmap[]{Assets.btn_play,
                    Assets.btn_play,Assets.btn_play_click,Assets.btn_play_click,
                    Assets.btn_play_click,Assets.btn_play,Assets.btn_play,
                    Assets.btn_play,Assets.btn_play_click};
            Rect btn = new Rect(60, 760, 60+Assets.btn_play.getWidth(), 760+Assets.btn_play.getHeight());
            Rect positions[] = new Rect[]{ btn, btn, btn, btn, btn, btn, btn, btn, btn };

            Animation anim = new Animation(graphics, images, positions,1);
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
            Assets.bubblexplosion = audio.newSound("bubblexplosion.ogg");
            Assets.gamesoundtheme = audio.newMusic("gamesoundtheme.ogg");
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
