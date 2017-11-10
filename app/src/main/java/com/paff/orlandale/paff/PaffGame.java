package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.*;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class PaffGame extends AndroidGame {

    @Override
    public Screen getStartScreen() {
        return new SplashScreen(this);
    }

}
