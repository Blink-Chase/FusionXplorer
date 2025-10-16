package com.blinkchase.fusionxplorer // Make sure this matches your app's package name

import android.annotation.SuppressLint
import android.os.Environment
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

// This is a blueprint for storing information about each file or folder
data class FileModel(
    val name: String,         // Name like "MyPhoto.jpg" or "Documents"
    val path: String,         // Full path like "/storage/emulated/0/Pictures/MyPhoto.jpg"
    val isDirectory: Boolean, // True if it's a folder, false if it's a file
    val size: Long,           // Size in bytes (e.g., 1024 for 1KB). For folders, we'll use 0.
    val lastModified: Long,   // A number representing when it was last changed
    val childrenCount: Int? = null // How many items inside, if it's a folder
)

// Helper to make a date number into a readable string like "dd MMM yyyy HH:mm"
fun Long.toFormattedDateString(pattern: String = "dd MMM yyyy HH:mm"): String { // Added pattern parameter with default
    val date = Date(this)
    // Updated the default pattern here if you prefer not to pass it from FileItem
    // val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(date)
}

// Helper to make a size number into a readable string like "1.5 MB"
@SuppressLint("DefaultLocale")
fun Long.toReadableFileSizeString(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt().coerceIn(0, units.size -1)
    val sizeInUnit = this / 1024.0.pow(digitGroups.toDouble())
    return "${DecimalFormat("#,##0.#").format(sizeInUnit)} ${units[digitGroups]}"
}

// An 'object' is like a single, shared instance of a class.
// We'll put our file functions here.
object FileUtils {

    // Gets the main starting folder for user files (like "Internal Storage")
    fun getRootDirectory(): File {
        return Environment.getExternalStorageDirectory()
    }

    fun isPathReadable(path: String): Boolean {
        if (path.isBlank()) return false
        val file = File(path)
        return file.exists() && file.canRead()
    }

    // This is the core function: gets all files and folders inside a given path
    // Removed 'activity: MainActivity' as it's not strictly needed for childrenCount here
    fun getFiles(directoryPath: String): List<FileModel> {
        val directory = File(directoryPath)

        if (!isPathReadable(directoryPath)) {
            println("Error: Path isn't a valid readable directory or doesn't exist: $directoryPath")
            return emptyList()
        }
        if (!directory.isDirectory) {
            println("Error: Path is not a directory: $directoryPath")
            return emptyList()
        }

        val filesArray = directory.listFiles()

        if (filesArray == null) {
            println("Warning: listFiles() gave null for $directoryPath. Might be restricted.")
            return emptyList()
        }

        return filesArray.mapNotNull { file ->
            if (!file.canRead()) {
                null
            } else {
                FileModel(
                    name = file.name,
                    path = file.absolutePath,
                    isDirectory = file.isDirectory,
                    size = if (file.isDirectory) 0L else file.length(),
                    lastModified = file.lastModified(),
                    childrenCount = if (file.isDirectory) {
                        // Count only readable children for a more accurate representation
                        file.listFiles()?.count { it.canRead() }
                    } else null
                )
            }
        }.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase(Locale.getDefault()) }))
    }
}