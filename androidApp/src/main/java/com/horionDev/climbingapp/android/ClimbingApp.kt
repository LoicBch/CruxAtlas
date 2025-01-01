package com.horionDev.climbingapp.android

import android.app.Application
import com.horionDev.climbingapp.android.di.platformModule
import com.horionDev.climbingapp.utils.Constants
import com.horionDev.climbingapp.utils.di.AndroidDependencyProvider
import com.horionDev.climbingapp.utils.di.ModelInitializer
import com.horionDev.climbingapp.utils.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import java.io.File
import java.security.Key
import java.util.zip.ZipInputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.spec.SecretKeySpec

class ClimbingApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val initializer = object : ModelInitializer {
            override fun unzip(byteArray: ByteArray) {
                val extractedFiles = mutableMapOf<String, ByteArray>()
                val decryptedBytes = decryptFileWithAES(byteArray, Constants.SECURITY.KEY_ARCHIVE)

                var modelId = ""
                ZipInputStream(decryptedBytes.inputStream()).use { zipInputStream ->
                    var entry = zipInputStream.nextEntry
                    while (entry != null) {
                        if (!entry.isDirectory) {
                            val fileBytes = zipInputStream.readBytes()
                            extractedFiles[entry.name] = fileBytes

                            if (entry.name.endsWith(".glb")) {
                                modelId = entry.name
                            }
                        }
                        entry = zipInputStream.nextEntry
                    }
                }

                if (modelId.isEmpty()) {
                    throw IllegalArgumentException("Archive does not contain a .glb file")
                }

                val modelDirectory =
                    "/data/data/com.horionDev.climbingapp.android/models/${modelId}/"

                extractedFiles.forEach { (fileName, fileBytes) ->
                    val targetPath = when {
                        fileName.endsWith(".glb") -> "${modelDirectory}${fileName}"
                        fileName == "routes.json" -> "${modelDirectory}${fileName}"
                        fileName == "info.json" -> "${modelDirectory}${fileName}"
                        else -> null
                    }

                    targetPath?.let { path ->
                        File(path).writeBytes(fileBytes)
                        println("File $fileName moved to $path")
                    }
                }

                extractedFiles.clear()
                println("Extraction and move completed. Memory cleaned.")
            }
        }

        val androidModule = module {
            single<ModelInitializer> { initializer }
        }

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@ClimbingApp)
            modules(sharedModule() + platformModule() + androidModule)
        }
    }
}

fun getSecretKeyFromHex(hexKey: String): Key {
    val keyBytes = hexKey.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    return SecretKeySpec(keyBytes, "AES")
}

fun decryptFileWithAES(encryptedBytes: ByteArray, secretKey: String): ByteArray {
    val key = getSecretKeyFromHex(Constants.SECURITY.KEY_ARCHIVE)
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, key)

    val cipherInputStream = CipherInputStream(encryptedBytes.inputStream(), cipher)
    return cipherInputStream.readBytes()
}
