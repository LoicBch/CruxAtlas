plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.3.1").apply(false)
    id("com.android.library").version("7.3.1").apply(false)
    kotlin("android").version("1.7.10").apply(false)
    kotlin("multiplatform").version("1.7.10").apply(false)
}

buildscript {
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.4")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register("renameFontFiles") {
    doLast {
        val fontDir = file("androidApp/src/main/res/font")
        if (fontDir.exists() && fontDir.isDirectory) {
            fontDir.listFiles()?.forEach { file ->
                if (file.name.endsWith(".ttf")) {
                    val newName = file.name.replace("[A-Z-]".toRegex(), "").replace(".ttf", ".otf")
                    val newFile = File(fontDir, newName)
                    file.renameTo(newFile)
                }
            }
        } else {
            println("Font directory not found or is not a directory.")
        }
    }
}
