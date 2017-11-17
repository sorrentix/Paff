package com.badlogic.androidgames.framework.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.Sound;

public class AndroidAudio implements Audio {
    AssetManager assets;
    SoundPool soundPool;
    List<MediaPlayer> mediaPlayers = new ArrayList<>();
    Map<Integer, Sound> sounds = new HashMap<>();


    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

    }

    @Override
    public Music newMusic(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            mediaPlayers.add(new MediaPlayer());
            return new AndroidMusic(assetDescriptor, mediaPlayers.get(mediaPlayers.size()-1));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }

    @Override
    public Sound getSoundByID(int ID){
        return sounds.get(ID);
    }

    @Override
    public Sound newSound(String filename) {
        try {

            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            int soundId = soundPool.load(assetDescriptor, 0);
            Sound s = new AndroidSound(soundPool, soundId);
            sounds.put(soundId,s);

            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    System.out.println("onload complete listenere called---------------------------------------------for sound"+ getSoundByID(i) + "input id"+ i);
                    getSoundByID(i).setIsLoaded();
                }
            });
            return s;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load sound '" + filename + "'");
        }
    }

    @Override
    public void pauseAll(){
        soundPool.autoPause();
        for (MediaPlayer p : mediaPlayers) {
            if (p.isPlaying())
                p.pause();
        }
    }

    @Override
    public void resumeAll(){
        soundPool.autoResume();
        for (MediaPlayer mediaPlayer : mediaPlayers) {
            if (mediaPlayer.isLooping())
                mediaPlayer.start();
        }
    }
}
