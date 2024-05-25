package com.appmobiledition.laundryfinder.android.appSettings

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appmobiledition.laundryfinder.android.ui.theme.AppColor
import com.appmobiledition.laundryfinder.utils.Constants
import com.appmobiledition.laundryfinder.utils.KMMPreference
import com.ramcosta.composedestinations.annotation.Destination
import com.appmobiledition.laundryfinder.android.R
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Destination
@Composable
fun SettingsMenu(navigator: DestinationsNavigator) {

    val uriHandler = LocalUriHandler.current

    val languagePopupIsShown = remember {
        mutableStateOf(false)
    }

    val measuringPopupIsShown = remember {
        mutableStateOf(false)
    }

    val menuItems = listOf(Pair(stringResource(id = R.string.measuring_system)) {
        measuringPopupIsShown.value = true
    },
                           Pair(stringResource(id = R.string.language)) {
                               languagePopupIsShown.value = true
                           },
                           Pair(stringResource(id = R.string.help)) { uriHandler.openUri(Constants.HELP_URL) },
                           Pair(stringResource(id = R.string.term_of_use)) {
                               uriHandler.openUri(Constants.A_PROPOS_URL)
                           },
                           Pair(stringResource(id = R.string.privacy_policy)) {
                               uriHandler.openUri(Constants.PRIVACY_POLICY_URL)
                           })

    Column() {
        HeaderSettingsMenu(navigator = navigator)
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            MenuList(menuItems)
            Spacer(Modifier.weight(1f))
            BuildInfos()

        }
    }

    if (languagePopupIsShown.value) {
        LanguagePopup {
            languagePopupIsShown.value = false
        }
    }

    if (measuringPopupIsShown.value) {
        MeasuringPopup {
            measuringPopupIsShown.value = false
        }
    }

}

@Composable
fun LanguagePopup(onClose: () -> Unit) {

    val context = LocalContext.current
    val currentConfig = LocalConfiguration.current

    val languages = listOf(
        Pair(stringResource(id = R.string.french), Locale.FRENCH),
        Pair(stringResource(id = R.string.english), Locale.US),
        Pair(
            stringResource(id = R.string.german),
            Locale.GERMAN
        ), //        Pair(stringResource(id = R.string.spanish), Locale.ITALIAN),
        Pair(stringResource(id = R.string.italian), Locale.ITALIAN)
    )

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
            languages.forEachIndexed { index, language ->

                val (title, locale) = language

                if (index != 0) Divider(modifier = Modifier.fillMaxWidth())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .align(CenterHorizontally)
                        .clickable {
                            val updatedConfig = Configuration(currentConfig).apply {
                                setLocale(locale)
                            }
                            context.resources.updateConfiguration(
                                updatedConfig, context.resources.displayMetrics
                            )
                            (context as Activity).recreate()

                        }, verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun MeasuringPopup(onClose: () -> Unit) {

    val application = LocalContext.current.applicationContext as Application

    val metrics = listOf(
        Pair(stringResource(id = R.string.meter)) {
            KMMPreference(application).put(Constants.PreferencesKey.METRIC, Constants.METER)
            onClose()
        },
        Pair(stringResource(id = R.string.miles)) {
            KMMPreference(application).put(Constants.PreferencesKey.METRIC, Constants.MILES)
            onClose()
        }
    )

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
            metrics.forEachIndexed { index, metric ->

                val (title, action) = metric

                if (index != 0) Divider(modifier = Modifier.fillMaxWidth())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .align(CenterHorizontally)
                        .clickable {
                            action()
                        }, verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun BuildInfos() {
    val context = LocalContext.current
    val packageName = context.packageName
    val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
    val versionName = packageInfo.versionName
    val versionCode = packageInfo.versionCode

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = CenterVertically
    ) {
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = ""
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = stringResource(id = R.string.technical_infos),
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                color = AppColor.Tertiary
            )
            Text(
                text = "${stringResource(id = R.string.version)}: $versionName",
                fontWeight = FontWeight(450),
                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "${stringResource(id = R.string.build)}: $versionCode",
                fontWeight = FontWeight(450),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                color = Color.Gray
            )
        }
    }

    Divider(
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MenuList(menuItems: List<Pair<String, () -> Unit>>) {

    Divider(
        modifier = Modifier.fillMaxWidth()
    )

    menuItems.forEach {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clickable { it.second() },
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = it.first,
                fontWeight = FontWeight.W700,
                fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontSize = 16.sp,
                color = AppColor.Tertiary
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Sharp.ArrowForward,
                contentDescription = "",
                tint = AppColor.Secondary
            )
        }
        Divider(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun HeaderSettingsMenu(navigator: DestinationsNavigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = CenterVertically
    ) {
        IconButton(onClick = { navigator.popBackStack() }) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.app_settings),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight.W500,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(modifier = Modifier.alpha(0f), onClick = {}) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
    }
}