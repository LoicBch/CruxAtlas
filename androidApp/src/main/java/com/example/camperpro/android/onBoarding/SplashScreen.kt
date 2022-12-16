package com.example.camperpro.android.onBoarding

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
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.camperpro.android.MainActivity
import com.example.camperpro.android.R
import com.example.camperpro.android.ui.theme.AppColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun SplashScreen(navController: NavController, viewModel: SplashScreenViewModel = getViewModel()) {

    val setupIsComplete = viewModel.setupIsComplete.collectAsState()

    val color = remember { Animatable(Color.White) }
    LaunchedEffect(Unit) {
        color.animateTo(AppColor.BlueCamperPro, animationSpec = infiniteRepeatable(tween(2000),
            RepeatMode.Reverse))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color.value), contentAlignment = Alignment
            .Center
    ) {
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
    }

    if (setupIsComplete.value) {
        navController.navigate(MainActivity.Graphs.HOME)
    }
}