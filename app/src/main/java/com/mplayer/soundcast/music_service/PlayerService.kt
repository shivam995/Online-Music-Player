package com.mplayer.soundcast.music_service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class PlayerService : Service(), MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {




    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {


        return false
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
    }

}