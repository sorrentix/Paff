package com.badlogic.androidgames.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.FileIO;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;

import com.google.fpl.liquidfun.Vec2;
import com.paff.orlandale.paff.AnimationPool;
import com.paff.orlandale.paff.Box;
import com.paff.orlandale.paff.GlobalConstants;
import com.paff.orlandale.paff.PhysicToPixel;
import com.paff.orlandale.paff.PhysicWorld;
import com.paff.orlandale.paff.R;
import com.paff.orlandale.paff.Settings;


public abstract class AndroidGame extends Activity implements Game {
    private static final String TAG = "AndroidGame";

    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    AnimationPool animationPool;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;
    Settings settings;
    PhysicWorld physicWorld;
    AccelerometerHandler accelerometerHandler;

    int screenWidth;
    int screenHeight;
    DisplayMetrics metrics = new DisplayMetrics();
    float scaleFactor;
    float offset;



    Screen previousScreen = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;


        Bitmap frameBuffer = Bitmap.createBitmap(GlobalConstants.FRAME_BUFFER_WIDTH,
                GlobalConstants.FRAME_BUFFER_HEIGHT, Config.RGB_565);

        float scaleFactorX = (float)GlobalConstants.FRAME_BUFFER_WIDTH / (float)screenWidth;
        float scaleFactorY = (float)GlobalConstants.FRAME_BUFFER_HEIGHT / (float)screenHeight;
        scaleFactor = (scaleFactorX>scaleFactorY)? scaleFactorY:scaleFactorX;
        offset = Math.abs(metrics.widthPixels - ((1.0f/scaleFactor)* GlobalConstants.FRAME_BUFFER_WIDTH))/2.0f;

        Log.e(TAG,"screenWidth: " + screenWidth + " screenHeight: " + screenHeight + " scaleFactorX: " + scaleFactorX+ " scaleFactorY: " + scaleFactorY+"ScaleFactor: "+scaleFactor);

        PhysicToPixel.physicalSize = new Box(GlobalConstants.Physics.X_MIN,GlobalConstants.Physics.Y_MIN,GlobalConstants.Physics.X_MAX,GlobalConstants.Physics.Y_MAX);//new Box(-10,-15,10,15);
        PhysicToPixel.framebufferWidth = GlobalConstants.FRAME_BUFFER_WIDTH;
        PhysicToPixel.framebufferHeight = GlobalConstants.FRAME_BUFFER_HEIGHT;
        accelerometerHandler = new AccelerometerHandler(this);
        physicWorld = new PhysicWorld(PhysicToPixel.physicalSize,new Box(0,0,GlobalConstants.FRAME_BUFFER_WIDTH,GlobalConstants.FRAME_BUFFER_HEIGHT),new Vec2(0,0),accelerometerHandler);

        settings = new Settings(getApplicationContext());
        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        animationPool = new AnimationPool();
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleFactor, scaleFactor);
        screen = getStartScreen();
        setContentView(renderView);
        
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");



    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public AnimationPool getAnimationPool(){ return animationPool; }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public Settings getSettings() {return settings;}

    @Override
    public PhysicWorld getPhysicWorld() {return physicWorld;}

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen() {
        return screen;
    }

    public float getScaleFactor(){
        return scaleFactor;
    }

    public float getOffset(){
        return offset;
    }

    public DisplayMetrics getMetrics(){
        return metrics;
    }

    public void setPreviousScreen(Screen screen){
        previousScreen = screen;
    }

    public Screen getPreviousScreen(){
        return previousScreen;
    }

    public AccelerometerHandler getAccelerometerHandler() { return accelerometerHandler;}

}
