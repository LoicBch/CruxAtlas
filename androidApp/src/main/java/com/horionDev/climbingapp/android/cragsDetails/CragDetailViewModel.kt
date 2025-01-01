package com.horionDev.climbingapp.android.cragsDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.android.decryptFileWithAES
import com.horionDev.climbingapp.android.login.LoginScreenViewModel.LoginScreenEvent
import com.horionDev.climbingapp.android.parcelable.CradDetailsParcel
import com.horionDev.climbingapp.android.parcelable.fromParcelable
import com.horionDev.climbingapp.android.parcelable.toParcelable
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.repositories.Crags
import com.horionDev.climbingapp.data.repositories.Models
import com.horionDev.climbingapp.data.repositories.Users
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.createRoutesJsonForUnity
import com.horionDev.climbingapp.utils.Constants
import com.horionDev.climbingapp.utils.SessionManager
import com.unity3d.player.s
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File

class CragDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val crags: Crags,
    private val users: Users,
    private val models: Models
) : ViewModel() {

    private val _event = MutableSharedFlow<CragDetailsEvent>()
    val event = _event.asSharedFlow()

    val cragDetails = savedStateHandle.getStateFlow<CradDetailsParcel?>("crags", null)
    val isFavorite = savedStateHandle.getStateFlow("isFavorite", false)

    fun init(cragId: Int) {
        viewModelScope.launch {
            when (val result = crags.getCragDetails(cragId)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["crags"] = result.value.toParcelable()
                }

                is ResultWrapper.Failure -> {
                    // handle error
                }
            }
        }
        savedStateHandle["isFavorite"] = SessionManager.user.favorites.contains(cragId.toString())
    }

    fun fetchModel(modelId: String) {
        viewModelScope.launch {

            val model = File(
                "/data/data/com.horionDev.climbingapp.android/models/${modelId}/",
                "${modelId}.glb"
            )

            val infos = File(
                "/data/data/com.horionDev.climbingapp.android/models/${modelId}/",
                "infos.json"
            )

            val routes = File(
                "/data/data/com.horionDev.climbingapp.android/models/${modelId}/",
                "routes.json"
            )

            if (model.exists() && infos.exists() && routes.exists()) {
                println("File already exists at: ${model.absolutePath}")
                _event.emit(CragDetailsEvent.OpenModelOnUnity("/data/data/com.horionDev.climbingapp.android/models/${modelId}/"))
                return@launch
            }

            when (models.get(modelId) { percent ->
                println("Fetching model $percent%")
            }) {
                is ResultWrapper.Success -> {
                    decodeFilesForUnity("/data/data/com.horionDev.climbingapp.android/models/${modelId}/")
                    _event.emit(CragDetailsEvent.OpenModelOnUnity("/data/data/com.horionDev.climbingapp.android/models/${modelId}/"))
                }

                is ResultWrapper.Failure -> {
                    println("Error while fetching model")
                }
            }
        }
    }

    fun decodeFilesForUnity(path: String) {
        File(path).listFiles()?.forEach { file ->
            decodeToTempFile(file)
        }
    }

    private fun decodeToTempFile(file: File) {
        val decryptedBytes = decryptFileWithAES(file.readBytes(), Constants.SECURITY.KEY_FILES)
        val tempFileName = "temp_${file.name}"
        val tempFile = File(file.parent, tempFileName)
        tempFile.writeBytes(decryptedBytes)
    }

    fun permuteFavorite(cragId: Int) {
        viewModelScope.launch {
            if (SessionManager.user.favorites.contains(cragId.toString())) {
                when (users.removeCragAsFavoriteForUser(SessionManager.user.id, cragId)) {
                    is ResultWrapper.Success -> {
                        savedStateHandle["isFavorite"] = false
                    }

                    is ResultWrapper.Failure -> {
                        // handle error
                    }
                }
            } else {
                when (users.addCragAsFavoriteToUser(SessionManager.user.id, cragId)) {
                    is ResultWrapper.Success -> {
                        savedStateHandle["isFavorite"] = true
                    }

                    is ResultWrapper.Failure -> {
                        // handle error
                    }
                }
            }
        }
    }

    sealed class CragDetailsEvent {
        //      todo  Quand unity est close il faut supprimer les fichiers temporaires
        data class OpenModelOnUnity(val path: String) : CragDetailsEvent()
    }
}
