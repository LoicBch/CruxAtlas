package com.example.camperpro.android.checklists

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.collectAsStateWithLifecycleImmutable
import com.example.camperpro.android.destinations.ChecklistDetailsDestination
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.domain.model.CheckList
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun CheckListsScreen(
    navigator: DestinationsNavigator, viewModel: CheckListsViewModel = getViewModel()
) {
    Column {
        CheckListHeader(navigator = navigator)
        Tags(viewModel.tagsFlow) { viewModel.selectTag(it) }
        CheckLists(viewModel.checklistsShowed) {
            navigator.navigate(ChecklistDetailsDestination(it))
        }
    }
}

@Composable
fun CheckListHeader(navigator: DestinationsNavigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navigator.popBackStack() }) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = stringResource(id = R.string.travel_checklists), fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(0.5f))

        IconButton(modifier = Modifier.alpha(0f), onClick = {}) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
    }
}

@Composable
fun Tags(tagsFlow: StateFlow<List<Pair<String, Boolean>>>, onSelectTag: (String) -> Unit) {

    val tags by tagsFlow.collectAsState()
    val scrollState = rememberScrollState()

    LazyRow(
        modifier = Modifier
            .padding(start = 15.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(tags) { tag ->
            TagItem(tag) { onSelectTag(it) }
        }
    }
}

@Composable
fun TagItem(tag: Pair<String, Boolean>, selectTag: (String) -> Unit) {
    Row(modifier = Modifier
        .padding(start = 16.dp)
        .shadow(2.dp, RoundedCornerShape(8))
        .background(
            if (tag.second) {
                AppColor.Primary
            } else {
                AppColor.greyTags
            }, RoundedCornerShape(8)
        )
        .padding(vertical = 8.dp, horizontal = 12.dp)
        .clickable { selectTag(tag.first) }, verticalAlignment = Alignment.CenterVertically) {

        Text(
            text = "#", fontSize = 22.sp, fontWeight = FontWeight.W700, fontFamily = FontFamily(Font(R.font.circularstdmedium)), color = if (tag.second) {
                Color.White
            } else {
                AppColor.Tertiary
            }
        )
        Text(
            text = tag.first,
            Modifier.padding(start = 5.dp), fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = if (tag.second) {
                Color.White
            } else {
                AppColor.Tertiary
            }
        )
    }
}

@Composable
fun CheckLists(checklistsFlow: StateFlow<List<CheckList>>, openChecklist: (CheckList) -> Unit) {

    val checkLists by checklistsFlow.collectAsStateWithLifecycleImmutable()
    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 22.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(checkLists.value) { checklist ->
            println("task is ${checklist.id}")
            ChecklistItem(checklist) { openChecklist(checklist) }
        }
    }
}

@Composable
fun ChecklistItem(checklist: CheckList, openChecklist: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 16.dp)
        .shadow(2.dp, RoundedCornerShape(8))
        .background(
            Color.White, RoundedCornerShape(8)
        )
        .clickable { openChecklist() }) {

        Image(
            modifier = Modifier
                .size(130.dp)
                .padding(end = 16.dp),
            painter = painterResource(id = R.drawable.twitter),
            contentDescription = ""
        )

        Column {
            Text(
                text = checklist.name,
                Modifier.padding(top = 16.dp),
                fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontWeight = FontWeight.W500,
                color = AppColor.Tertiary
            )

            Text(
                text = checklist.tags.joinToString(separator = " ") { "#$it" },
                Modifier.padding(top = 8.dp),
                fontSize = 11.sp,fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                fontWeight = FontWeight.W500,
                color = AppColor.Primary
            )
        }

    }
}
