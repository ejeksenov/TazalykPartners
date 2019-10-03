package kz.nextstep.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PinEntity(
    val address: String,
    val city: String,
    val country: String,
    val description: String,
    val imageLink: String,
    val latlng: String,
    val logo: String,
    val name: String,
    val partner: String,
    val phone: String,
    val phoneName: String,
    val qrCode: String,
    val verificationCode: String,
    val wasteId: String,
    val workTime: String
)