package com.appmobiledition.laundryfinder.android.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appmobiledition.laundryfinder.android.R
import com.appmobiledition.laundryfinder.android.ui.theme.AppColor
import kotlinx.coroutines.flow.StateFlow


@Composable
fun GlobalSliderModal(
    sliderState: StateFlow<MutableList<GlobalSliderState>>,
    onRemoveGpsNotification: () -> Unit,
    onRemoveNetworkNotification: () -> Unit
) {

    val state by sliderState.collectAsState()
    Column {
        AnimatedVisibility(visible = state.size > 0) {
            Column(
                Modifier
                    .padding(top = 70.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(15))
                    .background(
                        color = AppColor.PrimaryContainer, shape = RoundedCornerShape(15)
                    )
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
                ) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp), text = if (state.size > 0) {
                            when (state[0]) {
                                GlobalSliderState.NETWORK_MISSING -> stringResource(id = R.string.slider_missing_network_title)
                                GlobalSliderState.GPS_MISSING -> stringResource(id = R.string.slider_missing_gps_title)
                            }
                        } else {
                            ""
                        }, color = Color.Black, fontFamily = FontFamily(Font(R.font.circularstdmedium)), fontWeight = FontWeight.W500, fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        if (state.size > 0) {
                            when (state[0]) {
                                GlobalSliderState.NETWORK_MISSING -> onRemoveNetworkNotification()
                                GlobalSliderState.GPS_MISSING -> onRemoveGpsNotification()
                            }
                        }

                    }) {
                        Icon(imageVector = Icons.Sharp.Close, contentDescription = " ")
                    }
                }

                Text(
                    text = if (state.size > 0) {
                        when (state[0]) {
                            GlobalSliderState.NETWORK_MISSING -> stringResource(id = R.string.slider_missing_network_text)
                            GlobalSliderState.GPS_MISSING -> stringResource(id = R.string.slider_missing_gps_text)
                        }
                    } else {
                        ""
                    }, fontSize = 14.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)), fontWeight = FontWeight(450), color = Color.Black
                )
            }
        }

        AnimatedVisibility(visible = state.size > 1) {
            if (state.size > 1) {
                Column(
                    Modifier
                        .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(15))
                        .background(
                            color = AppColor.PrimaryContainer, shape = RoundedCornerShape(15)
                        )
                        .padding(start = 16.dp, bottom = 16.dp)
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.padding(top = 16.dp), text =
                            if (state.size > 1) {
                                when (state[1]) {
                                    GlobalSliderState.NETWORK_MISSING -> stringResource(id = R.string.slider_missing_network_title)
                                    GlobalSliderState.GPS_MISSING -> stringResource(id = R.string.slider_missing_gps_title)
                                }
                            } else {
                                ""
                            }, color = Color.Black,fontFamily = FontFamily(Font(R.font.circularstdmedium)), fontWeight = FontWeight.W500, fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            if (state.size > 1) {
                                when (state[1]) {
                                    GlobalSliderState.NETWORK_MISSING -> onRemoveNetworkNotification()
                                    GlobalSliderState.GPS_MISSING -> onRemoveGpsNotification()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Sharp.Close, contentDescription = " ")
                        }
                    }

                    Text(
                        if (state.size > 1) {
                            when (state[1]) {
                                GlobalSliderState.NETWORK_MISSING -> stringResource(id = R.string.slider_missing_network_text)
                                GlobalSliderState.GPS_MISSING -> stringResource(id = R.string.slider_missing_gps_text)
                            }
                        } else {
                            ""
                        }, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)), fontWeight = FontWeight(450), color = Color.Black
                    )
                }
            }
        }
    }
}

enum class GlobalSliderState {
    NETWORK_MISSING, GPS_MISSING
}