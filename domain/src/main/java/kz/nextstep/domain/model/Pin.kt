package kz.nextstep.domain.model

data class Pin(
    var name: String? = "",
    var description: String? = "",
    var address: String? = "",
    var phone: String? = "",
    var workTime: String? = "",
    var qrCode: String? = "",
    var imageLink: String? = "",
    var phoneName: String? = "",
    var logo: String? = "",
    var city: String? = "",
    var country: String? = "",
    var wasteId: String? = "",
    var partner: String? = "",
    var latlng: String? = "",
    var verificationCode: String? = ""
)