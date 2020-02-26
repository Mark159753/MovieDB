package com.example.moviedb.ui.youTube

import android.os.Bundle
import com.example.moviedb.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube.*

class YoutubeActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        val arguments = intent.extras
        val videoID = arguments?.getString(VIDEO_ID)

        player.initialize(YouTube_API, object :YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                youTubePlayer: YouTubePlayer?,
                b: Boolean
            ) {
                videoID?.let {
                    youTubePlayer?.setShowFullscreenButton(false)
                    youTubePlayer?.setFullscreen(true)
                    youTubePlayer?.loadVideo(it)
                }
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                youTubeInitializationResult: YouTubeInitializationResult?
            ) {
                onBackPressed()
            }
        })
    }

    companion object{
        private const val YouTube_API = "AIzaSyBSax85ikdaYtwL_yBsXD39N2RQZCBthqg"
        const val VIDEO_ID = "com.example.moviedb.ui.youTube.VIDEO_ID"
    }
}
