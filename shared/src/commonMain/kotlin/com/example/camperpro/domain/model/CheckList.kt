package com.example.camperpro.domain.model

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

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

