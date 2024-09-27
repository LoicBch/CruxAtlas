package com.horionDev.climbingapp.android.onBoarding

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.horionDev.climbingapp.android.MainActivity
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.extensions.hasLocationPermission
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

// TODO: optimize permissions management
@Destination
@Composable
fun SplashScreen(navController: NavController, viewModel: SplashScreenViewModel = getViewModel()) {

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

    if (setupIsComplete.value) {
        LaunchedEffect(setupIsComplete) {
            navController.navigate(MainActivity.Graphs.HOME)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.white), contentAlignment = Alignment
            .Center
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.app_logo), contentDescription = "")
            Image(painter = painterResource(id = R.drawable.app_name), contentDescription = "")
        }
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