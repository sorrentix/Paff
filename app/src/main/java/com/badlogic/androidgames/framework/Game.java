package com.badlogic.androidgames.framework;


import com.paff.orlandale.paff.AnimationPool;
import com.paff.orlandale.paff.Settings;


public interface Game {
    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public Audio getAudio();

    public AnimationPool getAnimationPool();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();

    public Settings getSettings();

    public void setPreviousScreen(Screen screen);

    public Screen getPreviousScreen();
}