package kz.nextstep.data.mapper

import kz.nextstep.data.entity.RequestsEntity
import kz.nextstep.domain.model.Requests

object RequestsMapper {
    fun map(requestsEntity: RequestsEntity) = Requests(
        name = requestsEntity.name,
        name_of_company = requestsEntity.name_of_company,
        phone_number = requestsEntity.phone_number,
        short_description = requestsEntity.short_description,
        request_type = requestsEntity.request_type,
        address_city = requestsEntity.address_city,
        waste_type = requestsEntity.waste_type,
        city = requestsEntity.city,
        rating_grade = requestsEntity.rating_grade,
        pin_id = requestsEntity.pin_id,
        user_id = requestsEntity.user_id
    )
}