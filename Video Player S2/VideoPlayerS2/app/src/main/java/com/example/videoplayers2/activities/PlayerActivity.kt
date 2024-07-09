package com.example.videoplayers2.activities

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.videoplayers2.databinding.ActivityPlayerBinding
import com.example.videoplayers2.models.VideoModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.io.File

class PlayerActivity : AppCompatActivity() {
    var binding: ActivityPlayerBinding? = null
    var builder: AlertDialog.Builder? = null
    var playerView: PlayerView? = null
    var exoPlayer: ExoPlayer? = null
    var videoList = ArrayList<VideoModel>()

    //    var videoUriList = ArrayList<Uri>()
    var position = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()


//        supportActionBar!!.hide()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        Log.d("TAG", "onCreate: Created")

        playerView = binding!!.Exoplayer

        val bundle = intent.extras
        if (bundle != null) {
            position = bundle.getInt("position")
            val list = bundle.getParcelableArrayList<VideoModel>("videoList")
            if (list != null) {
                Log.d("TAG", "onCreate: list size = ${list.size}")
                videoList = list
            }
        }
        Log.d("TAG", "onCreate: myFile $videoList")


//        val list = viewModel.getVideoArrayList().value
//        Log.d("TAG", "onCreate: list $list")

//        if(list != null){
//            myFile
//        }

//        myFile = if (viewModel.folderSending) (
//                viewModel.getVideoArrayList().value!!
//    //            foldervideoModelArrayList1
//        ) else {
//            viewModel.getVideoArrayList().value!!
////            videoModelArrayList
//        }
        Log.d("TAG", "onCreate: position $position ")
        Log.d("TAG", "onCreate: myFile $videoList ")

        try {
            val path = videoList[position].path
            Log.d("TAG", "onCreate: $path")
            val mRetriever = MediaMetadataRetriever()
            mRetriever.setDataSource(path)
            val frame = mRetriever.frameAtTime
            val width = frame!!.width
            val height = frame.height
            val it = width.toFloat() / height
            if (1.2 < it) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            val uriList = generateMediaUris(videoList)
            if (uriList[position] != null) {

//                val videoUris = listOf(
//                    Uri.parse("https://example.com/video1.mp4"),
//                    Uri.parse("https://example.com/video2.mp4")
//                )

//                val uri = uriList[position]
//                val uri = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
//                simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
                exoPlayer = ExoPlayer.Builder(this).build()


                val mediaSourceList = generateMediaSource(uriList)

                playerView!!.player = exoPlayer
                playerView!!.keepScreenOn = true


//                exoPlayer!!.addMediaSources(mediaSourceList)

                exoPlayer!!.prepare(mediaSourceList.get(position))
//                exoPlayer!!.seekTo(position.toLong())

//                exoPlayer!!.setMediaSource(mediaSourceList[position])
//                exoPlayer!!.seekTo(3,0)//8730826454

                exoPlayer!!.playWhenReady = true
                Log.d("TAG", "onCreate: playWhenReady called")
//                exoPlayer!!.play()

//                exoPlayer!!.next


//                exoPlayer!!.addListener(object : Player.Listener {
//                    override fun
//                })
//                exoPlayer!!.next()

//                exoPlayer!!.previous()



            }
        } catch (e: Exception) {
            Toast.makeText(this@PlayerActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            setError()
        }
    }

    private fun generateMediaSource(uriList: ArrayList<Uri>): ArrayList<MediaSource> {
        val list = ArrayList<MediaSource>()

        uriList.forEach {
            val mediaItem = MediaItem.Builder().setUri(it).build()
            val factory: DataSource.Factory =
                DefaultDataSourceFactory(this, Util.getUserAgent(this, "My App name"))
            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource =
                ProgressiveMediaSource.Factory(factory, extractorsFactory)
                    .createMediaSource(mediaItem)
            list.add(mediaSource)
        }
        return list
    }

    private fun generateMediaUris(videoList: ArrayList<VideoModel>): ArrayList<Uri> {
        val list = ArrayList<Uri>()
        videoList.forEach {
            val uri = Uri.fromFile(File(it.path))
            list.add(uri)
        }
        return list
    }
//    fun playNextSong() {
//        // Get the current position of the song.
//        val currentPosition = exoPlayer?.currentPosition ?: 0
//
//        // Get the number of songs in the list.
//        val numberOfSongs = myFile.size
//
//        // Calculate the next position.
//        val nextPosition = currentPosition + 1
//
//        // Check if the next position is within the list of songs.
//        if (nextPosition < numberOfSongs) {
//            // Set the next position to the ExoPlayer object.
//            exoPlayer?.seekTo(nextPosition)
//        } else {
//            // The next position is out of bounds, so set it to the first song.
//            exoPlayer?.seekTo(0)
//        }
//    }
//
//    fun playPreviousSong() {
//        // Get the current position of the song.
//        val currentPosition = exoPlayer?.currentPosition ?: 0
//
//        // Get the number of songs in the list.
//        val numberOfSongs = myFile.size
//
//        // Calculate the previous position.
//        val previousPosition = currentPosition - 1
//
//        // Check if the previous position is within the list of songs.
//        if (previousPosition >= 0) {
//            // Set the previous position to the ExoPlayer object.
//            exoPlayer?.seekTo(previousPosition)
//        } else {
//            // The previous position is out of bounds, so set it to the last song.
//            exoPlayer?.seekTo((numberOfSongs - 1).toLong())
//        }
//    }

    private fun setFullScreen() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun setError() {
        builder = AlertDialog.Builder(this@PlayerActivity)
        builder!!.setTitle("Error")
        builder!!.setMessage("Something went wrong !!!")
        builder!!.setCancelable(false)
        builder!!
            .setPositiveButton(
                "ok"
            ) { dialog, which -> // When the user click yes button
                // then app will close
                finish()
            }
        builder!!.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        exoPlayer!!.stop()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer!!.stop()
    }
}
