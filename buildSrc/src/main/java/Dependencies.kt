object Versions {

    object Compose {
        const val viewmodel = "2.5.1"
        const val core = "1.3.1"
        const val uiTooling = "1.3.1"
        const val uiToolingPreview = "1.3.1"
        const val foundation = "1.3.1"
        const val material = "1.3.1"
        const val activity = "1.6.1"
        const val navigationObjectArgs = "1.7.27-beta"
        const val navigationObjectArgsKsp = "1.7.27-beta"
        const val accompanist = "0.28.0"
    }

    object Kotlinx {
        const val serializationJson = "1.4.0"
    }

    object Maps {
        const val playService = "18.1.0"
        const val compose = "2.5.3"
    }

    object Koin {
        const val core = "3.2.2"
        const val android = "3.3.0"
        const val compose = "3.3.0"
    }

    object Ktor {
        const val core = "2.1.3"
        const val clientCio = "2.0.0"
        const val contentNegociation = "2.1.3"
        const val serializationCore = "2.0.0"
        const val logging = "2.1.3"
        const val serializationJson = "2.1.3"
        const val darwin = "2.1.3"
    }

    object SqlDelight {
        const val runtime = "1.5.4"
        const val nativeDriver = "1.5.4"
        const val androidDriver = "1.5.4"
    }

    object Lottie {
        const val compose = "5.2.0"
    }

    const val napier = "2.6.1"
    const val preferences = "1.0.0"
    const val landscapist = "2.1.0"

}

object Deps {

    object Kotlinx {
        const val serializationJson =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlinx.serializationJson}"
    }

    object Compose {
        const val viewmodel =
            "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Compose.viewmodel}"
        const val core = "androidx.compose.ui:ui:${Versions.Compose.core}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.Compose.uiTooling}"
        const val uiToolingPreview =
            "androidx.compose.ui:ui-tooling-preview:${Versions.Compose.uiToolingPreview}"
        const val foundation =
            "androidx.compose.foundation:foundation:${Versions.Compose.foundation}"
        const val material = "androidx.compose.material:material:${Versions.Compose.material}"
        const val activity = "androidx.activity:activity-compose:${Versions.Compose.activity}"
        const val navigationObjectArgs =
            "io.github.raamcosta.compose-destinations:animations-core:${Versions.Compose.navigationObjectArgs}"
        const val navigationObjectArgsKsp =
            "io.github.raamcosta.compose-destinations:ksp:${Versions.Compose.navigationObjectArgsKsp}"
        const val accompanist = "com.google.accompanist:accompanist-pager:${Versions.Compose.accompanist}"
    }

    object Maps {
        const val playService =
            "com.google.android.gms:play-services-maps:${Versions.Maps.playService}"
        const val compose = "com.google.maps.android:maps-compose:${Versions.Maps.compose}"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.Koin.core}"
        const val android = "io.insert-koin:koin-android:${Versions.Koin.android}"
        const val compose = "io.insert-koin:koin-androidx-compose:${Versions.Koin.compose}"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.Ktor.core}"
        const val clientCio = "io.ktor:ktor-client-cio:${Versions.Ktor.clientCio}"
        const val contentNegociation =
            "io.ktor:ktor-client-content-negotiation:${Versions.Ktor.contentNegociation}"
        const val serializationCore =
            "io.ktor:ktor-client-serialization:${Versions.Ktor.serializationCore}"
        const val logging = "io.ktor:ktor-client-logging:${Versions.Ktor.logging}"
        const val serializationJson =
            "io.ktor:ktor-serialization-kotlinx-json:${Versions.Ktor.serializationJson}"
        const val darwin = "io.ktor:ktor-client-darwin:${Versions.Ktor.darwin}"
    }

    object SqlDelight {
        const val runtime = "com.squareup.sqldelight:runtime:${Versions.SqlDelight.runtime}"
        const val nativeDriver =
            "com.squareup.sqldelight:native-driver:${Versions.SqlDelight.nativeDriver}"
        const val androidDriver =
            "com.squareup.sqldelight:android-driver:${Versions.SqlDelight.androidDriver}"
    }

    object Lottie {
        const val compose = "com.airbnb.android:lottie-compose:${Versions.Lottie.compose}"
    }

    const val napier = "io.github.aakira:napier:${Versions.napier}"
    const val preferences = "androidx.datastore:datastore-preferences:${Versions.preferences}"
    const val landscapist = "com.github.skydoves:landscapist-glide:${Versions.landscapist}"
}