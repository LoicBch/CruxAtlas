package com.horionDev.climbingapp.android.newsFeed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun NewsFeedScreen() {
    val news = remember {
        listOf(
            NewsItem("Titre 1", "Description 1", "https://example.com/image1.jpg"),
            NewsItem("Titre 2", "Description 2", "https://example.com/image2.jpg"),
            NewsItem("Titre 3", "Description 3", "https://example.com/image3.jpg"),
            // Ajoutez plus d'éléments de news ici
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
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

data class NewsItem(
    val title: String,
    val description: String,
    val imageUrl: String
)