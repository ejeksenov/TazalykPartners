package kz.nextstep.domain.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ChangeDateFormat {
    fun onChangeDateFormat(releasedDate: String?): String {
        var simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("ru"))
        val date: Date = simpleDateFormat.parse(releasedDate)

        simpleDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT, Locale("ru"))
        return simpleDateFormat.format(date)
    }

    fun getCurrentDate(): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale("ru"))
        return simpleDateFormat.format(date)
    }


    fun onGetFilterDate(valueofDays: Int): String {
        val str: String
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        val cal = GregorianCalendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DAY_OF_YEAR, -valueofDays)
        val beforeDate = cal.time
        val beforeDateStr = simpleDateFormat.format(beforeDate)
        val afterDateStr = simpleDateFormat.format(Date())
        str = "$beforeDateStr-$afterDateStr"
        return str
    }

    fun onCompareData(start_date: String, end_date: String, passed_date: String): Boolean {
        val simpleDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT, Locale("ru"))
        val startDate: Date
        val endDate: Date
        val passedDate: Date
        try {
            startDate = simpleDateFormat.parse(start_date)
            endDate = simpleDateFormat.parse(end_date)
            passedDate = simpleDateFormat.parse(passed_date)
            if ((passedDate == startDate || passedDate.after(startDate)) && (passedDate == endDate || passedDate.before(
                    endDate
                ) || passedDate.toString() == "")
            )
                return true
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }


    fun isOnDate(passedDate: String, selectedDates: String): Boolean {
        if (selectedDates == "")
            return true
        else {
            var startDate = ""
            var endDate = ""
            if (selectedDates != "") {
                val dates = selectedDates.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                if (dates.size == 1) run {
                    startDate = dates[0]
                    endDate = ""
                } else if (dates.size > 1) {
                    startDate = dates[0]
                    endDate = dates[dates.size - 1]
                    endDate = onChangeDateFormat("$endDate 11:11:11")
                }
                startDate = onChangeDateFormat("$startDate 11:11:11")

            }
            if (endDate == "" && passedDate == startDate)
                return true
            else if (onCompareData(startDate, endDate, passedDate))
                return true
        }
        return false
    }
}