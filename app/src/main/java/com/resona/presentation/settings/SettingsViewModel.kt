package com.resona.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resona.data.repository.SettingsRepository
import com.resona.ui.theme.AccentColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val accentColor: StateFlow<AccentColor> = settingsRepository.accentColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, AccentColor.Default)

    fun setAccentColor(accent: AccentColor) {
        viewModelScope.launch {
            settingsRepository.setAccentColor(accent)
        }
    }
}
