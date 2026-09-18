plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.caribeanroyal.headless"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.caribeanroyal.headless"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        missingDimensionStrategy("brand", "brandA")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // ONLY DEPEND ON THE HEADLESS CORE SDK!
    // No dependency on checkout-ui.
    // --- To use the published remote SDK ---
    implementation("com.caribeanroyal.ecommercesample:checkout-core:1.0.5")

    // --- To build the SDK from local source (uncomment this and comment the remote one) ---
    // implementation(project(":sdk:checkout-core"))
    
    // Add feature:booking to show the booking screen
    implementation(project(":feature:booking"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":library:designsystem"))
    
    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    
    implementation(libs.retrofit.main)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)
}
