package com.horionDev.climbingapp.android.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.horionDev.climbingapp.android.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
//viewModel: SignupViewModel = getViewModel()
fun SignupScreen(navigator: DestinationsNavigator) {

//    val signupIsValid by viewModel.signupIsValid.collectAsState()
//    val signupIsComplete by viewModel.signupIsComplete.collectAsState()

//    if (signupIsComplete) {
//        Popup(onDismissRequest = {
//            navigator.popBackStack()
//        }) {
//            Text(text = "test")
//        }
//    }

//    LaunchedEffect(key1 = signupIsComplete) {
//        if (signupIsComplete) {
//            navigator.popBackStack()
//        }
//    }

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
            Text(text = "Create your account")
            Text(text = "let's explore")

            TextFieldAnimate(
                modifier = Modifier.padding(top = 40.dp), placeHolder = R.string.username
            ) {
//                viewModel.controlUsername(it.text)
            }

            TextFieldAnimate(
                modifier = Modifier.padding(top = 15.dp), placeHolder = R.string.email
            ) {
//                viewModel.controlMail(it.text)
            }

            TextFieldAnimate(
                modifier = Modifier.padding(top = 15.dp),
                placeHolder = R.string.password,
                asteriskVisible = true
            ) {
//                viewModel.controlPass(it.text)
            }

            Button(
                modifier = Modifier.padding(top = 40.dp),
                onClick = {
//                    viewModel.signup()
                          },
//                enabled = signupIsValid,
                enabled = true,
                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (signupIsValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = "Signup !")
            }
        }
    }

}