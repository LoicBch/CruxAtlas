package com.horionDev.climbingapp.android.areaDetails

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun CragsTab(
    crags: List<Crag>
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        crags.forEach {
            CragBlock(Modifier, it) {

            }
        }
    }
}

@Composable
fun CragBlock(modifier: Modifier = Modifier, crag: Crag, onCragClick: (Crag) -> Unit) {
    if (!crag.image.isNullOrEmpty()) {
        Box {
            GlideImage(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp, bottomStart = 8.dp
                        )
                    )
                    .size(130.dp),
                imageModel = { crag.image },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillHeight, alignment = Alignment.Center
                )
            )
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {

        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            fontWeight = FontWeight.W500,
            maxLines = 1,
            fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.oppinsedium)),
            text = crag.name,
        )

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                fontWeight = FontWeight(450),
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium)),
                text = "${crag.sectors.size}" + stringResource(id = R.string.sectors),
                color = AppColor.neutralText
            )

            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                fontWeight = FontWeight(450),
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium)),
                text = "${
                    crag.sectors.map { it.routes?.size ?: 0 }
                        .foldRight(0) { element, acc -> acc + element }
                }" + stringResource(id = R.string.routes),
                color = AppColor.neutralText
            )
        }
    }
}