package com.blinkchase.fusionxplorer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.blinkchase.fusionxplorer.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

data class StorageStats(
    val totalSpace: Long,
    val freeSpace: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onStorageAnalysisClick: () -> Unit,
    onStorageClick: () -> Unit = {},
    onCacheClear: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState(initial = false)
    val showHiddenFiles by viewModel.showHiddenFiles.collectAsState(initial = false)

    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Appearance Section
            item {
                SettingsSectionHeader("Appearance")
            }

            item {
                SettingsSwitchItem(
                    title = "Dark Theme",
                    subtitle = "Enable dark mode",
                    checked = isDarkTheme,
                    onCheckedChange = { enabled ->
                        coroutineScope.launch {  // Launch coroutine here
                            viewModel.setDarkTheme(enabled)
                        }
                    }
                )
            }

            // File Management Section
            item {
                SettingsSectionHeader("File Management")
            }

            item {
                SettingsSwitchItem(
                    title = "Show Hidden Files",
                    subtitle = "Show hidden files and folders",
                    checked = showHiddenFiles,
                    onCheckedChange = { show ->
                        coroutineScope.launch {
                            viewModel.setShowHiddenFiles(show)
                        }
                    }
                )
            }

            // Storage Section
            item {
                SettingsSectionHeader("Storage")
            }

            item {
                SettingsActionItem(
                    title = "Storage Analysis",
                    subtitle = "View storage usage and statistics",
                    onClick = onStorageAnalysisClick
                )
            }

            // Advanced Section
            item {
                SettingsSectionHeader("Advanced")
            }

            item {
                SettingsActionItem(
                    title = "Clear Cache",
                    subtitle = "Clear temporary files and thumbnails",
                    onClick = { /* TODO: Implement cache clearing */ }
                )
            }

            // About Section
            item {
                SettingsSectionHeader("About")
            }

            item {
                SettingsActionItem(
                    title = "Version",
                    subtitle = "1.0.0",
                    onClick = { /* No action */ }
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp)
    )
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = null, // Handled by the row click
        )
    }
}

@Composable
private fun SettingsActionItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatFileSize(bytes: Long): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var size = bytes.toDouble()
    var unitIndex = 0

    while (size > 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }

    return "%.1f %s".format(size, units[unitIndex])
}