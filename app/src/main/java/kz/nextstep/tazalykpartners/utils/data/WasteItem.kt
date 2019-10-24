package kz.nextstep.tazalykpartners.utils.data

data class WasteItem(
    val selected_waste_type: String? = "",
    val selected_waste_id: String? = "",
    val passed_total: Double? = 0.0,
    val userId: String? = "",
    val passedDate: String? = ""
)