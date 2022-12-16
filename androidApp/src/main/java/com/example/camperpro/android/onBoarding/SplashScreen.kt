package com.example.camperpro.android.onBoarding

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.camperpro.android.MainActivity
import com.example.camperpro.android.R
import com.example.camperpro.android.ui.theme.AppColor
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun SplashScreen(navController: NavController, viewModel: SplashScreenViewModel = getViewModel()) {

    val setupIsComplete by remember {
        mutableStateOf(viewModel.setupIsComplete)
    }

    val color = remember { Animatable(Color.White) }
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

    if (setupIsComplete) {
        navController.navigate(MainActivity.Graphs.HOME)
    }
}