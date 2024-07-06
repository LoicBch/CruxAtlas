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
    compileSdk = 33
    defaultConfig {
        applicationId = "com.horionDev.climbingapp.android"
        minSdk = 24
        targetSdk = 33
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

    implementation("com.google.android.gms:play-services-location:21.0.1")

    //Dependency injection
    with(Deps.Koin) {
        implementation(core)
        implementation(android)
        implementation(compose)
    }


    implementation("androidx.startup:startup-runtime:1.1.1")

    //Google maps
    with(Deps.Maps) {
        implementation(compose)
        implementation(playService)
    }

    //Animation
    implementation(Deps.Lottie.compose)

    //Persistence
    implementation(Deps.preferences)

    //Compose
    with(Deps.Compose) {
        implementation(viewmodel)
        implementation(core)
        implementation(uiTooling)
        implementation(uiToolingPreview)
        implementation(foundation)
        implementation(material)
        implementation(activity)
        implementation(navigationObjectArgs)
        implementation(accompanist)
        ksp(navigationObjectArgsKsp)
    }

    //Image
    implementation(Deps.landscapist)
}