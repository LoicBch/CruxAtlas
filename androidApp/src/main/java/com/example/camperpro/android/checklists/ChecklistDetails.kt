package com.example.camperpro.android.checklists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.camperpro.android.R
import com.example.camperpro.android.extensions.share
import com.example.camperpro.android.spotSheet.Gallery
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.Dealer

@Composable
fun ChecklistDetails() {

    Column {
//        Header(partner = partner, onClose = { navigator.popBackStack() })
    }

}



@Composable
fun Header(partner: Dealer, onClose: () -> Unit) {

    val context = LocalContext.current

    Box(Modifier.heightIn(min = 50.dp)) {

        if (partner.isPremium && partner.photos.isNotEmpty()) {
            Gallery(photos = partner.photos)
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
        ) {
            IconButton(modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
                .zIndex(1f)
                .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { onClose() }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
                .zIndex(1f)
                .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.help), contentDescription = "")
            }

            IconButton(modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(Dimensions.radiusRound))
                .zIndex(1f)
                .background(Color.White, RoundedCornerShape(Dimensions.radiusRound)),
                       onClick = { context.share(context, "") }) {
                Icon(painter = painterResource(id = R.drawable.share), contentDescription = "")
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
        ) {
            if (partner.isPremium) {
                Row(
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.premium_badge),
                        contentDescription = "",
                        tint = AppColor.Primary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(15))
                        .zIndex(1f)
                        .background(Color.White, RoundedCornerShape(15))
                        .padding(5.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 5.dp),
                        painter = painterResource(id = R.drawable.premium_badge),
                        contentDescription = "",
                        tint = AppColor.Primary
                    )
                    Text(
                        text = stringResource(id = R.string.verified),
                        color = AppColor.neutralText,
                        fontWeight = FontWeight.W500,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}