package com.appmobiledition.laundryfinder.domain.model

import com.appmobiledition.laundryfinder.domain.model.composition.Location
import com.appmobiledition.laundryfinder.utils.CommonParcelable
import com.appmobiledition.laundryfinder.utils.CommonParcelize

@CommonParcelize
data class Place(
    val name: String,
    val location: Location
) : CommonParcelable