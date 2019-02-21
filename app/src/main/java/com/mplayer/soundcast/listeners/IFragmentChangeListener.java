package com.mplayer.soundcast.listeners;

import android.os.Bundle;

public interface IFragmentChangeListener {


    void push(Integer fragmentId, Bundle arg);

    void pop();
}
