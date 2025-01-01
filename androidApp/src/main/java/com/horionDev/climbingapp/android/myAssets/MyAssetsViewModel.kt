package com.horionDev.climbingapp.android.myAssets

import android.os.StatFs
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.horionDev.climbingapp.android.parcelable.ModelParcel
import com.horionDev.climbingapp.android.parcelable.toParcelable
import com.horionDev.climbingapp.data.model.dto.ModelDto
import com.horionDev.climbingapp.domain.model.entities.Model
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import toVo
import java.io.File

class MyAssetsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var models = savedStateHandle.getStateFlow<List<ModelParcel>>("models", emptyList())
    var storageAvailable = savedStateHandle.getStateFlow("storageAvailable", "")
    var storageUsed = savedStateHandle.getStateFlow("storageUsed", "")
    var modelsCount = savedStateHandle.getStateFlow("modelsCount", 0)

    init {
        savedStateHandle["models"] = getLocalModels()
        savedStateHandle["storageUsed"] =
            File("/data/data/com.horionDev.climbingapp.android/models/").sizeString()
        savedStateHandle["storageAvailable"] = getAvailableStorageSpaceString()
        savedStateHandle["modelsCount"] = models.value.size
    }
}


fun File.sizeString(): String {
    val totalSizeBytes = walkTopDown()
        .filter { it.isFile }
        .map { it.length() }
        .sum()

    return when {
        totalSizeBytes >= 1_000_000_000 -> "${"%.2f".format(totalSizeBytes / 1_000_000_000.0)} GB"
        totalSizeBytes >= 1_000_000 -> "${"%.2f".format(totalSizeBytes / 1_000_000.0)} MB"
        totalSizeBytes >= 1_000 -> "${"%.2f".format(totalSizeBytes / 1_000.0)} KB"
        else -> "$totalSizeBytes bytes"
    }
}

fun File.size(): Long {
    return walkTopDown()
        .filter { it.isFile }
        .map { it.length() }
        .sum()
}

fun getAvailableStorageSpaceString(path: String = "/"): String {
    val statFs = StatFs(path)
    val blockSize = statFs.blockSizeLong // Taille d'un bloc
    val availableBlocks = statFs.availableBlocksLong // Nombre de blocs disponibles
    val availableBytes = blockSize * availableBlocks

    return when {
        availableBytes >= 1_000_000_000 -> "${"%.2f".format(availableBytes / 1_000_000_000.0)} GB"
        availableBytes >= 1_000_000 -> "${"%.2f".format(availableBytes / 1_000_000.0)} MB"
        availableBytes >= 1_000 -> "${"%.2f".format(availableBytes / 1_000.0)} KB"
        else -> "$availableBytes bytes"
    }
}


fun getLocalModels(): List<ModelParcel> {
    val parser = Json {
        ignoreUnknownKeys = true
    }

    return File("/data/data/com.horionDev.climbingapp.android/models/").subdirectoriesPath()
        .map { modelPath ->
            val jsonContent =
                File(modelPath).listFiles { file -> file.name == "infos.json" }?.first()?.readText()
           parser.decodeFromString<ModelDto>(jsonContent!!).toVo().toParcelable().apply {
               size = File(modelPath).size()
           }
        }
}

fun Long.toReadableSize(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val base = 1024
    val exponent = (Math.log(this.toDouble()) / Math.log(base.toDouble())).toInt()
    val size = this / Math.pow(base.toDouble(), exponent.toDouble())
    return String.format("%.2f %s", size, units[exponent])
}

fun File.subdirectoriesPath(): List<String> =
    listFiles { file -> file.isDirectory }
        ?.map { it.absolutePath }
        ?: emptyList()
