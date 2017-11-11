package com.paff.orlandale.paff;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sorrentix on 08/11/2017.
 */

public class Assets {
    /*AUDIO*/
    public static Sound bubblexplosion;
    public static Sound gamesoundtheme;
    public static Sound splashsound;
    /*GRAFICHE*/
    public static Pixmap logo;
    public static Pixmap menu_background;
    public static Pixmap btn_play;
    public static Pixmap btn_play_click;
    public static Pixmap btn_settings;
    public static Pixmap btn_settings_click;
    public static Pixmap btn_help;
    public static Pixmap btn_help_click;
    public static Pixmap btn_on;
    public static Pixmap btn_off;
    public static Pixmap sounds_text;
    public static Pixmap music_text;
    public static Pixmap help_screen;

    public static boolean flagReady=false;

    public static boolean areReady(){
        return flagReady;
    }

    /*
    public static boolean hasNullElement(){
        Field[] allFields = Assets.class.getDeclaredFields();
        for (Field field : allFields) {
            try {
                if (field.get(null) == null)
                    return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }*/

}
