package com.badlogic.androidgames.framework.impl;

import android.media.SoundPool;
import android.support.annotation.NonNull;

import com.badlogic.androidgames.framework.Sound;
import com.paff.orlandale.paff.GlobalConstants;
import com.paff.orlandale.paff.Settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AndroidSound implements Sound {
    final int soundId;
    SoundPool soundPool;
    boolean loaded = false;
    int streamId;

    public AndroidSound(SoundPool soundPool, final int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;

    }
    @Override
    public void setIsLoaded(){
        loaded = true;
    }

    @Override
    public boolean isLoaded(){
        return loaded;
    }

    @Override
    public void play() {
        if(Settings.sounds)
            streamId = soundPool.play(soundId, GlobalConstants.VOLUME, GlobalConstants.VOLUME, 0, 0, 1);
    }

    @Override
    public void playLoop() {
        if(Settings.sounds)
            streamId = soundPool.play(soundId, GlobalConstants.VOLUME, GlobalConstants.VOLUME, 0, -1, 1);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }

    @Override
    public void stop() {soundPool.stop(streamId);}
}
