package com.resona.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resona.util.openPlayStoreListing
import com.resona.util.openPrivacyPolicy
import com.resona.util.shareApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    bottomPadding: PaddingValues,
    onNavigateToAbout: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val accentColor by settingsViewModel.accentColor.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottomPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsSectionHeader("Appearance")
            AccentColorRow(selected = accentColor, onSelect = settingsViewModel::setAccentColor)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.outline
            )

            SettingsSectionHeader("About")
            SettingsItem(
                icon = Icons.Filled.Info,
                title = "About Resona",
                subtitle = "Version, credits & links",
                onClick = onNavigateToAbout
            )
            SettingsItem(
                icon = Icons.Filled.Star,
                title = "Rate on Google Play",
                onClick = { openPlayStoreListing(context) }
            )
            SettingsItem(
                icon = Icons.Filled.Share,
                title = "Share Resona",
                onClick = { shareApp(context) }
            )
            SettingsItem(
                icon = Icons.Filled.PrivacyTip,
                title = "Privacy Policy",
                onClick = { openPrivacyPolicy(context) }
            )
        }
    }
}
