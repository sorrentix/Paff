package com.paff.orlandale.paff;

/**
 * Created by sorrentix on 13/11/2017.
 */

/**
 * Conveniece class per la conversione dal sistema di riferimento fisico a quello pixel
 */
public class PhysicToPixel {

    public static Box physicalSize;
    public static float framebufferWidth;
    public static float framebufferHeight;

    public static float X(float x) {
        return (x-physicalSize.xmin)/physicalSize.width*framebufferWidth;
    }

    public static float Y(float y) {
        return (y-physicalSize.ymin)/physicalSize.height*framebufferHeight;
    }

    public static float XLength(float x) {
        return (x/physicalSize.width*framebufferWidth);
    }

    public static float YLength(float y) {
        return (y/physicalSize.height*framebufferHeight);
    }

    public static float Length(float r){
        float xr = XLength(r);
        float yr = YLength(r);
        return (float)Math.sqrt((xr*xr)+(yr*yr));
    }

}
