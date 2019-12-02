package com.adarsh.adarshmeditationapp.meditation

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.adarsh.adarshmeditationapp.constants.FIREBASE_MODEL
import com.adarsh.adarshmeditationapp.data.FirebaseModel
import com.adarsh.adarshmeditationapp.extensions.invisible
import com.adarsh.adarshmeditationapp.extensions.loadImage
import com.adarsh.adarshmeditationapp.extensions.visible
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_meditation.*
import javax.inject.Inject
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory



class MeditationActivity : DaggerAppCompatActivity(), Player.EventListener {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var firebaseModel: FirebaseModel
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition = 0L

    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }
    private val adaptiveTrackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.adarsh.adarshmeditationapp.R.layout.activity_meditation)

    }

    override fun onStart() {
        super.onStart()

        val intent = intent

        if (intent.hasExtra(FIREBASE_MODEL)) {
            firebaseModel = intent.getSerializableExtra(FIREBASE_MODEL) as FirebaseModel
        }

        firebaseModel.musicModel?.imageLink?.let { ivMusic.loadImage(it) }
    }

    override fun onResume() {
        super.onResume()
        firebaseModel.musicModel?.link?.let { initializeExoplayer(it) }
    }

    override fun onStop() {
        releaseExoplayer()
        super.onStop()
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            progressBar.visible()
        else if (playbackState == Player.STATE_READY)
            progressBar.invisible()
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

    }

    override fun onSeekProcessed() {

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    private fun initializeExoplayer(url: String) {
        simpleExoplayer = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(this),
            DefaultTrackSelector(adaptiveTrackSelectionFactory),
            DefaultLoadControl()
        )

        prepareExoplayer(url)
        simpleExoPlayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
    }

    private fun releaseExoplayer() {
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory =
            DefaultDataSourceFactory(this, getUserAgent(this, getString(com.adarsh.adarshmeditationapp.R.string.app_name)), null)
        return ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    private fun prepareExoplayer(url: String) {
        val uri = Uri.parse(url)
        val mediaSource = buildMediaSource(uri)
        simpleExoplayer.prepare(mediaSource)
    }
}
