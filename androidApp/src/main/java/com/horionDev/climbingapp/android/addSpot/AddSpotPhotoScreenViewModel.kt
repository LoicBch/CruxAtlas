//package com.horionDev.climbingapp.android.addSpot
//
//import android.content.ContentResolver
//import android.content.Context
//import android.net.Uri
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.horion.murbex.data.ResultWrapper
//import com.horion.murbex.domain.usecases.managinImages.AddImageToSpotUseCase
//import kotlinx.coroutines.launch
//import java.io.InputStream
//import java.nio.ByteBuffer
//
//class AddSpotPhotoScreenViewModel(
//    private val savedStateHandle: SavedStateHandle,
//    private val addImageToSpotUseCase: AddImageToSpotUseCase,
//) : ViewModel() {
//
//    val photos = savedStateHandle.getStateFlow("photos", emptyList<String>())
//
//    fun addPhoto(photo: String) {
//        savedStateHandle["photos"] = photos.value + photo
//    }
//
//    fun removePhoto(photo: String) {
//        savedStateHandle["photos"] = photos.value - photo
//    }
//
//    fun attachPhotoToSpot(spotId: Int, uris: List<Uri>, context: Context) {
//        viewModelScope.launch {
//            uris.forEach { uri ->
//
//                when (val result = addImageToSpotUseCase(spot = spotId, byteArray = uriToByteArray(context, uri)!!)) {
//                    is ResultWrapper.Success -> {
//
//                    }
//                    is ResultWrapper.Failure -> {
//
//                    }
//                }
//            }
//
//        }
//    }
//
//    fun uriToByteArray(context: Context, imageUri: Uri): ByteArray? {
//        val contentResolver: ContentResolver = context.contentResolver
//
//        try {
//            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
//            if (inputStream != null) {
//                val byteBuffer = ByteBuffer.allocate(inputStream.available())
//                inputStream.use { input ->
//                    while (true) {
//                        val byte = input.read()
//                        if (byte == -1) break
//                        byteBuffer.put(byte.toByte())
//                    }
//                }
//                return byteBuffer.array()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return null
//    }
//
//}