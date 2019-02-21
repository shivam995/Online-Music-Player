package com.mplayer.soundcast.music_player

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import okhttp3.internal.http2.ErrorCode

class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {


    private var shouldShowProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var errorLiveData: MutableLiveData<Pair<ErrorCode, String>> = MutableLiveData()
    private var isMusicPlayingLiveData: MutableLiveData<Boolean> = MutableLiveData()



    fun getShouldShowProgressLiveData(): MutableLiveData<Boolean> {
        return shouldShowProgressLiveData
    }

    fun getErrorLiveData(): MutableLiveData<Pair<ErrorCode, String>> {
        return errorLiveData
    }




}






