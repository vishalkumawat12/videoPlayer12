package com.example.videoplayers2.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.lifecycle.ViewModelProvider
import com.example.videoplayers2.R
import com.example.videoplayers2.databinding.ActivityPlayer2Binding
import com.example.videoplayers2.databinding.CustomPlaybackViewBinding
import com.example.videoplayers2.viewmodels.PlayerVideModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class PlayerActivity2 : AppCompatActivity(), View.OnClickListener {

    private var position = -1;
    lateinit var exoPlayer: ExoPlayer
    lateinit var playerView: PlayerView

    lateinit var concatenatingMediaSource: ConcatenatingMediaSource


    lateinit var binding: CustomPlaybackViewBinding
    lateinit var viewModel: PlayerVideModel

    lateinit var bindingPlayer: ActivityPlayer2Binding

    lateinit var back: ImageFilterView
    lateinit var title: TextView
    lateinit var playlist: ImageFilterView
    lateinit var more: ImageFilterView
    lateinit var currentPosition: TextView
    lateinit var seekbar: DefaultTimeBar
    lateinit var duration: TextView
    lateinit var lock: ImageFilterView
    lateinit var replay: ImageFilterView
    lateinit var forward: ImageFilterView
    lateinit var previous: ImageFilterView
    lateinit var next: ImageFilterView
    lateinit var play: ImageFilterView
    lateinit var scale: ImageFilterView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPlayer = ActivityPlayer2Binding.inflate(layoutInflater)
        fullScreen()
        setContentView(bindingPlayer.root)
        supportActionBar?.hide()

        initialize()

        val bundle = intent.extras
        if (bundle != null) {
            viewModel.getData(bundle)
        }

        playVideo()
        next.setOnClickListener(this)
        play.setOnClickListener(this)
        previous.setOnClickListener(this)

        play.setOnClickListener {
            Log.d("TAG", "onCreate: play.setOnClickListener")
//            if(exoPlayer.isPlaying){
//                play.setAltImageResource(R.drawable.round_pause_24)
//                exoPlayer.pause()
//            }else{
//                play.setAltImageResource(R.drawable.round_play_arrow_24)
//                exoPlayer.play()
//            }
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initialize() {

        playerView = bindingPlayer.Exoplayer2
        exoPlayer = ExoPlayer.Builder(application).build()

        binding = CustomPlaybackViewBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(PlayerVideModel::class.java)

        back = binding.ivBack
        title = binding.tvTitle
        playlist = binding.ivPlaylist
        more = binding.ivMore
        currentPosition = binding.tvPosition
        seekbar = binding.dtbProgressBar
        duration = binding.tvDuration
        lock = binding.ivUnlock
        replay = binding.ivReplay
        forward = binding.ivForward
        previous = binding.ivForward
        next = binding.ivNext
        play = binding.ivPlay
        scale = binding.ivScale

    }

    private fun playVideo() {

        position = viewModel.getPosition()

        val currentVideo = viewModel.getVideoList()[position]
        setTitle(currentVideo.title)

        viewModel.generateVideoUriList()
        val mediaSourceList = generateMediaSource(viewModel.getUriList())


        playerView.player = exoPlayer
        playerView.keepScreenOn = true
//        exoPlayer.prepare(mediaSourceList[position])
        exoPlayer.prepare(concatenatingMediaSource)
        exoPlayer.playWhenReady = true
        exoPlayer.seekTo(viewModel.getPosition(), C.TIME_UNSET)
        playError()
    }

    private fun playError() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Toast.makeText(this@PlayerActivity2, "Video Player Error ", Toast.LENGTH_SHORT)
                    .show()

            }
        })
        exoPlayer.playWhenReady = true
    }

    private fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    private fun generateMediaSource(uriList: ArrayList<Uri>): ArrayList<MediaSource> {
        val list = ArrayList<MediaSource>()
        concatenatingMediaSource = ConcatenatingMediaSource()
        uriList.forEach {
            val mediaItem = MediaItem.Builder().setUri(it).build()
            val factory: DataSource.Factory =
                DefaultDataSourceFactory(this, Util.getUserAgent(this, "My App name"))
            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource =
                ProgressiveMediaSource.Factory(factory, extractorsFactory)
                    .createMediaSource(mediaItem)
            list.add(mediaSource)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return list
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        if (exoPlayer.isPlaying) {
            exoPlayer.stop()
        }
    }


    override fun onPause() {
        super.onPause()

        exoPlayer.playWhenReady = false
        exoPlayer.playbackState

    }

    override fun onResume() {
        super.onResume()
        exoPlayer.playWhenReady = true
        exoPlayer.playbackState

    }

    override fun onRestart() {
        super.onRestart()
        exoPlayer.playWhenReady = true
        exoPlayer.playbackState
    }

    fun fullScreen() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

    override fun onClick(v: View?) {
        Log.d("TAG", "onClick: ")
        when (v?.getId()) {
            R.id.iv_next -> {
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            }

            R.id.iv_play -> {
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            }

            R.id.iv_previous -> {
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }


}