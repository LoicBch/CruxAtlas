package com.example.camperpro.android.onBoarding

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.camperpro.android.MainActivity
import com.example.camperpro.android.R
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.utils.LocationClient
import com.example.camperpro.utils.hasLocationPermission
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.get
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

// TODO: optimize permissions management
@Destination
@Composable
fun SplashScreen(navController: NavController, viewModel: SplashScreenViewModel) {

    val setupIsComplete = viewModel.setupIsComplete.collectAsState()
    val context = LocalContext.current
    val locationPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    viewModel.onUserRespondToLocationPermission(true)
                }
                else -> {
                    viewModel.onUserRespondToLocationPermission(false)
                }
            }
        }

    val color = remember { Animatable(Color.White) }

    if (setupIsComplete.value) {
        LaunchedEffect(setupIsComplete) {
            navController.navigate(MainActivity.Graphs.HOME)
        }
    }

    LaunchedEffect(Unit) {
        color.animateTo(
            AppColor.BlueCamperPro, animationSpec = infiniteRepeatable(
                tween(2000),
                RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color.value), contentAlignment = Alignment
            .Center
    ) {
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
    }

    LaunchedEffect(true) {
        if (!context.hasLocationPermission) {
            locationPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }else{
            viewModel.onUserRespondToLocationPermission(true)
        }
    }


    viewModel.initApp()

}