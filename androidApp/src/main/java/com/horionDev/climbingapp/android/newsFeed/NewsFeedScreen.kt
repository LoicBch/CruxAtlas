package com.horionDev.climbingapp.android.newsFeed

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.UnityParentActivity
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.composables.ImageAppButton
import com.horionDev.climbingapp.android.destinations.LoginScreenDestination
import com.horionDev.climbingapp.domain.model.NewsItem
import com.horionDev.climbingapp.domain.model.entities.RouteGrade
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun NewsFeedScreen(
    navigator: DestinationsNavigator,
    viewModel: NewsFeedViewModel = getViewModel()
) {

    val news by viewModel.news.collectAsState()

    val walls = remember {
        listOf(
            Triple("Ange", "Ceuse, France", RouteGrade.SevenA),
            Triple("Berlin", "Ceuse, France", RouteGrade.SevenCPlus),
            Triple("La galere", "Ceuse, France", RouteGrade.SevenA)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Climbing App",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        LoginCard(navigator)

//        ExploreCard()
//        WallsItemCard(walls)
        news.forEach { newsItem ->
            NewsItemCard(newsItem)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NewsItemCard(newsItem: NewsItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            GlideImage(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
                imageModel = { newsItem.imageUrl }, imageOptions = ImageOptions(
                contentScale = ContentScale.FillBounds, alignment = Alignment.Center
            )
            )
            Text(
                text = newsItem.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = newsItem.description,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Image à partir de l'URL
        }
    }
}

@Composable
fun LoginCard(navigator: DestinationsNavigator) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.height(100.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(vertical = 18.dp),
                text = "Hi there! let's sign in to continue."
            )
            AppButton(
                modifier = Modifier.padding(bottom = 18.dp, start = 18.dp, end = 18.dp),
                isActive = true,
                textRes = R.string.login,
                onClick = {
                    navigator.navigate(LoginScreenDestination)
                })
        }
    }
}

@Composable
fun ExploreCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column {
            Text("Hi there! Start Exploring.", modifier = Modifier.padding(vertical = 18.dp))
            ImageAppButton(
                onClick = { /* Handle login */ },
                textRes = R.string.explore,
                isActive = true
            )
        }
    }
}

@Composable
fun WallsItemCard(walls: List<Triple<String, String, RouteGrade>>) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            // Traiter le résultat ici
        }
    )

    Text(
        modifier = Modifier.padding(12.dp),
        text = "Latest 3D walls added",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column {
            walls.forEach { wall ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = wall.first,
                        fontWeight = FontWeight.W500
                    )

                    Text(
                        text = wall.second,
                        fontWeight = FontWeight.W500
                    )

                    Text(
                        text = wall.third.displayValue,
                        fontWeight = FontWeight.W500
                    )

                    IconButton(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(25),
                            )
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(25)
                            ), onClick = {

                        }) {
                        Image(
                            painter = painterResource(id = R.drawable.modeling),
                            contentDescription = stringResource(id = R.string.cd_button_vertical_list)
                        )
                    }
                }
                Divider()
            }
        }
    }
}

@Composable
fun AnotherListCard(walls: List<Pair<String, String>>) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            // Traiter le résultat ici
        }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Text(text = "Another list")

        walls.forEach { wall ->
            Row(modifier = Modifier.padding(16.dp)) {
                Column {
                    Text(
                        text = wall.first,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = wall.second,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AppButton(
                        isActive = true,
                        onClick = {
                            val intent = Intent(context, UnityParentActivity::class.java)
                                .putExtra("unity", "my_unity_scene")
                            launcher.launch(intent)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        textRes = R.string.unity_viewer
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.vatertag),
                    contentDescription = "wall image"
                )
            }
        }
    }
}