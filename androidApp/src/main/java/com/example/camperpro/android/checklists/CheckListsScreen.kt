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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
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
    CheckListHeader(navigator = navigator)
    Tags(viewModel.tagsFlow) { viewModel.selectTag(it) }
    CheckLists(viewModel.checklists) {
        //        navigator.navigate(ChecklistDetailsDestination(it))
    }
}

@Composable
fun CheckListHeader(navigator: DestinationsNavigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navigator.popBackStack() }) {
            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "")
        }
        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = stringResource(id = R.string.menu_my_location),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(0.5f))

    }
}

@Composable
fun Tags(tagsFlow: StateFlow<List<Pair<String, Boolean>>>, onSelectTag: (String) -> Unit) {

    val tags by tagsFlow.collectAsState()
    val scrollState = rememberScrollState()

    LazyRow(
        modifier = Modifier.scrollable(
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
        .padding(5.dp)
        .shadow(2.dp, RoundedCornerShape(8))
        .background(
            if (tag.second) {
                AppColor.Primary
            } else {
                AppColor.Tertiary
            }, RoundedCornerShape(8)
        )
        .clickable { selectTag(tag.first) }) {
        Text(
            text = "#", fontSize = 18.sp, fontWeight = FontWeight.W700, color = if (tag.second) {
                Color.White
            } else {
                AppColor.Tertiary
            }
        )
        Text(
            text = tag.first,
            Modifier.padding(start = 15.dp),
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

    val checkLists by checklistsFlow.collectAsState()
    val scrollState = rememberScrollState()

    LazyColumn(
        modifier = Modifier
            .padding(top = 22.dp)
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
    ) {
        items(checkLists) { checklist ->
            ChecklistItem(checklist) { openChecklist(checklist) }
        }
    }
}

@Composable
fun ChecklistItem(checklist: CheckList, openChecklist: () -> Unit) {
    Row(modifier = Modifier
        .padding(5.dp)
        .shadow(2.dp, RoundedCornerShape(8))
        .background(
            AppColor.Tertiary, RoundedCornerShape(8)
        )
        .clickable { openChecklist() }) {

        Image(painter = painterResource(id = R.drawable.checklist), contentDescription = "")

        Column() {
            Text(
                text = "Name",
                Modifier.padding(top = 16.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                color = AppColor.Tertiary
            )

            Text(
                text = "tags",
                Modifier.padding(top = 8.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.W500,
                color = AppColor.Primary
            )
        }

    }
}
