package com.akmal.androidtasklessons.ui.video

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.akmal.androidtasklessons.R
import com.akmal.androidtasklessons.databinding.FragmentVideoBinding
import com.akmal.androidtasklessons.utils.Constants
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class VideoFragment : Fragment(R.layout.fragment_video) {
    private val binding by viewBinding(FragmentVideoBinding::bind)
    private val viewModel: VideoViewModel by viewModels()
    private val args: VideoFragmentArgs by navArgs()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private var currentMediaIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        setFragmentResultListener("unlocked") { _, _ -> playNextVideo() }
    }

    private fun setupViews() = with(binding) {
        name.text = args.item.name
        desc.text = args.item.description
        val next = playerView.findViewById<ImageView>(com.google.android.exoplayer2.ui.R.id.exo_next)
        val prev = playerView.findViewById<ImageView>(com.google.android.exoplayer2.ui.R.id.exo_prev)
        val playPause = playerView.findViewById<ImageView>(com.google.android.exoplayer2.R.id.exo_pause)
        val fullScreen = playerView.findViewById<ImageView>(R.id.exo_fullscreen_icon)
        playPause.setOnClickListener {
            if (simpleExoPlayer.isPlaying) {
                simpleExoPlayer.pause()
                playPause.setImageResource(com.google.android.exoplayer2.R.drawable.exo_controls_play)
            } else {
                simpleExoPlayer.play()
                playPause.setImageResource(com.google.android.exoplayer2.R.drawable.exo_controls_pause)
            }
        }
        fullScreen.setOnClickListener {
            viewModel.toggleFullscreen()
            val iconRes = if (viewModel.isFullscreen.value) R.drawable.ic_fullscreen_exit else R.drawable.ic_fullscreen
            fullScreen.setImageDrawable(ContextCompat.getDrawable(requireContext(), iconRes))
        }
        next.setOnClickListener { playNextVideo() }
        prev.setOnClickListener { playPreviousVideo() }
    }

    private fun setupObservers() {
        viewModel.isFullscreen.onEach {
            toggleFullscreen(it)
        }.launchIn(lifecycleScope)
    }

    private fun initializePlayer() {

        val mediaDataSourceFactory = DefaultDataSource.Factory(requireContext())
        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        args.lessons.list.forEach { lesson ->
            val mediaSource =
                ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(MediaItem.fromUri(lesson.video_url))
            simpleExoPlayer.addMediaSource(mediaSource)
        }
        currentMediaIndex = args.lessons.list.indexOf(args.item)
        simpleExoPlayer.seekToDefaultPosition(currentMediaIndex)

        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.pauseAtEndOfMediaItems = true
        simpleExoPlayer.prepare()
        binding.playerView.player = simpleExoPlayer
        binding.playerView.requestFocus()
    }

    private fun releasePlayer() {
        simpleExoPlayer.release()
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleFullscreen(false)
    }

    private fun toggleFullscreen(isFullscreen: Boolean) {
        val layoutParams = binding.playerView.layoutParams as ConstraintLayout.LayoutParams
        val act = requireActivity()
        if (isFullscreen) {
            layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT

            act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            act.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = 0

            act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            act.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        binding.playerView.layoutParams = layoutParams
    }

    private fun playNextVideo() {
        if (Constants.isPurchased || currentMediaIndex < 2) {
            simpleExoPlayer.pause()
            currentMediaIndex++
            if (currentMediaIndex < args.lessons.list.size) {
                simpleExoPlayer.seekTo(currentMediaIndex, 0)
                simpleExoPlayer.play()
                binding.name.text = args.lessons.list[currentMediaIndex].name
                binding.desc.text = args.lessons.list[currentMediaIndex].description
            }
        } else {
            navController.navigate(VideoFragmentDirections.actionVideoFragmentToPremiumDialog())
        }
    }

    private fun playPreviousVideo() {
        simpleExoPlayer.pause()
        currentMediaIndex--
        if (currentMediaIndex >= 0) {
            simpleExoPlayer.seekTo(currentMediaIndex, 0)
            simpleExoPlayer.play()
            binding.name.text = args.lessons.list[currentMediaIndex].name
            binding.desc.text = args.lessons.list[currentMediaIndex].description
        }
    }
}