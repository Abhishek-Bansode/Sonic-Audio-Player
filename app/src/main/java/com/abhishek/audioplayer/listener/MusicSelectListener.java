package com.abhishek.audioplayer.listener;

import com.abhishek.audioplayer.model.Music;

import java.util.List;

public interface MusicSelectListener {
    void playQueue(List<Music> musicList);

    void setShuffleMode(boolean mode);
}