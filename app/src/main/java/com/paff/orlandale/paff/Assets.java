package com.paff.orlandale.paff;

import android.graphics.Typeface;

import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;


/**
 * Created by sorrentix on 08/11/2017.
 */

public class Assets {
    /*AUDIO*/
    public static Sound bubblexplosion;
    public static Music gamesoundtheme;
    public static Music splashsound;
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
    public static Pixmap btn_resume;
    public static Pixmap btn_replay;
    public static Pixmap btn_exit;
    public static Pixmap sounds_text;
    public static Pixmap music_text;
    public static Pixmap pause_text;
    public static Pixmap gameover_text;
    public static Pixmap help_screen;
    public static Pixmap highscore;
    public static Pixmap score;
    public static Pixmap count3;
    //Font
    public static Typeface font;

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
