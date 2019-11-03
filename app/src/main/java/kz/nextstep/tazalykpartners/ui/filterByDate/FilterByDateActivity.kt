package kz.nextstep.tazalykpartners.ui.filterByDate

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_filter_by_date.*
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.domain.utils.ChangeDateFormat
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.filterDateDays
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.selectedDates
import kz.nextstep.tazalykpartners.ui.navigationDrawer.NavigationDrawerActivity.Companion.selectedFilterType
import kz.nextstep.tazalykpartners.ui.statistics.StatisticsFragment
import kz.nextstep.tazalykpartners.utils.data.DateTypeItem

class FilterByDateActivity : AppCompatActivity() {

    lateinit var rvFilterByDate: RecyclerView
    lateinit var btnFilterByDate: Button
    lateinit var toolbar: Toolbar

    var filterDateList: MutableList<DateTypeItem> = ArrayList()
    lateinit var filterDateArray: Array<String>
    lateinit var adapter: FilterByDateAdapter
    var filterDateType = "За месяц"
    var filterDateDays = 30
    var selectedDates = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_by_date)

        if (intent != null) {
            selectedFilterType = intent.getStringExtra(AppConstants.SELECTED_FILTER_TYPE)!!
            selectedDates = intent.getStringExtra(AppConstants.SELECTED_DATES)!!
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btnFilterByDate = findViewById(R.id.btn_filter_by_date)
        rvFilterByDate = findViewById(R.id.rv_filter_by_date)
        rvFilterByDate.layoutManager = LinearLayoutManager(this)


        filterDateArray = resources.getStringArray(R.array.filter_date_type)
        onManageFilterDateList(filterDateArray)

        adapter = FilterByDateAdapter(filterDateList)
        rvFilterByDate.adapter = adapter

        adapter.onItemClick = {
            if (it < filterDateList.size - 1) {
                for ((index, item) in filterDateList.withIndex()) {
                    if (index == it) {
                        onManageFilterDateDays(index)
                        item.isSelected = true
                        selectedFilterType = item.dateType!!
                        selectedDates = ChangeDateFormat.onGetFilterDate(filterDateDays)
                    } else
                        item.isSelected = false
                    item.selectedDate = ""
                }
            } else
                goToCalendarActivity()
            adapter.notifyDataSetChanged()
        }

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }


        btnFilterByDate.setOnClickListener {
            onReturnResult()
        }
    }

    private fun onReturnResult() {
        val intent = Intent()
        intent.putExtra(AppConstants.FILTER_DATE_DAYS, filterDateDays)
        intent.putExtra(AppConstants.SELECTED_DATES, selectedDates)
        intent.putExtra(AppConstants.SELECTED_FILTER_TYPE, selectedFilterType)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onManageFilterDateDays(index: Int) {
        when (index) {
            0 -> filterDateDays = 7
            1 -> filterDateDays = 30
            2 -> filterDateDays = 90
            3 -> filterDateDays = 180
            4 -> filterDateDays = 365
        }
    }


    private fun onManageFilterDateList(filterDateArray: Array<String>) {
        var arrayIndex = filterDateArray.indexOf(selectedFilterType)
        if (selectedDates == selectedFilterType) {
            arrayIndex = filterDateArray.size - 1
        }
        for ((index, arrayItem) in filterDateArray.withIndex()) {
            val dateTypeItem = DateTypeItem(
                if (index == arrayIndex && arrayIndex == filterDateArray.size - 1) "$arrayItem($selectedFilterType)" else arrayItem,
                index == arrayIndex,
                ""
            )
            filterDateList.add(dateTypeItem)
        }
    }

    private fun goToCalendarActivity() {
        val intent = Intent(this, CalendarActivity::class.java)
        intent.putExtra(AppConstants.SELECTED_DATES, selectedDates)
        startActivityForResult(intent, AppConstants.REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedDates = data?.getStringExtra(AppConstants.SELECTED_DATES)!!
            filterDateDays = data.getIntExtra(AppConstants.FILTER_DATE_DAYS, 30)
            filterDateType = selectedDates
            if (selectedDates != "")
                onReturnResult()
        }
    }
}
