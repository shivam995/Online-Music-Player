package com.mplayer.soundcast.music_library.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class MusicLibraryDao : Serializable {


    @SerializedName("results")
    @Expose
    var result: ArrayList<Song>? = null



}