package com.resona.presentation.player

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.resona.domain.model.Song
import com.resona.service.ResonaPlaybackService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    data class PlayerState(
        val currentSong: Song? = null,
        val isPlaying: Boolean = false,
        val progress: Float = 0f,
        val currentPosition: Long = 0L
    )

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state.asStateFlow()

    private var mediaController: MediaController? = null
    private var songQueue: List<Song> = emptyList()
    private var progressJob: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _state.update { it.copy(isPlaying = isPlaying) }
            if (isPlaying) startProgressTicker() else progressJob?.cancel()
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val index = mediaController?.currentMediaItemIndex ?: return
            _state.update { it.copy(currentSong = songQueue.getOrNull(index)) }
        }
    }

    init {
        connectToService()
    }

    private fun connectToService() {
        val token = SessionToken(context, ComponentName(context, ResonaPlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        future.addListener({
            runCatching { future.get() }.onSuccess { controller ->
                mediaController = controller
                controller.addListener(playerListener)
            }
        }, MoreExecutors.directExecutor())
    }

    fun playSongs(songs: List<Song>, startIndex: Int = 0) {
        songQueue = songs
        val controller = mediaController ?: return
        val items = songs.map { song ->
            MediaItem.Builder()
                .setMediaId(song.id.toString())
                .setUri(song.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setAlbumTitle(song.album)
                        .build()
                )
                .build()
        }
        controller.setMediaItems(items, startIndex, C.TIME_UNSET)
        controller.prepare()
        controller.play()
        _state.update { it.copy(currentSong = songs.getOrNull(startIndex), isPlaying = true) }
        startProgressTicker()
    }

    fun togglePlayPause() {
        val controller = mediaController ?: return
        if (controller.isPlaying) controller.pause() else controller.play()
    }

    fun skipNext() { mediaController?.seekToNextMediaItem() }

    fun skipPrevious() { mediaController?.seekToPreviousMediaItem() }

    fun seekTo(fraction: Float) {
        val controller = mediaController ?: return
        val duration = controller.duration
        if (duration > 0) controller.seekTo((fraction * duration).toLong())
    }

    private fun startProgressTicker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                delay(500)
                val controller = mediaController ?: break
                val position = controller.currentPosition
                val duration = controller.duration
                if (duration > 0) {
                    _state.update {
                        it.copy(
                            progress = position.toFloat() / duration.toFloat(),
                            currentPosition = position
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        progressJob?.cancel()
        mediaController?.release()
        super.onCleared()
    }
}
