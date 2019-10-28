package kz.nextstep.tazalykpartners.ui.filterByDate

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.squareup.timessquare.CalendarPickerView
import kz.nextstep.domain.utils.AppConstants
import kz.nextstep.tazalykpartners.R
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var tvCalendarBeforeDate: TextView
    lateinit var tvCalendarAfterDate: TextView
    lateinit var btnCalendarApply: Button
    lateinit var cvCalendarView: CalendarPickerView

    var startD = ""
    var endD = ""
    var selectedDate = ""
    lateinit var simpleDateFormat: SimpleDateFormat
    var dateSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        tvCalendarAfterDate = findViewById(R.id.tv_calendar_after_date)
        tvCalendarBeforeDate = findViewById(R.id.tv_calendar_before_date)
        cvCalendarView = findViewById(R.id.cv_calendar_view)
        btnCalendarApply = findViewById(R.id.btn_calendar_apply)

        initCalendarPickerView()

        if (intent != null) {
            selectedDate = intent.getStringExtra(AppConstants.SELECTED_DATES)!!
            highlightSelectedDates(selectedDate)
        }

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }


        cvCalendarView.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date?) {
                cvCalendarView.clearHighlightedDates()
                onGetDateFromCalendar()
                if (startD != "" && endD != "" && dateSize > 1) {
                    tvCalendarBeforeDate.text = startD
                    tvCalendarAfterDate.text = endD
                } else {
                    tvCalendarBeforeDate.text = startD
                    tvCalendarAfterDate.text = "-"
                }
            }

            override fun onDateUnselected(date: Date?) {}

        })


        btnCalendarApply.setOnClickListener {
            onGetDateFromCalendar()
            onReturnResult(selectedDate)
        }
    }

    private fun onReturnResult(selectedDate: String) {
        val returnIntent = Intent()
        returnIntent.putExtra(AppConstants.SELECTED_DATES, selectedDate)
        returnIntent.putExtra(AppConstants.FILTER_DATE_DAYS, dateSize)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun onGetDateFromCalendar() {
        selectedDate = ""
        val dates = cvCalendarView.selectedDates
        dateSize = dates.size
        if (dateSize == 1) {
            selectedDate = simpleDateFormat.format(dates[0])
        } else if (dateSize > 1) {
            val cal = GregorianCalendar.getInstance()
            cal.time = dates[0]
            cal.add(Calendar.DAY_OF_YEAR, 0)
            startD = simpleDateFormat.format(cal.time)
            cal.time = dates[dateSize - 1]
            cal.add(Calendar.DAY_OF_YEAR, 0)
            endD = simpleDateFormat.format(cal.time)
            selectedDate = "$startD-$endD"
        }
    }

    private fun highlightSelectedDates(selectedDate: String) {
        if (selectedDate != "" && selectedDate.contains("-")) {
            val selectedDateList: MutableList<Date> = ArrayList()
            val selectedDateListItemArr = selectedDate.split("-")
            tvCalendarBeforeDate.text = selectedDateListItemArr[0]
            val cStart = Calendar.getInstance()
            val startDateArr = selectedDateListItemArr[0].split(".")
            cStart.set(
                Integer.parseInt(startDateArr[2]),
                Integer.parseInt(startDateArr[1]),
                Integer.parseInt(startDateArr[0])
            )
            cStart.add(Calendar.MONTH, -1)
            val startDate = cStart.time
            cvCalendarView.scrollToDate(startDate)
            if (selectedDateListItemArr[1] == "") {
                selectedDateList.add(startDate)
                cvCalendarView.highlightDates(selectedDateList)
            } else {
                tvCalendarAfterDate.text = selectedDateListItemArr[1]
                val cEnd = Calendar.getInstance()
                val endDateArr = selectedDateListItemArr[1].split(".")
                cEnd.set(
                    Integer.parseInt(endDateArr[2]),
                    Integer.parseInt(endDateArr[1]),
                    Integer.parseInt(endDateArr[0])
                )
                cEnd.add(Calendar.MONTH, -1)
                while (cStart.compareTo(cEnd) < 1) {
                    val addingDate = cStart.time
                    selectedDateList.add(addingDate)
                    cStart.add(Calendar.DAY_OF_MONTH, 1)
                }
                cvCalendarView.highlightDates(selectedDateList)
            }
        }
    }

    private fun initCalendarPickerView() {
        simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 2)
        val prevYear = Calendar.getInstance()
        prevYear.add(Calendar.YEAR, -2)
        val today = Date()
        cvCalendarView.init(prevYear.time, nextYear.time).inMode(CalendarPickerView.SelectionMode.RANGE)
            .withSelectedDate(today)
    }
}
