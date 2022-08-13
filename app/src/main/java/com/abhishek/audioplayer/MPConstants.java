package com.abhishek.audioplayer;

import com.abhishek.audioplayer.listener.MusicSelectListener;

public class MPConstants {
    public static final String PACKAGE_NAME = "com.abhishek.audioplayer";

    public static final int PERMISSION_READ_STORAGE = 1009;

    public static final String MEDIA_SESSION_TAG = "com.abhishek.audioplayer.MediaSession";

    public static final int NOTIFICATION_ID = 101;
    public static final String PLAY_PAUSE_ACTION = "com.abhishek.audioplayer.PLAYPAUSE";
    public static final String NEXT_ACTION = "com.abhishek.audioplayer.NEXT";
    public static final String PREV_ACTION = "com.abhishek.audioplayer.PREV";
    public static final String CLOSE_ACTION = "com.abhishek.audioplayer.CLOSE";
    public static final String CHANNEL_ID = "com.abhishek.audioplayer.CHANNEL_ID";
    public static final int REQUEST_CODE = 100;

    public static final float VOLUME_DUCK = 0.2f;
    public static final float VOLUME_NORMAL = 1.0f;
    public static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    public static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    public static final int AUDIO_FOCUSED = 2;

    public static final int[] TAB_ICONS = new int[]{
            R.drawable.ic_music_note,
            R.drawable.ic_settings,
    };
    public static final String SETTINGS_THEME = "shared_pref_theme";
    public static final String SETTINGS_ALBUM_REQUEST = "shared_pref_album_request";
    public static final String SETTINGS_THEME_MODE = "shared_pref_theme_mode";
    public static final String SETTINGS_EXCLUDED_FOLDER = "shared_pref_excluded_folders";

    public static final int DATABASE_VERSION = 1;
    public static final String MUSIC_TABLE = "music";
    public static final String DATABASE_NAME = "playlist";
    public static MusicSelectListener musicSelectListener;
}
