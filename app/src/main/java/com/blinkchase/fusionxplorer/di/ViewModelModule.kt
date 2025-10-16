package com.blinkchase.fusionxplorer.di

import com.blinkchase.fusionxplorer.data.AppSettings
import com.blinkchase.fusionxplorer.ui.viewmodel.SettingsViewModel
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
    fun provideSettingsViewModel(settings: AppSettings): SettingsViewModel {
        return SettingsViewModel(settings)
    }
}
