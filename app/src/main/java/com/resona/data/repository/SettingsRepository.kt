package com.resona.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.resona.ui.theme.AccentColor
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore(name = "resona_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {
    private val accentColorKey = stringPreferencesKey("accent_color")

    val accentColor = context.settingsDataStore.data.map { prefs ->
        AccentColor.fromName(prefs[accentColorKey])
    }

    suspend fun setAccentColor(accent: AccentColor) {
        context.settingsDataStore.edit { prefs ->
            prefs[accentColorKey] = accent.name
        }
    }
}
