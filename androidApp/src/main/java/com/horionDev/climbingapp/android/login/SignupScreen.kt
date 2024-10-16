package com.horionDev.climbingapp.android.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Destination
@Composable

fun SignupScreen(navigator: DestinationsNavigator, viewModel: SignupViewModel = getViewModel()) {

    val signupIsValid by viewModel.signupIsValid.collectAsState()
    val signupIsComplete by viewModel.signupIsComplete.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    if (signupIsComplete) {
        Popup(onDismissRequest = {
            navigator.popBackStack()
        }) {
            Text(text = "test")
        }
    }

    LaunchedEffect(key1 = signupIsComplete) {
        if (signupIsComplete) {
            navigator.popBackStack()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Outlined.Close, contentDescription = "")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.create_account), fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(
                    Font(R.font.oppinsedium)
                )
            )

            Text(
                text = stringResource(id = R.string.lets_explore), fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.oppinsedium))
            )

            TextFieldAnimate(
                modifier = Modifier.padding(top = 40.dp), placeHolder = R.string.username
            ) {
                viewModel.controlUsername(it.text)
            }

            TextFieldAnimate(
                modifier = Modifier.padding(top = 15.dp), placeHolder = R.string.email
            ) {
                viewModel.controlMail(it.text)
            }

            TextFieldAnimate(
                modifier = Modifier.padding(top = 15.dp),
                placeHolder = R.string.password,
                asteriskVisible = true
            ) {
                viewModel.controlPass(it.text)
            }

            if (errorMessage != "") {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = errorMessage, color = Color.Red,
                    fontFamily = FontFamily(Font(R.font.oppinsedium)),
                    fontSize = 14.sp
                )
            }

            Row(Modifier.padding(horizontal = 18.dp)) {
                AppButton(
                    modifier = Modifier.padding(top = 40.dp),
                    onClick = {
                        viewModel.signup()
                    },
                    isActive = signupIsValid,
                    textRes = R.string.signup
                )
            }
        }
    }
}