package com.mplayer.soundcast.music_library

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.mplayer.soundcast.music_library.model.MusicLibraryDao
import com.mplayer.soundcast.music_library.model.Song
import com.mplayer.soundcast.restclient.RestClient
import okhttp3.internal.http2.ErrorCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MusicLibraryViewModel(application: Application) : AndroidViewModel(application) {


    private var musicLibraryLiveData: MutableLiveData<ArrayList<Song>> = MutableLiveData()
    private var shouldShowProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var errorLiveData: MutableLiveData<Pair<ErrorCode, String>> = MutableLiveData()


    fun getMusicLibraryLiveData(): MutableLiveData<ArrayList<Song>> {
        return musicLibraryLiveData
    }

    fun getShouldShowProgressLiveData(): MutableLiveData<Boolean> {
        return shouldShowProgressLiveData
    }

    fun getErrorLiveData(): MutableLiveData<Pair<ErrorCode, String>> {
        return errorLiveData
    }


    public fun getMusicLibrary() {
        shouldShowProgressLiveData.postValue(true)
        RestClient.getClient().getMusicLibrary()
            .enqueue(object : Callback<MusicLibraryDao> {
                override fun onFailure(call: Call<MusicLibraryDao>, t: Throwable) {
                    t.printStackTrace()

                    shouldShowProgressLiveData.postValue(false)
                    errorLiveData.postValue(Pair(ErrorCode.CONNECT_ERROR, t.message!!))
                }

                override fun onResponse(call: Call<MusicLibraryDao>, response: Response<MusicLibraryDao>) {
                    shouldShowProgressLiveData.postValue(false)

                    var songList = response.body()?.result!!
                    verifyList(songList)

                    musicLibraryLiveData.postValue(songList)
                }

            })


    }


    private fun verifyList(musicList: MutableList<Song>) {
        val each = musicList.iterator()
        while (each.hasNext()) {
            if (each.next().music_file == null) {
                each.remove()
            }
        }
//        var length = musicList.size-1
//        for(index in 0..length ){
//            val song = musicList[index]
//            if (song.music_file == null) {
//                musicList.removeAt(index)
//                length -= 1
//            }
//        }

    }


}






