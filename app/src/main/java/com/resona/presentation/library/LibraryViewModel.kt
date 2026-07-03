package com.resona.presentation.library

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resona.data.repository.ManualImportResult
import com.resona.data.repository.SongRepository
import com.resona.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: SongRepository
) : ViewModel() {

    sealed interface State {
        data object Loading : State
        data class Ready(val songs: List<Song>) : State
        data object Empty : State
        data object NoPermission : State
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _importResult = MutableSharedFlow<ManualImportResult>(extraBufferCapacity = 1)
    val importResult: SharedFlow<ManualImportResult> = _importResult

    init {
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {
            if (!hasStoragePermission()) {
                _state.value = State.NoPermission
                return@launch
            }
            _state.value = State.Loading
            repository.syncSongs()
            repository.getSongs().collect { songs ->
                _state.value = if (songs.isEmpty()) State.Empty else State.Ready(songs)
            }
        }
    }

    private fun hasStoragePermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun addManualSongs(uris: List<Uri>) {
        viewModelScope.launch {
            val result = repository.addManualSongs(uris)
            // Room Flow emits automatically — no manual refresh needed
            _importResult.emit(result)
        }
    }
}
