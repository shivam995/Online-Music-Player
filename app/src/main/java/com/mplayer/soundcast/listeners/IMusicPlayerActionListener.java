package com.mplayer.soundcast.listeners;

public interface IMusicPlayerActionListener {


    void play(int position);
    void pause();
    void resume(Integer seekTo);
}
