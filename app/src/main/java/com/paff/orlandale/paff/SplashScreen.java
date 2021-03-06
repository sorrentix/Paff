package com.paff.orlandale.paff;

import android.graphics.Rect;
import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
/**
 * Created by sorrentix on 08/11/2017.
 */

/**
 * Schermata iniziale in cui avviene il caricamento degli assets
 */
class SplashScreen extends Screen {


    GameState state = GameState.BASIC_LOADING;
    long startTime;
    long currTime;


    public SplashScreen(Game game) {
        super(game);

        startTime = System.nanoTime();
    }

    @Override
    public void update(float deltaTime) {

        AnimationPool animationPool = game.getAnimationPool();
        Graphics g = game.getGraphics();
        Font f = game.getFont();
        Audio a = game.getAudio();

        switch (state){
            case BASIC_LOADING:
                updateBasics(g, a);
                break;
            case COMPLETE_LOADING:
                updateAll(g, a,f, animationPool);
                break;
            case COMPLETE_ANIMATION:
                waitForAnimationComplete();
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
        Assets.splashsound = a.newMusic("splashsound.ogg");

        state = GameState.COMPLETE_LOADING;
    }

    private void updateAll(Graphics g, Audio a, Font f, final AnimationPool animationPool){
        g.clear(0xffffff);
        g.drawPixmap(Assets.logo, 262, 682);
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
        Assets.btn_resume = g.newPixmap("resume_btn.png", Graphics.PixmapFormat.ARGB4444);
        Assets.btn_replay = g.newPixmap("replay_btn.png", Graphics.PixmapFormat.ARGB4444);
        Assets.btn_exit = g.newPixmap("exit_btn.png", Graphics.PixmapFormat.ARGB4444);
        Assets.sounds_text = g.newPixmap("sounds.png", Graphics.PixmapFormat.ARGB4444);
        Assets.music_text = g.newPixmap("music.png", Graphics.PixmapFormat.ARGB4444);
        Assets.pause_text = g.newPixmap("pausa.png", Graphics.PixmapFormat.ARGB4444);
        Assets.gameover_text = g.newPixmap("game_over_red.png", Graphics.PixmapFormat.ARGB4444);
        Assets.help_screen = g.newPixmap("help_screen.png", Graphics.PixmapFormat.ARGB4444);
        Assets.highscore = g.newPixmap("top_highscore.png",Graphics.PixmapFormat.ARGB4444);
        Assets.score = g.newPixmap("score.png",Graphics.PixmapFormat.ARGB4444);
        Assets.count3 = g.newPixmap("count3.png",Graphics.PixmapFormat.ARGB4444);
        Assets.count2 = g.newPixmap("count2.png",Graphics.PixmapFormat.ARGB4444);
        Assets.count1 = g.newPixmap("count1.png",Graphics.PixmapFormat.ARGB4444);
        Assets.countJump = g.newPixmap("countjump.png",Graphics.PixmapFormat.ARGB4444);
        Assets.speedup_image = g.newPixmap("speedup_image.png",Graphics.PixmapFormat.ARGB4444);
        Assets.scoreText = g.newPixmap("scoretext.png",Graphics.PixmapFormat.ARGB4444);
        Assets.credits = g.newPixmap("credits_nomi.png", Graphics.PixmapFormat.ARGB4444);
        //Font
        Assets.font = f.newFont("paff_font.ttf");


        //Sounds
        Assets.bubblexplosion = a.newSound("bubblexplosion.ogg");
        Assets.gamesoundtheme = a.newMusic("gamesoundtheme.ogg");
        Assets.gamemenusoundtheme = a.newMusic("gamemenusoundtheme.ogg");
        Assets.countdown = a.newSound("jump.ogg");
        Assets.newhighscore = a.newSound("newhighscore.ogg");
        Assets.highscore_gameover = a.newSound("hs_gameover.ogg");
        Assets.fall_gameover = a.newSound("gameover_fall.ogg");
        Assets.speedup = a.newSound("doublespeed.ogg");



          //Animations
        Pixmap countDown3[]    = new Pixmap[60];
        Pixmap countDown2[]    = new Pixmap[60];
        Pixmap countDown1[]    = new Pixmap[60];
        Pixmap countDownJump[] = new Pixmap[60];
        Pixmap speedUp[]       = new Pixmap[60];

        Rect speedUpStartingRect = new Rect(250,10,250 + Assets.speedup_image.getWidth(),10 + Assets.speedup_image.getHeight());
        Rect speedUpPositions[] = new Rect[60];

        for(int i=0; i < speedUpPositions.length; i++) {
            if (i==0)
                speedUpPositions[i] = new Rect(speedUpStartingRect.left,speedUpStartingRect.top,speedUpStartingRect.right,speedUpStartingRect.bottom);
            else
                speedUpPositions[i] = new Rect(speedUpPositions[i-1].left + 2,speedUpPositions[i-1].top + 1 ,speedUpPositions[i-1].right - 2,speedUpPositions[i-1].bottom - 1 );

            speedUp[i] = Assets.speedup_image;
        }

        Rect countDown3StartingRect = new Rect(140, 560, 140 + Assets.count3.getWidth(), 560 + Assets.count3.getHeight());
        Rect countDown3Positions[] = new Rect[60];

        for (int i = 0; i < countDown3Positions.length; i++ ){
            if (i == 0)
                countDown3Positions[i] = new Rect(countDown3StartingRect.left, countDown3StartingRect.top, countDown3StartingRect.right, countDown3StartingRect.bottom);
            else
                countDown3Positions[i] = new Rect(countDown3Positions[i-1].left + 5, countDown3Positions[i-1].top + 5, countDown3Positions[i-1].right - 5, countDown3Positions[i-1].bottom - 5);
            countDown3[i]= Assets.count3;
            countDown2[i]= Assets.count2;
            countDown1[i]= Assets.count1;
            countDownJump[i]= Assets.countJump;
        }
  
        Animation countDown3Animation    = new Animation(g, countDown3, new Rect(0,0,Assets.count3.getWidth(), Assets.count3.getHeight()), countDown3Positions,1);
        Animation countDown2Animation    = new Animation(g, countDown2, new Rect(0,0,Assets.count2.getWidth(), Assets.count2.getHeight()), countDown3Positions,2);
        Animation countDown1Animation    = new Animation(g, countDown1, new Rect(0,0,Assets.count1.getWidth(), Assets.count1.getHeight()), countDown3Positions,3);
        Animation countDownJumpAnimation = new Animation(g, countDownJump, new Rect(0,0,Assets.countJump.getWidth(), Assets.countJump.getHeight()), countDown3Positions,4);
        Animation speedUpAnimation       = new Animation(g, speedUp, new Rect(0,0,Assets.speedup_image.getWidth(),Assets.speedup_image.getHeight()),speedUpPositions,10);

        countDown3Animation.addListener(new AnimationPool.onAnimationCompleteListener() {
            @Override
            public void onAnimationComplete(Animation anim) {
                //execute something when the animation is complete
                animationPool.animationToExecute = 2;
                animationPool.getAnimationByID(2).currentImage=0;
            }
        });
        animationPool.loadAnimation(countDown3Animation);

        countDown2Animation.addListener(new AnimationPool.onAnimationCompleteListener() {
            @Override
            public void onAnimationComplete(Animation anim) {
                //execute something when the animation is complete
                animationPool.animationToExecute = 3;
                animationPool.getAnimationByID(3).currentImage=0;
            }
        });
        animationPool.loadAnimation(countDown2Animation);

        countDown1Animation.addListener(new AnimationPool.onAnimationCompleteListener() {
            @Override
            public void onAnimationComplete(Animation anim) {
                //execute something when the animation is complete
                animationPool.animationToExecute = 4;
                animationPool.getAnimationByID(4).currentImage=0;
            }
        });
        animationPool.loadAnimation(countDown1Animation);

        countDownJumpAnimation.addListener(new AnimationPool.onAnimationCompleteListener() {
            @Override
            public void onAnimationComplete(Animation anim) {
                //execute something when the animation is complete
                animationPool.animationToExecute = -1;
                Assets.gamesoundtheme.setLooping(true);
                Assets.gamesoundtheme.play();
            }
        });
        animationPool.loadAnimation(countDownJumpAnimation);

        speedUpAnimation.addListener(new AnimationPool.onAnimationCompleteListener() {
            @Override
            public void onAnimationComplete(Animation a) {
                animationPool.animationToExecute=-2;
            }
        });
        animationPool.loadAnimation(speedUpAnimation);



        Assets.flagReady = true;
        state = GameState.COMPLETE_ANIMATION;

    }

    public void waitForAnimationComplete(){
        currTime = System.nanoTime();
        if(Settings.music) {
            if (!Assets.splashsound.isPlaying())
                game.setScreen(new GameMenuScreen(game));
        }
        else if (currTime-startTime >= 3800000000.0f)
            game.setScreen(new GameMenuScreen(game));

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
