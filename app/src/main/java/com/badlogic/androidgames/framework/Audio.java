package com.badlogic.androidgames.framework;

public interface Audio {
    public Music newMusic(String filename);

    public Sound newSound(String filename);

    public Sound getSoundByID(int id);

    public void pauseAll();

    public void resumeAll();

}
