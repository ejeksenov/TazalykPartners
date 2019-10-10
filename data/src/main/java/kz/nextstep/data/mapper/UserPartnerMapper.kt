package kz.nextstep.data.mapper

import kz.nextstep.data.entity.UserPartnerEntity
import kz.nextstep.domain.model.UserPartner

object UserPartnerMapper {
    fun map(userPartnerEntity: UserPartnerEntity) = UserPartner(
        email = userPartnerEntity.email,
        name = userPartnerEntity.name,
        pinIds = userPartnerEntity.pinIds,
        imageUrl = userPartnerEntity.imageUrl
    )

}