package com.mplayer.soundcast.music_library


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mplayer.soundcast.R
import com.mplayer.soundcast.music_library.model.Song
import kotlinx.android.synthetic.main.fragment_musiclibrary.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class MusicLibraryFragment : Fragment() {


    private var musicListAdapter: MusicListAdapter? = null
    private var musicLibraryViewModel: MusicLibraryViewModel? = null
    private var mMusicList: MutableList<Song> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: get arguments here
        if (arguments != null) {
            arguments.let {
                mMusicList = it?.getSerializable("list") as ArrayList<Song>
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_musiclibrary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()

//        observeDataChange()
    }


//    private fun observeDataChange() {
//        musicLibraryViewModel?.getMusicLibraryLiveData()?.observe(this,
//            Observer<MutableList<Song>> { list ->
//                if (list != null && list.isNotEmpty()) {
//                    mMusicList.clear()
//                    mMusicList.addAll(list)
//
//                    musicListAdapter?.notifyDataSetChanged()
//                }
//            })
//
//
//
//        musicLibraryViewModel?.getShouldShowProgressLiveData()?.observe(this, Observer<Boolean> { shouldShow ->
//            if (shouldShow!!)
//                progressBar.visibility = View.VISIBLE
//            else
//                progressBar.visibility = View.GONE
//        })
//    }


    private fun initializeViews() {
        musicListAdapter = MusicListAdapter(mMusicList, context!!)
        rvMusicList.adapter = musicListAdapter
        rvMusicList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvMusicList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


//        musicLibraryViewModel = ViewModelProviders.of(this).get(MusicLibraryViewModel::class.java)
//        musicLibraryViewModel?.getMusicLibrary()
    }


    companion object {

        fun newInstance(arg: Bundle?): MusicLibraryFragment {
            val fragment = MusicLibraryFragment()
            fragment.arguments = arg

            return fragment
        }
    }

}
