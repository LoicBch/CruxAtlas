package com.horionDev.climbingapp.android.addSpot

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.horionDev.climbingapp.BuildConfig
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.composables.SmallAppButton
import com.horionDev.climbingapp.android.extensions.createImageFile
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import java.util.*


@Destination
@Composable
fun AddSpotPhotoScreen(
    navigator: DestinationsNavigator,
    crag: Crag,
//    viewModel: AddSpotPhotoScreenViewModel = getViewModel(),
//    resultNavigator: ResultBackNavigator<Location>
) {

    val context = LocalContext.current
    var uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        Build.PRODUCT + ".provider", context.createImageFile()
    )

//    val photos by viewModel.photos.collectAsState()
    val photos = listOf<String>()

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            uriList.forEach {
//                viewModel.addPhoto(it.toString())
            }
        }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
//            viewModel.addPhoto(uri.toString())
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            uri = FileProvider.getUriForFile(
                Objects.requireNonNull(context),
                BuildConfig.LIBRARY_PACKAGE_NAME + ".provider", context.createImageFile()
            )
            cameraLauncher.launch(uri)
        } else {

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.none),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(modifier = Modifier.alpha(0f), onClick = { }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
            }
        }

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Add Photo to your spot",
            fontWeight = FontWeight.Bold,
        )

        Divider(modifier = Modifier.padding(vertical = 10.dp))

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Photo has to be of the outside and has to be taken from a public road",
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                PhotoSourceSelection(
                    onLaunchGallery = { galleryLauncher.launch("image/*") },
                    onLaunchCamera = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    })
            }

            if (photos.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "Photos added",
                        fontWeight = FontWeight.Black
                    )
                }
            }

            items(photos) { uri ->
                photoItem(
                    Uri.parse(uri),
                    onDeletePhoto = {
//                        viewModel.removePhoto(uri)
                    })
            }
        }

        AppButton(
            isActive = photos.isNotEmpty(),
            onClick = {
//                viewModel.attachPhotoToSpot(spot.id, photos.map { Uri.parse(it) }, context)
//                resultNavigator.navigateBack(Location(spot.latitude, spot.longitude))
            },
            modifier = Modifier.padding(top = 10.dp),
            textRes = R.string.finish_spot_creation_photo
        )

    }
}

@Composable
fun PhotoSourceSelection(onLaunchGallery: () -> Unit, onLaunchCamera: () -> Unit) {
    Column(horizontalAlignment = CenterHorizontally) {

        AppButton(
            isActive = true,
            onClick = {
                onLaunchCamera()
            },
            modifier = Modifier.padding(vertical = 10.dp),
            textRes = R.string.add_photo_from_camera
        )

        Text(text = "or", fontWeight = FontWeight.Medium)
        AppButton(
            isActive = true,
            onClick = {
                onLaunchGallery()
            },
            modifier = Modifier.padding(vertical = 10.dp),
            textRes = R.string.add_photo_from_gallery
        )
        Divider(modifier = Modifier.padding(vertical = 10.dp))
    }
}

@Composable
fun photoItem(uri: Uri, onDeletePhoto: () -> Unit = {}) {

    Column {
        GlideImage(
            imageModel = { uri },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            imageOptions = ImageOptions(contentScale = ContentScale.Fit)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))
            SmallAppButton(
                isActive = true,
                onClick = { onDeletePhoto() },
                modifier = Modifier,
                textRes = R.string.remove
            )
        }
    }
}