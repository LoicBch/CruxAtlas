package com.example.camperpro.data.model.dto

@kotlinx.serialization.Serializable
data class CheckListDto(
    val id: String,
    val name: String,
    //    val description: String,
    //    val imageLink: String,
    val tags: String,
    val tasks: List<TaskDto>
)

@kotlinx.serialization.Serializable
data class TaskDto(val id: String, val name: String)