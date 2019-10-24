package kz.nextstep.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class HistoryPinEntity(
    val pinId: String? = "",
    val userId: String? = "",
    val time: String? = "",
    val total: String? = ""
)