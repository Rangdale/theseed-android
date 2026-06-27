plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.theseed.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.theseed.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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

configurations.all {
    resolutionStrategy.eachDependency {
        when {
            requested.group == "androidx.core" &&
                    (requested.name == "core" || requested.name == "core-ktx") ->
                useVersion("1.13.1")
            requested.group == "androidx.activity" ->
                useVersion("1.9.3")
            requested.group == "androidx.lifecycle" ->
                useVersion("2.8.7")
            requested.group == "androidx.navigation" ->
                useVersion("2.7.7")
        }
    }
}

dependencies {
    implementation(libs.lottie.compose)
    implementation(libs.googleSignIn)
    implementation(libs.retrofit.gson)
    implementation("androidx.compose.material:material-icons-extended")

    implementation(libs.androidx.splashscreen)
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.runtime.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit + Serialization
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // Navigation + Lifecycle
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    debugImplementation(libs.compose.ui.tooling)
}