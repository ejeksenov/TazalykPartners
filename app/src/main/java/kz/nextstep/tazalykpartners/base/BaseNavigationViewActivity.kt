package kz.nextstep.tazalykpartners.base

import androidx.appcompat.app.AppCompatActivity

open class BaseNavigationViewActivity: AppCompatActivity() {
    companion object {
        var selectedWasteId = ""
        var selectedDates = ""
        var selectedFilterType = "За месяц"
        var filterDateDays = 30
    }
}