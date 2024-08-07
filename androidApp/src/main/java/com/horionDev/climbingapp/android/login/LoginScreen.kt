package com.horionDev.climbingapp.android.login

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.destinations.SignupScreenDestination
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    //    viewModel: LoginScreenViewModel = getViewModel()
) {
    //    val loginIsValid by viewModel.loginValid.collectAsState()
    //    val loginIsComplete by viewModel.loginIsComplete.collectAsState()
    //    val loginFailed by viewModel.loginFailed.collectAsState()
    //    val appViewModel = LocalDependencyContainer.current.appViewModel
    val uriHandler = LocalUriHandler.current


    //    LaunchedEffect(loginIsComplete) {
    //        if (loginIsComplete) {
    //            appViewModel.updateLoginState(true)
    //            navigator.popBackStack()
    //        }
    //    }

    //    LaunchedEffect(true) {
    //        viewModel.event.collect {
    //            when (it) {
    //                LoginScreenViewModel.LoginScreenEvent.HideLoading -> appViewModel.onLoadingChange(
    //                    false
    //                )
    //
    //                LoginScreenViewModel.LoginScreenEvent.ShowLoading -> appViewModel.onLoadingChange(
    //                    true
    //                )
    //            }
    //        }
    //    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Connect to your account", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "let's explore", fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Image(
            modifier = Modifier
                .size(100.dp, 100.dp)
                .padding(top = 10.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = ""
        )

        TextFieldAnimate(
            modifier = Modifier.padding(top = 40.dp), placeHolder = R.string.username
        ) {
            //            viewModel.controlUsername(it.text)
        }

        TextFieldAnimate(
            modifier = Modifier.padding(top = 15.dp),
            asteriskVisible = true,
            placeHolder = R.string.password
        ) {
            //            viewModel.controlPass(it.text)
        }


        //        if (loginFailed) {
        //            Text(
        //                modifier = Modifier.padding(vertical = 5.dp),
        //                text = "Username or password incorrect", color = MaterialTheme.colorScheme.error,
        //                style = MaterialTheme.typography.labelLarge
        //            )
        //        }

        Button(
            modifier = Modifier.padding(top = 40.dp),
            onClick = {
                //                viewModel.login()
            },
            //            enabled = loginIsValid,
            colors = ButtonDefaults.buttonColors(
                //                containerColor = if (loginIsValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(text = "Login")
        }

        Text(
            modifier = Modifier
                .clickable {
                    navigator.navigate(SignupScreenDestination)
                }
                .padding(top = 5.dp),
            textAlign = TextAlign.Center,
            text = "Dont have an account yet ? \nClick to create one",
        )
    }
}

@Composable
fun TextFieldAnimate(
    modifier: Modifier,
    @StringRes placeHolder: Int,
    asteriskVisible: Boolean = false,
    initialContent: String? = null,
    onUserSearch: ((TextFieldValue) -> Unit) = {},
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val boxIsFocused = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        if (initialContent != null ) {
            textState.value = TextFieldValue(initialContent)
        }
    }

    OutlinedTextField(modifier = modifier
        .onFocusChanged {
            boxIsFocused.value = it.isFocused
        }
        .fillMaxWidth(),
                      visualTransformation = if (asteriskVisible) PasswordVisualTransformation() else VisualTransformation.None,
                      maxLines = 1,
                      trailingIcon = {
                          if (textState.value.text.isNotEmpty()) {
                              Icon(
                                  modifier = Modifier.clickable {
                                      textState.value = TextFieldValue("")
                                  },
                                  imageVector = Icons.Outlined.Close,
                                  contentDescription = "",
                                  tint = AppColor.Primary30
                              )
                          }
                      },
                      placeholder = {
                          Text(
                              text = stringResource(placeHolder)
                          )
                      },
                      value = textState.value,
                      onValueChange = { textFieldValue ->
                          textState.value = textFieldValue
                          onUserSearch(textFieldValue)
                      }
    )
}