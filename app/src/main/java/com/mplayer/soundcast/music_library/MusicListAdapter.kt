package com.mplayer.soundcast.music_library

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mplayer.soundcast.R
import com.mplayer.soundcast.listeners.IFragmentChangeListener
import com.mplayer.soundcast.listeners.IMusicPlayerActionListener
import com.mplayer.soundcast.music_library.model.Song
import com.mplayer.soundcast.music_player.MusicPlayerFragment
import com.mplayer.soundcast.utils.Constants
import kotlinx.android.synthetic.main.itemview_musiclist.view.*

class MusicListAdapter(var musicList: List<Song>, var context: Context) :RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {

    private var fragmentChangeListener: IFragmentChangeListener = context as IFragmentChangeListener
    private var musicPlayerActionListener: IMusicPlayerActionListener = context as IMusicPlayerActionListener


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.itemview_musiclist, p0, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = musicList[position]

        holder.mTitle.text = song.title

        Glide.with(context)
            .load(song.thumbnail)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.mThumbnail)



        holder.itemView.setOnClickListener {

            val arg = Bundle()
            arg.putInt(MusicPlayerFragment.ARG_PARAM3,position)
            arg.putString(MusicPlayerFragment.ARG_PARAM1,song.title)
            arg.putString(MusicPlayerFragment.ARG_PARAM2,song.thumbnail)


            fragmentChangeListener.push(Constants.FRAGMENT_MUSIC_PLAYER,arg)
            musicPlayerActionListener.play(position)
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTitle: TextView = itemView.tvTitle
        var mThumbnail: ImageView = itemView.ivThumbnail
    }
}