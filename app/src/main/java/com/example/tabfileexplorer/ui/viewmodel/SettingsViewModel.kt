package com.example.tabfileexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabfileexplorer.data.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettings: AppSettings
) : ViewModel() {
    
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    init {
        // Collect theme preference when ViewModel is created
        viewModelScope.launch {
            appSettings.themePreference.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
    }
    
    suspend fun setDarkTheme(enabled: Boolean) {
        appSettings.setDarkTheme(enabled)
    }
}
