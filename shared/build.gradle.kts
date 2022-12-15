plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.4.21"
    id("com.squareup.sqldelight")
    id("kotlin-parcelize")
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {

                //Local
                implementation(Deps.SqlDelight.runtime)

                //Dependency injection
                implementation(Deps.Koin.core)

                //Networking
                with(Deps.Ktor) {
                    implementation(core)
                    implementation(clientCio)
                    implementation(contentNegociation)
                    implementation(serializationCore)
                    implementation(serializationJson)
                    implementation(logging)
                }

                //Parsing
                implementation(Deps.Kotlinx.serializationJson)

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.SqlDelight.androidDriver)
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation(Deps.SqlDelight.nativeDriver)
                implementation(Deps.Ktor.darwin)
                implementation(Deps.landscapist)
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}


sqldelight {
    database("CamperproDatabase") {
        packageName = "com.example.camperpro.database"
        sourceFolders = listOf("sqldelight")
    }
}

android {
    namespace = "com.example.camperpro"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}
dependencies {
    implementation("com.google.android.gms:play-services-location:21.0.1")
}
