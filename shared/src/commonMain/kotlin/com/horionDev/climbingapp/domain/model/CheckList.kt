package com.horionDev.climbingapp.domain.model

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@CommonParcelize
data class CheckList(
    val id: String,
    val name: String,
    val description: String,
    val imageLink: String,
    val tags: List<String>,
    val todos: List<Todo>
) : CommonParcelable

@CommonParcelize
data class Todo(val id: String, val name: String) : CommonParcelable

