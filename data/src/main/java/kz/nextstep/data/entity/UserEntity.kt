package kz.nextstep.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserEntity(
    val userName: String? = "",
    val total: String? = "",
    val bonus: String? = "",
    val imageLink: String? = "",
    val phoneNumber: String? = "",
    val email: String? = "",
    val userRole: String? = "",
    val city: String? = "",
    val bio: String? = "",
    val status: String? = "",
    val website: String? = "",
    val fbPage: String? = "",
    val instaPage: String? = "",
    val prev_total: String? = ""
)