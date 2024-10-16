plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

ksp {
    arg("compose-destinations.mode", "singlemodule")
    arg("compose-destinations.moduleName", "androidApp")
}

android {
    namespace = "com.horionDev.climbingapp.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.horionDev.climbingapp.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "0.2"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(files("libs/launcher-debug.aar"))
    implementation(files("libs/unityLibrary-debug.aar"))
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.compose.ui:ui:1.3.1")
    implementation("androidx.compose.ui:ui-tooling:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.1")
    implementation("androidx.compose.foundation:foundation:1.3.1")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.runtime:runtime:1.6.8")
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.7.27-beta")
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.airbnb.android:lottie-compose:5.2.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.github.skydoves:landscapist-glide:2.1.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:maps-compose:2.5.3")
    implementation("io.insert-koin:koin-core:3.2.2")
    implementation("io.insert-koin:koin-android:3.3.0")
    implementation("io.insert-koin:koin-androidx-compose:3.3.0")
    implementation("com.android.volley:volley:1.2.1")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.7.27-beta")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.startup:startup-runtime:1.1.1")
    implementation("com.himanshoe:charty:2.0.0-alpha01")
    implementation("com.github.skydoves:orchestra-spinner:1.2.0")
//    implementation("com.patrykandpatrick.vico:compose:2.0.0-alpha.22")
}