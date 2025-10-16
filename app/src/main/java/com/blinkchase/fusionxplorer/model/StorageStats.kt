package com.blinkchase.fusionxplorer.model

data class StorageStats(
    val totalSpace: Long,
    val freeSpace: Long
) {
    val usedSpace: Long
        get() = totalSpace - freeSpace
}
