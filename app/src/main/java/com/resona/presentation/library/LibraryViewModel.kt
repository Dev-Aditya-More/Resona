package com.resona.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resona.data.repository.SongRepository
import com.resona.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: SongRepository
) : ViewModel() {

    sealed interface State {
        data object Loading : State
        data class Ready(val songs: List<Song>) : State
        data object Empty : State
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {
            _state.value = State.Loading
            repository.syncSongs()
            repository.getSongs().collect { songs ->
                _state.value = if (songs.isEmpty()) State.Empty else State.Ready(songs)
            }
        }
    }
}
