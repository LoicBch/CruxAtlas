package com.appmobiledition.laundryfinder.domain.model

import com.appmobiledition.laundryfinder.utils.CommonParcelable
import com.appmobiledition.laundryfinder.utils.CommonParcelize

@CommonParcelize
class Ad(
    val type: String,
    val url: String,
    val redirect: String,
    val click: String
): CommonParcelable