package com.badlogic.androidgames.framework;


import com.badlogic.androidgames.framework.impl.AccelerometerHandler;
import com.paff.orlandale.paff.AnimationPool;
import com.paff.orlandale.paff.Font;
import com.paff.orlandale.paff.PhysicWorld;
import com.paff.orlandale.paff.Settings;


public interface Game {
    public Input getInput();
    
    public FileIO getFileIO();

    public Graphics getGraphics();

    public Audio getAudio();

    public AnimationPool getAnimationPool();

    PhysicWorld getPhysicWorld();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();

    public void setPreviousScreen(Screen screen);

    public Screen getPreviousScreen();

    Font getFont();
}