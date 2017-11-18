package com.paff.orlandale.paff;

import android.util.Log;

import com.badlogic.androidgames.framework.*;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class PaffGame extends AndroidGame {

    public static final String TAG = "PaffGame";

    @Override
    public Screen getStartScreen() {
        return new SplashScreen(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e(TAG, "onPause: Entrato");
        if (Assets.gamesoundtheme!=null){//TODO SOSTITUIRE CON PAUSA TOTALE DEL GIOCO
            Log.e(TAG, "onPause: Entrato in musictheme!=null");
            super.getAudio().pauseAll();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(TAG, "onResume: entrato");
        if(!Assets.areReady()) {
            Log.e(TAG, "onResume: entrato in areReady");
            getStartScreen();
        }
        else if(Settings.music)//TODO SOSTITUIRE CON RESUME TOTALE DEL GIOCO
        {
            Log.e(TAG, "onResume: entrato in musicaaaaaa");
            super.getAudio().resumeAll();
        }

    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed: vado a"+super.getPreviousScreen() );

        Log.e(TAG, "onBackPressed: "+super.getCurrentScreen().getClass());
        if (super.getCurrentScreen() instanceof GameMenuScreen)
            super.onBackPressed();
        super.setScreen(super.getPreviousScreen());
    }

}
