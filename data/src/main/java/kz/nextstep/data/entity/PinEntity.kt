package kz.nextstep.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PinEntity(
    val name: String? = "",
    val description: String? = "",
    val address: String? = "",
    val phone: String? = "",
    val workTime: String? = "",
    val qrCode: String? = "",
    val imageLink: String? = "",
    val phoneName: String? = "",
    val logo: String? = "",
    val city: String? = "",
    val country: String? = "",
    val wasteId: String? = "",
    val partner: String? = "",
    val latlng: String? = "",
    val verificationCode: String? = ""
)