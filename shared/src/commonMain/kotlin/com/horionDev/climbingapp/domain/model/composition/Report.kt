package com.horionDev.climbingapp.domain.model.composition

import kotlinx.serialization.Serializable

@Serializable
data class Report(val reason: ReportReason, val comment: String)


enum class ReportReason {
    DESTROYED,
    WRONG_LOCATION,
    OCCUPIED,
    OTHER
}