package com.paff.orlandale.paff;

/**
 * Created by Yoshi on 17/11/2017.
 */


/**
 * Classe enumerata che consente di gestire gli stati del gioco,
 * sia per gli screen che per il mondo fisico.
 */
public enum GameState {
    BASIC_LOADING,
    COMPLETE_LOADING,
    COMPLETE_ANIMATION,

    SOUND_ON,
    SOUND_OFF,
    MUSIC_ON,
    MUSIC_OFF,

    PLAY,
    HELP,
    SETTINGS,
    WAITING,

    SHOT,
    ROTATE,
    JOINT,

    PAUSED,
    GAME_OVER,
    SETUP;

}
