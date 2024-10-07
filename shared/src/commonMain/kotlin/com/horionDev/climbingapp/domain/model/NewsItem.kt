package com.horionDev.climbingapp.domain.model

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize
import kotlinx.serialization.Serializable

@CommonParcelize
class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
//    val date: String,
) : CommonParcelable