package com.appmobiledition.laundryfinder.domain.model

import com.appmobiledition.laundryfinder.utils.CommonParcelable
import com.appmobiledition.laundryfinder.utils.CommonParcelize

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

