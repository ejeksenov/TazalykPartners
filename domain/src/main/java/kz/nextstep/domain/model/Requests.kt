package kz.nextstep.domain.model

data class Requests(
val name: String,
val name_of_company: String,
val phone_number: String,
val short_description: String,
val request_type: String,
val address_city: String,
val waste_type: String,
val city: String,
val rating_grade: String,
val pin_id: String,
val user_id: String
)