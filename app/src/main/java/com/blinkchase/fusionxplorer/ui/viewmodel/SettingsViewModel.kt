package com.blinkchase.fusionxplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blinkchase.fusionxplorer.data.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettings: AppSettings
) : ViewModel() {
    
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    private val _showHiddenFiles = MutableStateFlow(false)
    val showHiddenFiles: StateFlow<Boolean> = _showHiddenFiles.asStateFlow()
    
    init {
        // Collect preferences when ViewModel is created
        viewModelScope.launch {
            launch {
                appSettings.themePreference.collect { isDark ->
                    _isDarkTheme.value = isDark
                }
            }
            launch {
                appSettings.showHiddenFiles.collect { show ->
                    _showHiddenFiles.value = show
                }
            }
        }
    }
    
    fun setShowHiddenFiles(show: Boolean) {
        viewModelScope.launch {
            appSettings.setShowHiddenFiles(show)
        }
    }
    
    suspend fun setDarkTheme(enabled: Boolean) {
        appSettings.setDarkTheme(enabled)
    }
}
