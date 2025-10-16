package com.blinkchase.fusionxplorer.ui.screens

import android.os.Environment
import android.os.StatFs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.blinkchase.fusionxplorer.model.StorageStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Function to get storage stats
private fun getStorageStats(): StorageStats {
    val stat = StatFs(Environment.getDataDirectory().path)
    val blockSize = stat.blockSizeLong
    val totalBlocks = stat.blockCountLong
    val availableBlocks = stat.availableBlocksLong
    val totalSpace = totalBlocks * blockSize
    val freeSpace = availableBlocks * blockSize

    return StorageStats(
        totalSpace = totalSpace,
        freeSpace = freeSpace
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageAnalysisScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var storageStats by remember { mutableStateOf<StorageStats?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Load storage stats when the screen is first shown
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val stats = getStorageStats()
                withContext(Dispatchers.Main) {
                    storageStats = stats
                    isLoading = false
                }
            } catch (e: Exception) {
                error = e.message
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Storage Analysis") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $error")
                }
            }

            else -> {
                storageStats?.let { stats ->
                    StorageAnalysisContent(
                        stats = stats,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
private fun StorageAnalysisContent(
    stats: StorageStats,
    modifier: Modifier = Modifier
) {
    val totalSpace = stats.totalSpace
    val usedSpace = stats.usedSpace
    val freeSpace = stats.freeSpace
    val usedPercentage = (usedSpace.toFloat() / totalSpace * 100).coerceIn(0f, 100f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Storage Overview Card
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Storage Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Storage Bar
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LinearProgressIndicator(
                        progress = { usedPercentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${"%.1f".format(usedPercentage)}% used",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${formatFileSize(usedSpace)} of ${formatFileSize(totalSpace)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Storage Info
                StorageInfoItem("Total Space", formatFileSize(totalSpace))
                StorageInfoItem("Used Space", formatFileSize(usedSpace))
                StorageInfoItem("Free Space", formatFileSize(freeSpace))
                StorageInfoItem("Used", "%.1f%%".format(usedPercentage))
            }
        }

        // Storage Categories
        Text(
            "Storage Usage by Category",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // TODO: Add storage categories (Images, Videos, Audio, Apps, etc.)
        // You can use FileUtils to scan and categorize files
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Storage categories will be shown here",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StorageInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

// Helper function to format file size
private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "%.1f MB".format(bytes / (1024.0 * 1024.0))
        else -> "%.1f GB".format(bytes / (1024.0 * 1024.0 * 1024.0))
    }
}