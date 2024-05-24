package com.example.camperpro.android.checklists

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.camperpro.domain.model.CheckList
import com.example.camperpro.managers.location.extension.appending
import com.example.camperpro.managers.location.extension.removed
import com.example.camperpro.utils.KMMPreference
import kotlinx.coroutines.flow.MutableStateFlow

class CheckListDetailsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var tasksDoneIds = mutableStateOf(listOf<String>())
    private val _tasksDoneIds = MutableStateFlow(tasksDoneIds)
    val tasksDoneIdsFlow: MutableStateFlow<MutableState<List<String>>> get() = _tasksDoneIds


    fun init(context: Application, checkList: CheckList) {
        val tasksSaved = KMMPreference(context = context).getString("tasks_of_${checkList.id}")
        println("tasks_of_${checkList.id}")
        println("tasksSaved: $tasksSaved")
        if (tasksSaved != null) {
            if (tasksSaved.contains(",")) {
                tasksDoneIds.value = tasksSaved.split(",")
            } else {
                tasksDoneIds.value = listOf(tasksSaved)
            }
        }
    }

    fun unCheckTask(taskId: String, checkList: CheckList, context: Application) {
        tasksDoneIds.value = tasksDoneIds.value.removed(taskId)
        Log.d("CHECKLIST", tasksDoneIds.value.size.toString())
        KMMPreference(context = context).put(
            "tasks_of_${checkList.id}", tasksDoneIds.value.joinToString(",")
        )
    }

    fun checkTask(taskId: String, checkList: CheckList, context: Application) {
        tasksDoneIds.value = tasksDoneIds.value.appending(taskId)
        Log.d("CHECKLIST", tasksDoneIds.value.size.toString())
        println("tasksSaved: ${tasksDoneIds.value.joinToString(",")}")
        KMMPreference(context = context).put(
            "tasks_of_${checkList.id}", tasksDoneIds.value.joinToString(",")
        )
    }

    fun clearAll(checkList: CheckList, context: Application) {
        Log.d("CHECKLIST", tasksDoneIds.value.size.toString())
        tasksDoneIds.value = listOf()
        KMMPreference(context = context).remove("tasks_of_${checkList.id}")
    }

}