package com.mplayer.soundcast.music_player


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.mplayer.soundcast.R
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_player.*


class MusicPlayerFragment : Fragment() {

    private var songTitle = ""
    private var songImageLogoUrl = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            Log.i("bundle data", Gson().toJson(arguments))
            arguments!!.let {
                songTitle = it.getString(ARG_PARAM1)
                songImageLogoUrl = it.getString(ARG_PARAM2)
            }
        }
    }

    fun updateSongDetails(title: String, logoUrl: String) {
        songTitle = title
        songImageLogoUrl = logoUrl

        if (isAdded) {

            setData()
        }
    }

    private fun setData() {
        tvTitle.text = songTitle

        Glide.with(context!!).load(songImageLogoUrl)
            .into(ivLogo)

        val myOptions = RequestOptions()
            .transform(BlurTransformation(10,1))
            .override(35, 35)

        Glide.with(context!!).load(songImageLogoUrl)
            .apply(myOptions)

            .into(flLogoBackground)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_player, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        tvTitle.text = songTitle
//
//        Glide.with(context!!).load(songImageLogoUrl)
//            .into(ivLogo)

        setData()

    }


    companion object {

        const val ARG_PARAM1 = "song_position"
        const val ARG_PARAM2 = "title"
        const val ARG_PARAM3 = "logo_url"


        fun newInstance(position: Int, title: String, url: String): MusicPlayerFragment {
            val arg = Bundle()
            arg.putString(ARG_PARAM1, title)
            arg.putString(ARG_PARAM2, url)
            arg.putInt(ARG_PARAM3, position)
            val fragment = MusicPlayerFragment()
            fragment.arguments = arg

            return fragment
        }

        fun newInstance(arg: Bundle?): MusicPlayerFragment {
            val fragment = MusicPlayerFragment()
            fragment.arguments = arg

            return fragment
        }
    }

}
