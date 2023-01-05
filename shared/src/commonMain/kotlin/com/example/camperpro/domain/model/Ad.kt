package com.example.camperpro.domain.model

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

@CommonParcelize
class Ad(
    val type: String,
    val url: String,
    val click: String
): CommonParcelable