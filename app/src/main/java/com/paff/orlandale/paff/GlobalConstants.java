package com.paff.orlandale.paff;

import com.google.fpl.liquidfun.Vec2;

/**
 * Created by Yoshi on 17/11/2017.
 */

public class GlobalConstants {
    public static final int FRAME_BUFFER_WIDTH = 1080;
    public static final int FRAME_BUFFER_HEIGHT = 1920;
    public static final float FPS = 1.0f/60.0f;//1.0f/60.0f;

    public static final float VOLUME = 1.0f;

    public static final Vec2 GRAVITY = new Vec2(0,9.81f);
    public static final float PAFF_RADIUS = 0.5f;
    public static final float BUBBLE_BASIC_RADIUS = 0.95f;
    public static final float BUBBLE_VARIATION_RADIUS = 0.5f;
    public static final int BUBBLE_NUMBER = 12;

    public static final float SPEEDUP_TIME = 5.0f;

    public class Physics {
        public static final float X_MIN = -9.0f;
        public static final float X_MAX = 9.0f;
        public static final float Y_MIN = -16.0f;
        public static final float Y_MAX = 16.0f;
    }

    public class Colors {
        public static final int BLUE = 0x3498db;
        public static final int RED = 0xe74c3c;
        public static final int BLACK = 0x181818;
        public static final int GREY = 0x34495e;
        public static final int RED_DARK = 0xc0392b;
        public static final int ORANGE = 0xf39c12;
        public static final int WHITE = 0xffffff;
        public static final int GREEN = 0x2ecc71;
        public static final int GREEN_LIGHT = 0x00fc00;
    }
    public static final int ALPHA = 255;

}


