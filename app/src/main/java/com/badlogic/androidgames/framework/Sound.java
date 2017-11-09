package com.badlogic.androidgames.framework;

public interface Sound {
    public void play(float volume);

    public void dispose();

    public boolean isLoaded();

    public void setIsLoaded();
}
