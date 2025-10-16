plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.blinkchase.fusionxplorer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.blinkchase.fusionxplorer"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Add icon dependencies
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)

    testImplementation(libs.junit)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.57.2")
    kapt("com.google.dagger:hilt-android-compiler:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")

    // For storage access
    implementation("androidx.documentfile:documentfile:1.1.0")

    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.9.3")

    // Android Test dependencies with explicit versions to avoid conflicts
    androidTestImplementation(libs.androidx.junit)

    // Force consistent Espresso version
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.7.0")

    // Compose testing dependencies
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug implementations
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Add constraint to force consistent Espresso version across all dependencies
    configurations.all {
        resolutionStrategy {
            force("androidx.test.espresso:espresso-core:3.5.1")
            force("androidx.test.espresso:espresso-contrib:3.5.1")
            force("androidx.test.espresso:espresso-intents:3.5.1")
        }
    }
}