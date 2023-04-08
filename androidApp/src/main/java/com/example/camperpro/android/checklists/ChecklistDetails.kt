package com.example.camperpro.android.checklists

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.AppButton
import com.example.camperpro.android.spotSheet.Gallery
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import com.example.camperpro.domain.model.CheckList
import com.example.camperpro.domain.model.Task
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun ChecklistDetails(
    navigator: DestinationsNavigator,
    viewModel: CheckListDetailsViewModel = getViewModel(),
    checklist: CheckList
) {

    val application = LocalContext.current.applicationContext

    LaunchedEffect(key1 = true) {
        viewModel.init(application as Application, checklist)
    }

    Column {
        Header(onClose = { navigator.popBackStack() })
        Body(checkList = checklist,
             viewModel.tasksDoneIdsFlow,
             { viewModel.checkTask(it, checklist, application as Application) },
             { viewModel.unCheckTask(it, checklist, application as Application) }
        ) { viewModel.clearAll(checklist, application as Application) }
    }
}


@Composable
fun Header(onClose: () -> Unit) {

    Box(Modifier.heightIn(min = 50.dp)) {

        Gallery(listOf())

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 12.dp, start = 17.dp, end = 17.dp)
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
        }
    }
}

@Composable
fun Body(
    checkList: CheckList,
    tasksDoneFlow: MutableStateFlow<MutableState<List<String>>>,
    onTaskCheck: (String) -> Unit,
    onTaskUncheck: (String) -> Unit,
    onClearAll: () -> Unit
) {

    val scrollState = rememberScrollState()
    val tasksDone by tasksDoneFlow.collectAsState()

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        Text(
            text = checkList.name,
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            color = Color.Black
        )
        Text(
            modifier = Modifier.padding(vertical = 12.dp),
            text = checkList.tags.joinToString(separator = " ") { "#$it" },
            fontWeight = FontWeight.W700,
            fontSize = 12.sp,
            color = AppColor.Primary
        )
        Text(
            text = checkList.name,
            fontWeight = FontWeight(450),
            fontSize = 22.sp,
            color = Color.Black
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${tasksDone.value.size}/${checkList.tasks.size}",
                color = AppColor.Primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.W500
            )
        }

        Divider(modifier = Modifier.padding(top = 11.dp))

        LazyColumn(
            modifier = Modifier
                .scrollable(
                    state = scrollState, orientation = Orientation.Vertical
                )
        ) {
            items(checkList.tasks) { task ->
                TaskItem(task, selected = tasksDone.value.contains(task.id)) {
                    if (tasksDone.value.contains(task.id)) {
                        onTaskUncheck(task.id)
                    } else {
                        onTaskCheck(task.id)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            isActive = true,
            onClick = { onClearAll() },
            modifier = Modifier.padding(bottom = 25.dp),
            textRes = R.string.clear_all
        )
    }
}

@Composable
fun TaskItem(task: Task, selected: Boolean, onClick: () -> Unit) {

    var checkedState by remember { mutableStateOf(selected) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onClick()
            }, verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = task.name,
            fontSize = 14.sp,
            fontWeight = FontWeight(450),
            color = AppColor.Tertiary
        )
        Spacer(modifier = Modifier.weight(1f))

        Checkbox(checked = selected,
                 enabled = true,
                 colors = CheckboxDefaults.colors(checkedColor = AppColor.Primary),
                 onCheckedChange = {
                     onClick()
                 })
    }
    Divider(modifier = Modifier.fillMaxWidth())
}