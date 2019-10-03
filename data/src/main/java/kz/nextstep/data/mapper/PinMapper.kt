package kz.nextstep.data.mapper

import kz.nextstep.data.entity.PinEntity
import kz.nextstep.domain.model.Pin

object PinMapper {
    fun map(pinEntity: PinEntity) = Pin(
        name = pinEntity.name,
        address = pinEntity.address,
        description = pinEntity.description,
        phone = pinEntity.phone,
        phoneName = pinEntity.phoneName,
        workTime = pinEntity.workTime,
        qrCode = pinEntity.qrCode,
        imageLink = pinEntity.imageLink,
        logo = pinEntity.logo,
        city = pinEntity.city,
        country = pinEntity.country,
        wasteId = pinEntity.wasteId,
        partner = pinEntity.partner,
        latlng = pinEntity.latlng,
        verificationCode = pinEntity.verificationCode
    )
}