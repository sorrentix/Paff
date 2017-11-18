package com.paff.orlandale.paff;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ALESSANDROSERRAPICA on 10/11/2017.
 */

public class Settings {
    public static boolean sounds;
    public static boolean music;
    public static int highscore;
    private static Context cont;

    public Settings() {
    }

    public static void setSounds(boolean flag){
        sounds=flag;
        save("sounds",flag);
    }
    public static void setMusic(boolean flag){
        music=flag;
        save("music",flag);
    }
    public static void newHighscore(int h){
        highscore = h;
        SharedPreferences prefs = cont.getSharedPreferences("PAFF_SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("highscore",highscore);
        editor.commit();
    }

    public static void save(String s, boolean flag){
        SharedPreferences prefs = cont.getSharedPreferences("PAFF_SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(s,flag);
        editor.commit();
    }

    public static void load(Context c){
        cont = c;
        SharedPreferences prefs = cont.getSharedPreferences("PAFF_SETTINGS", Context.MODE_PRIVATE);
        sounds= prefs.getBoolean("sounds",true);
        music = prefs.getBoolean("music", true);
        highscore = prefs.getInt("highscore",0);
    }



}
