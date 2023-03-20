package com.example.camperpro.android.onBoarding

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.camperpro.android.MainActivity
import com.example.camperpro.android.R
import com.example.camperpro.android.extensions.hasLocationPermission
import com.example.camperpro.android.ui.theme.AppColor
import com.ramcosta.composedestinations.annotation.Destination

// TODO: optimize permissions management
@Destination
@Composable
fun SplashScreen(navController: NavController, viewModel: SplashScreenViewModel) {

    //    val setupIsComplete = viewModel.setupIsComplete.collectAsState()
    val setupIsComplete = viewModel.gpsLocationIsAsked.collectAsState()
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

    if (setupIsComplete.value) {
        LaunchedEffect(setupIsComplete) {
            navController.navigate(MainActivity.Graphs.HOME)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.SplashScreenBackground), contentAlignment = Alignment
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
        } else {
            viewModel.onUserRespondToLocationPermission(true)
        }
        viewModel.initApp()
    }
}