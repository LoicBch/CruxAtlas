package com.horionDev.climbingapp.android.login

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.LocalDependencyContainer
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.composables.GlobalPopupState
import com.horionDev.climbingapp.android.destinations.SignupScreenDestination
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginScreenViewModel = getViewModel()
) {
    val loginIsValid by viewModel.loginValid.collectAsState()
    val loginIsComplete by viewModel.loginIsComplete.collectAsState()
    val loginFailed by viewModel.loginFailed.collectAsState()
    val newMailSentPopupActive by viewModel.newMailSendPopupActive.collectAsState()
    val appViewModel = LocalDependencyContainer.current.appViewModel
    val askForNewPass by viewModel.askForNewPass.collectAsState()


    LaunchedEffect(loginIsComplete) {
        if (loginIsComplete) {
            navigator.popBackStack()
        }
    }

    LaunchedEffect(true) {
        viewModel.event.collect {
            when (it) {
                LoginScreenViewModel.LoginScreenEvent.HideLoading -> appViewModel.onLoadingChange(
                    false
                )

                LoginScreenViewModel.LoginScreenEvent.ShowLoading -> appViewModel.onLoadingChange(
                    true
                )

                LoginScreenViewModel.LoginScreenEvent.NewPassPopupActive -> {
                    val popup = GlobalPopupState.CUSTOM
                    popup.title = "Password reset"
                    popup.content =
                        "A new password has been sent to your email. Please check your inbox and spam folder"
                    appViewModel.showGlobalPopup(popup)
                }
            }
        }
    }

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
            viewModel.controlUsername(it.text)
        }

        TextFieldAnimate(
            modifier = Modifier.padding(top = 15.dp),
            asteriskVisible = true,
            placeHolder = R.string.password
        ) {
            viewModel.controlPass(it.text)
        }

        if (loginFailed) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = "Username or password incorrect", color = AppColor.Primary30
            )
        }

        Button(
            modifier = Modifier.padding(top = 40.dp),
            onClick = {
                viewModel.login()
            },
            enabled = loginIsValid
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

        Text(
            modifier = Modifier
                .clickable {
                    viewModel.showNewPassPopup()
                }
                .padding(top = 5.dp),
            textAlign = TextAlign.Center,
            text = "Forgot your password ? Click here",
        )
    }

    if (askForNewPass) {
        PopupPasswordReset({ viewModel.forgotPassword(it) }) {
            viewModel.hideNewPassPopup()
        }
    }
}

@Composable
fun PopupPasswordReset(onRequestNewPassword: (String) -> Unit, onClose: () -> Unit) {
    var email by remember { mutableStateOf("") }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = AppColor.ClearGrey)
        .clickable {
            onClose()
        }) {
        Column(
            Modifier
                .padding(horizontal = 34.dp)
                .shadow(4.dp, RoundedCornerShape(5))
                .background(
                    color = Color.White, shape = RoundedCornerShape(5)
                )
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.Center)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Reset password", color = AppColor.Primary30)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Sharp.Close,
                    contentDescription = "",
                    tint = AppColor.Primary30,
                    modifier = Modifier.clickable {
                        onClose()
                    }
                )
            }
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                isActive = email.isNotEmpty(),
                onClick = { onRequestNewPassword(email) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textRes = R.string.valid
            )
        }
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
        if (initialContent != null) {
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