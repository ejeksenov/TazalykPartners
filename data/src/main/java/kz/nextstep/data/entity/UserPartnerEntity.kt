package kz.nextstep.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserPartnerEntity(
    val email: String? = "",
    val name: String? = "",
    val pinIds: String? = "",
    val imageUrl: String? = "",
    val productIds: String? = ""
)