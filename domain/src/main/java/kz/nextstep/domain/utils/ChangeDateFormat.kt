package kz.nextstep.domain.utils

import java.text.SimpleDateFormat
import java.util.*

object ChangeDateFormat {
    fun onChangeDateFormat(releasedDate: String?): String {
        var simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("ru"))
        val date: Date = simpleDateFormat.parse(releasedDate)

        simpleDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale("ru"))
        return simpleDateFormat.format(date)
    }


}