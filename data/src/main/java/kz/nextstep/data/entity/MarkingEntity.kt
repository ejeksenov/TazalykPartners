package kz.nextstep.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MarkingEntity(
    val markingName: String? = "",
    val markingNumberId: String? = "",
    val shortMarkingDescription: String? = "",
    val markingDescription: String? = "",
    val markingType: String? = "",
    val markingLogoUrl: String? = "",
    val markingImageUrl: String? = ""
)