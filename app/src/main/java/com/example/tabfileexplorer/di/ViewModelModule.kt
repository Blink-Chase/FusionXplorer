package com.example.tabfileexplorer.di

import com.example.tabfileexplorer.ui.viewmodel.SettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    
    @Provides
    @ViewModelScoped
    fun provideSettingsViewModel(settings: com.example.tabfileexplorer.data.AppSettings): SettingsViewModel {
        return SettingsViewModel(settings)
    }
}
