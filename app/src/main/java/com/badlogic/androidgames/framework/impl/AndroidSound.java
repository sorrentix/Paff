package com.badlogic.androidgames.framework.impl;

import android.media.SoundPool;

import com.badlogic.androidgames.framework.Sound;

public class AndroidSound implements Sound {
    final int soundId;
    SoundPool soundPool;
    boolean loaded = false;

    public AndroidSound(SoundPool soundPool, final int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                System.out.println("onload complete listenere called---------------------------------------------for id"+ soundId + "input id"+ i);

                loaded = true;
            }
        });
    }



    public boolean isLoaded(){
        return loaded;
    }

    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }

}
