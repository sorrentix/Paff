package com.paff.orlandale.paff;

import android.graphics.Typeface;

import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Sound;


/**
 * Created by sorrentix on 08/11/2017.
 */
/**
 * Convenience class usata per mantere un riferimento a tutti gli asset
 * necessari al corretto svolgimento del gioco.
 * Tipi di asset sono: Sound, Music, Pixmap, Typeface
 */
public class Assets {
    /*AUDIO*/
    public static Sound bubblexplosion;
    public static Music gamesoundtheme;
    public static Music splashsound;
    public static Music gamemenusoundtheme;
    public static Sound countdown;
    public static Sound highscore_gameover;
    public static Sound newhighscore;
    public static Sound fall_gameover;
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
    public static Pixmap scoreText;
    public static Pixmap count3;
    public static Pixmap count2;
    public static Pixmap count1;
    public static Pixmap countJump;
    
    //Font
    public static Typeface font;

    public static boolean flagReady=false;

    public static boolean areReady(){
        return flagReady;
    }
}
