package kz.nextstep.domain.model

data class Marking(
    val markingName: String? = "",
    val markingNumberId: String? = "",
    val shortMarkingDescription: String? = "",
    val markingDescription: String? = "",
    val markingType: String? = "",
    val markingLogoUrl: String? = "",
    val markingImageUrl: String? = ""
)