package com.horionDev.climbingapp.android.myAssets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.cragsDetails.ActionButton
import com.horionDev.climbingapp.android.cragsDetails.CragDetailViewModel
import com.horionDev.climbingapp.android.parcelable.ModelParcel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Composable
@Destination
fun MyAssetsScreen(
    viewModel: MyAssetsViewModel = getViewModel(), navigator: DestinationsNavigator
) {

    val models by viewModel.models.collectAsState()
    val storageAvailable by viewModel.storageAvailable.collectAsState()
    val storageUsed by viewModel.storageUsed.collectAsState()
    val modelsCount by viewModel.modelsCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {

        HeaderAssets(navigator)

        Text(
            modifier = Modifier.padding(top = 25.dp, bottom = 10.dp),
            text = "You have $modelsCount models (${storageUsed})"
        )
//        Text(modifier = Modifier.padding(bottom = ), text = "Storage available: $storageAvailable")


        LazyColumn() {
            items(models.size) { index ->
                val model = models[index]
                ModelItem(model)
            }
        }
    }
}

@Composable
fun ModelItem(model: ModelParcel) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .size(70.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(25),
            )
            .background(
                color = Color.White, shape = RoundedCornerShape(25)
            )
            .padding(16.dp), verticalAlignment = CenterVertically
    ) {

        Icon(
            imageVector = Icons.Sharp.AccountCircle, contentDescription = "", tint = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        Text("${model.name} - ${model.size.toReadableSize()}")

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {

        }, modifier = Modifier.padding(end = 8.dp)) {
            Icon(painter = painterResource(R.drawable.modeling), contentDescription = "")
        }

        IconButton(onClick = {

        }) {
            Icon(imageVector = Icons.Sharp.MoreVert, contentDescription = "")
        }
    }
}

@Composable
fun HeaderAssets(navigator: DestinationsNavigator) {
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
            text = "My models",
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

