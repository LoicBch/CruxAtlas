package com.example.camperpro.android.help

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HelpScreen(navigator: DestinationsNavigator) {

    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { navigator.popBackStack() }) {
            Image(painter = painterResource(id = R.drawable.arrow_left), contentDescription = "")
        }

        Text(
            modifier = Modifier.padding(top = 41.dp),
            text = stringResource(id = R.string.help),
            fontSize = 22.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight(700)
        )


        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(id = R.string.help_subtitle),
            fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight(450)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable {
                    uriHandler.openUri("https://www.camperpro.com/faq")
                }
        ) {
            Icon(painter = painterResource(id = R.drawable.world), contentDescription = "")
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = stringResource(id = R.string.faq_link),
                fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontWeight = FontWeight(450)
            )
        }

        Text(
            modifier = Modifier.padding(top = 80.dp),
            text = stringResource(id = R.string.bug_report),
            fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight(500)
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(id = R.string.bug_text),
            fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontWeight = FontWeight(450)
        )
    }

}