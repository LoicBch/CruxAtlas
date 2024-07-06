package com.horionDev.climbingapp.domain.model

data class Starter(
    val filterServices : List<Pair<String, String>>,
    val filterBrands : List<Pair<String, String>>,
    val menuLinks : List<MenuLink>
)