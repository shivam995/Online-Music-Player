package com.mplayer.soundcast


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.mplayer.soundcast.listeners.IFragmentChangeListener
import com.mplayer.soundcast.listeners.IMusicPlayerActionListener
import com.mplayer.soundcast.music_library.MusicLibraryFragment
import com.mplayer.soundcast.music_library.MusicLibraryViewModel
import com.mplayer.soundcast.music_library.model.Song
import com.mplayer.soundcast.music_player.MusicPlayerFragment
import com.mplayer.soundcast.utils.Constants.FRAGMENT_MUSIC_LIBRARY
import com.mplayer.soundcast.utils.Constants.FRAGMENT_MUSIC_PLAYER
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.itemview_bottom_control.*
import okhttp3.internal.http2.ErrorCode
import java.util.*


class MainActivity : AppCompatActivity(), IFragmentChangeListener, IMusicPlayerActionListener, View.OnClickListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var musicLibraryViewModel: MusicLibraryViewModel? = null
    private var mMusicList: ArrayList<Song> = ArrayList()
    private var currentSongPosition = 0
    private var mediaPlayerCurrentSeekPosition = 0
    private var isMusicPlaying: Boolean = false
    private var musicLibraryFragment: MusicLibraryFragment? = null
    private var musicPlayerFragment: MusicPlayerFragment? = null
    private val mSeekbarUpdateHandler = Handler()
    private val mUpdateSeekbar = object : Runnable {
        override fun run() {
            progressSeekbar.progress = mMediaPlayer?.currentPosition!!
            mSeekbarUpdateHandler.postDelayed(this, 50)
        }
    }


    override fun push(fragmentId: Int?, arg: Bundle?) {
        var fragment: Fragment? = null
        var tag = ""
        when (fragmentId) {
            FRAGMENT_MUSIC_LIBRARY -> {
                tag = "music_library"
                musicLibraryFragment = MusicLibraryFragment.newInstance(arg)
                fragment = musicLibraryFragment

            }

            FRAGMENT_MUSIC_PLAYER -> {
                tag = "music_player"
                musicPlayerFragment = MusicPlayerFragment.newInstance(arg)
                fragment = musicPlayerFragment
            }
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction
            .replace(R.id.frameContainer, fragment!!, tag)
            .addToBackStack(tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .commit()

    }


    override fun pop() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            finish()
    }


    override fun resume(seekTo: Int?) {

        mMediaPlayer?.seekTo(seekTo!!)
        mMediaPlayer?.start()
    }


    override fun play(songPosition: Int) {

        if (songPosition < mMusicList.size - 1 && songPosition > 0) {
            currentSongPosition = songPosition

            if (!TextUtils.isEmpty(mMusicList[currentSongPosition].music_file.url)) {
                currentSongPosition = songPosition

                progressSeekbar.progress = 0
                musicLibraryViewModel?.getShouldShowProgressLiveData()?.postValue(true)
                mMediaPlayer?.stop()
                mMediaPlayer?.reset()
                mMediaPlayer?.setDataSource(mMusicList[currentSongPosition].music_file.url)
                mMediaPlayer?.prepareAsync()

            } else {
                Toast.makeText(this, "Song url not valid", Toast.LENGTH_LONG).show()
            }

        }
    }


    private fun showPlayerControllerView(showControllerView: Boolean) {
        if (showControllerView) {
            bottom_control.visibility = View.VISIBLE
        }
    }


    override fun pause() {
        musicLibraryViewModel?.getShouldShowProgressLiveData()?.postValue(false)
        if (mMediaPlayer != null) {
            mMediaPlayer?.pause()
            mediaPlayerCurrentSeekPosition = mMediaPlayer?.currentPosition!!
            isMusicPlaying = false
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)
            btnPlayPause.setImageDrawable(resources.getDrawable(R.drawable.ic_play_arrow_white_24dp))
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        musicLibraryViewModel = ViewModelProviders.of(this).get(MusicLibraryViewModel::class.java)

        musicLibraryViewModel?.getMusicLibrary()

        observeDataChange()


        initializeMediaPlayer()

        btnPlayPause.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        btnPrevious.setOnClickListener(this)


        progressSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    mMediaPlayer?.seekTo(progress)
            }
        })


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnNext -> {
                if (currentSongPosition < mMusicList.size - 1) {
                    currentSongPosition += 1
                    play(currentSongPosition)
                }
            }

            R.id.btnPlayPause -> {
                if (isMusicPlaying)
                    pause()
                else {
                    mMediaPlayer?.start()
                    isMusicPlaying = true
                    mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0)
                    btnPlayPause.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_white_24dp))
                }
            }

            R.id.btnPrevious -> {
                if (currentSongPosition > 0) {
                    currentSongPosition -= 1
                    play(currentSongPosition)
                }
            }
        }

    }


    private fun initializeMediaPlayer() {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)


        mMediaPlayer?.setOnCompletionListener { mp ->
            currentSongPosition += 1
            play(currentSongPosition)
        }


        mMediaPlayer?.setOnPreparedListener {
            musicPlayerFragment?.updateSongDetails(
                mMusicList[currentSongPosition].title,
                mMusicList[currentSongPosition].thumbnail
            )
            musicLibraryViewModel?.getShouldShowProgressLiveData()?.postValue(false)
            progressSeekbar.max = it.duration
            mMediaPlayer?.start()
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0)
            isMusicPlaying = true
            showPlayerControllerView(true)
            btnPlayPause.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_white_24dp))

        }
    }


    private fun observeDataChange() {
        musicLibraryViewModel?.getMusicLibraryLiveData()?.observe(this,
            Observer<ArrayList<Song>> { list ->
                if (list != null && list.isNotEmpty()) {
                    mMusicList = list

                    val arg = Bundle()
                    arg.putSerializable("list", mMusicList)
                    push(FRAGMENT_MUSIC_LIBRARY, arg)


                    mMediaPlayer?.setDataSource(mMusicList[0].music_file.url)

                }
            })



        musicLibraryViewModel?.getShouldShowProgressLiveData()?.observe(this, Observer<Boolean> { shouldShow ->
            if (shouldShow!!)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        })


        musicLibraryViewModel?.getErrorLiveData()?.observe(this,Observer<Pair<ErrorCode,String>>{pair: Pair<ErrorCode, String>? ->
            Snackbar.make(parentView,pair?.second!!,Snackbar.LENGTH_LONG).show()
        })
    }


    override fun onBackPressed() {
        pop()
    }


    override fun onDestroy() {
        super.onDestroy()

        if (mMediaPlayer != null) {
            if (mMediaPlayer?.isPlaying!!) {

                mMediaPlayer?.stop()
                mMediaPlayer?.release()
                mMediaPlayer = null

            }
        }
    }

}
