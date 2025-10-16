package com.blinkchase.fusionxplorer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class AppSettings @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    // Keys for preferences
    private object PreferencesKeys {
        val THEME = booleanPreferencesKey("dark_theme")
        val SHOW_HIDDEN_FILES = booleanPreferencesKey("show_hidden_files")
        val DEFAULT_VIEW_MODE = intPreferencesKey("default_view_mode")
        val SORT_METHOD = intPreferencesKey("sort_method")
        val SORT_ORDER = intPreferencesKey("sort_order")
        val SHOW_FILE_EXTENSIONS = booleanPreferencesKey("show_file_extensions")
        val ENABLE_THUMBNAILS = booleanPreferencesKey("enable_thumbnails")
        val CONFIRM_DELETE = booleanPreferencesKey("confirm_delete")
    }

    // Theme
    val themePreference: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME] ?: false // Default to light theme
        }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = enabled
        }
    }

    // Show hidden files
    val showHiddenFiles: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SHOW_HIDDEN_FILES] ?: false
        }

    suspend fun setShowHiddenFiles(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_HIDDEN_FILES] = show
        }
    }

    // Default view mode (0 = List, 1 = Grid)
    val defaultViewMode: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DEFAULT_VIEW_MODE] ?: 0
        }

    suspend fun setDefaultViewMode(mode: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEFAULT_VIEW_MODE] = mode
        }
    }

    // Sort method (0 = Name, 1 = Date, 2 = Size, 3 = Type)
    val sortMethod: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SORT_METHOD] ?: 0
        }

    suspend fun setSortMethod(method: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_METHOD] = method
        }
    }

    // Sort order (0 = Ascending, 1 = Descending)
    val sortOrder: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] ?: 0
        }

    suspend fun setSortOrder(order: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = order
        }
    }

    // Show file extensions
    val showFileExtensions: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SHOW_FILE_EXTENSIONS] ?: true
        }

    suspend fun setShowFileExtensions(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_FILE_EXTENSIONS] = show
        }
    }

    // Enable thumbnails
    val enableThumbnails: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ENABLE_THUMBNAILS] ?: true
        }

    suspend fun setEnableThumbnails(enable: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_THUMBNAILS] = enable
        }
    }

    // Confirm before delete
    val confirmDelete: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.CONFIRM_DELETE] ?: true
        }

    suspend fun setConfirmDelete(confirm: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CONFIRM_DELETE] = confirm
        }
    }

    // Clear all preferences (for testing/logout)
    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}