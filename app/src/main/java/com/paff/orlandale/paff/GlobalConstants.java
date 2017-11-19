package com.paff.orlandale.paff;

import com.google.fpl.liquidfun.Vec2;

/**
 * Created by Yoshi on 17/11/2017.
 */

public class GlobalConstants {
    public static final int FRAME_BUFFER_WIDTH = 1080;
    public static final int FRAME_BUFFER_HEIGHT = 1920;
    public static final float FPS = 1.0f/60.0f;

    public static final float VOLUME = 1.0f;

    public static final Vec2 GRAVITY = new Vec2(0,9.81f);
    public static final float PAFF_RADIUS = 1.0f;
    public static final float BUBBLE_BASIC_RADIUS = 1.8f;
    public static final int BUBBLE_NUMBER = 5;


    public class Physics {
        public static final float X_MIN = -9.0f;
        public static final float X_MAX = 9.0f;
        public static final float Y_MIN = -16.0f;
        public static final float Y_MAX = 16.0f;
    }

    public class Colors {
        public static final int BLUE = 0x3498db;
        public static final int RED = 0xe74c3c;
    }
    public static final int ALPHA = 255;

}


