package com.paff.orlandale.paff;

/**
 * Created by sorrentix on 13/11/2017.
 */

public class PhysicToPixel {

    public static Box physicalSize;
    public static float framebufferWidth;
    public static float framebufferHeight;

    public static int X(float x) {
        return (int)((x-physicalSize.xmin)/physicalSize.width*framebufferWidth);
    }

    public static int Y(float y) {
        return (int)((-1*((y-physicalSize.ymin)/physicalSize.height*framebufferHeight))+framebufferHeight);
    }

    public static int XLength(float x) {
        return (int)(x/physicalSize.width*framebufferWidth);
    }

    public static int YLength(float y) {
        return (int)(y/physicalSize.height*framebufferHeight);
    }


}
