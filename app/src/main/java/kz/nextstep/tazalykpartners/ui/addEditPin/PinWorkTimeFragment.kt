package kz.nextstep.tazalykpartners.ui.addEditPin


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar

import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.ui.addEditPin.AddEditPinActivity.Companion.pin

class PinWorkTimeFragment : Fragment() {

    companion object {
        fun newInstance() = PinWorkTimeFragment()
    }

    lateinit var rvPinWorkTimeWeekDaysList: GridView
    lateinit var tvPinWorkTimeSchedule: TextView
    lateinit var sbPinWorkTimeSchedule: RangeSeekBar
    lateinit var tvPinWorkTimeLunch: TextView
    lateinit var sbPinWorkTimeLunch: RangeSeekBar
    lateinit var edtPinWorkTime: EditText
    lateinit var btnPinWorkTimeGetData: Button
    lateinit var btnPinWorkTimeSave: Button

    lateinit var weekDays: MutableList<String>

    var selectedWeekDays = ""

    var workScheduleRange = ""
    var lunchScheduleRange = ""
    var pinWorkTime = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_work_time, container, false)

        rvPinWorkTimeWeekDaysList = view.findViewById(R.id.rv_pin_work_time_week_days_list)
        tvPinWorkTimeSchedule = view.findViewById(R.id.tv_pin_work_time_schedule)
        sbPinWorkTimeSchedule = view.findViewById(R.id.sb_pin_work_time_schedule)
        tvPinWorkTimeLunch = view.findViewById(R.id.tv_pin_work_time_lunch)
        sbPinWorkTimeLunch = view.findViewById(R.id.sb_pin_work_time_lunch)
        edtPinWorkTime = view.findViewById(R.id.edt_pin_work_time)
        btnPinWorkTimeGetData = view.findViewById(R.id.btn_pin_work_time_get_data)
        btnPinWorkTimeSave = view.findViewById(R.id.btn_pin_work_time_save)

        onSetWeekDaysAdapter()

        val workTimeStr = resources.getText(R.string.work_time)
        val lunchTimeStr = resources.getText(R.string.lunch_time)

        pinWorkTime = pin.workTime!!
        edtPinWorkTime.setText(pinWorkTime)

        sbPinWorkTimeLunch.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {}

            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                lunchScheduleRange =
                    if (leftValue == 600F && rightValue == 630F) "" else onFormatToTimeRange(leftValue, rightValue)
                tvPinWorkTimeLunch.text = "$lunchTimeStr: $lunchScheduleRange"

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {}

        })

        sbPinWorkTimeSchedule.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {}

            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                workScheduleRange =
                    if (leftValue == 480F && rightValue == 540F) "" else onFormatToTimeRange(leftValue, rightValue)
                tvPinWorkTimeSchedule.text = "$workTimeStr: $workScheduleRange"
            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {}

        })

        btnPinWorkTimeGetData.setOnClickListener {
            onGetWorkTimeData()
        }

        btnPinWorkTimeSave.setOnClickListener {
            if (pinWorkTime.isNotBlank()) {
                pin.workTime = pinWorkTime
                activity!!.onBackPressed()
            } else
                Toast.makeText(context, resources.getString(R.string.set_work_schedule), Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun onGetWorkTimeData() {
        val weekendStr = ":выходной"
        if (selectedWeekDays.isBlank() || (pinWorkTime.isBlank() && selectedWeekDays.isNotBlank() && workScheduleRange.isBlank() && lunchScheduleRange.isBlank())) {
            for (item in weekDays) {
                pinWorkTime += "$item$weekendStr;"
            }
        } else {
            // if pin work time is empty
            if (pinWorkTime.isBlank()) {
                if (lunchScheduleRange.isBlank()) {
                    for (item in weekDays) {
                        pinWorkTime += if (selectedWeekDays.contains(item)) "$item:$workScheduleRange;" else "$item$weekendStr;"
                    }
                } else {
                    for (item in weekDays) {
                        val workScheduleArr = workScheduleRange.split("-")
                        val lunchScheduleArr = lunchScheduleRange.split("-")
                        pinWorkTime += if (selectedWeekDays.contains(item)) "$item:${workScheduleArr[0]}-${lunchScheduleArr[0]},${lunchScheduleArr[1]}-${workScheduleArr[1]};" else "$item$weekendStr;"
                    }
                }
            } else { // if pin work time is previously selected
                val pinWorkSchedule: MutableList<String> = pinWorkTime.split(";").toMutableList()
                for ((index, item) in pinWorkSchedule.withIndex()) {
                    for (item2 in selectedWeekDays.split(";")) {
                        if (lunchScheduleRange.isBlank() && item2.isNotBlank() && item.contains(item2)) {
                            pinWorkSchedule[index] = if (workScheduleRange.isNotBlank()) "$item2:$workScheduleRange" else "$item2$weekendStr"
                            break
                        } else if (workScheduleRange.isNotBlank() && lunchScheduleRange.isNotBlank() && item2.isNotBlank() && item.contains(item2)) {
                            val workScheduleArr = workScheduleRange.split("-")
                            val lunchScheduleArr = lunchScheduleRange.split("-")
                            pinWorkSchedule[index] =
                                "$item2:${workScheduleArr[0]}-${lunchScheduleArr[0]},${lunchScheduleArr[1]}-${workScheduleArr[1]}"
                            break
                        }
                    }
                }
                pinWorkTime = ""
                for (item in pinWorkSchedule) {
                    if (item.isNotBlank())
                        pinWorkTime += "$item;"
                }
            }
        }
        edtPinWorkTime.setText(pinWorkTime)
    }

    private fun onFormatToTimeRange(leftValue: Float, rightValue: Float): String {
        val startTime: String = onFormatToTime(leftValue)
        val endTime = onFormatToTime(rightValue)
        return "$startTime - $endTime"
    }

    private fun onFormatToTime(value: Float): String {
        val hour: Int = (value / 60).toInt()
        val minute: Int = (value - (hour * 60)).toInt()
        val hourStr = if (hour < 10) "0$hour" else hour.toString()
        val minuteStr = if (minute < 10) "0$minute" else minute.toString()
        return "$hourStr:$minuteStr"
    }


    private fun onSetWeekDaysAdapter() {
        weekDays = resources.getStringArray(R.array.days_of_week_long).toMutableList()
        val adapter = WeekDaysListAdapter(weekDays)
        rvPinWorkTimeWeekDaysList.adapter = adapter

        rvPinWorkTimeWeekDaysList.setOnItemClickListener { parent, view, position, id ->
            val rowWeekDaysListItemLayout: ViewGroup = view.findViewById(R.id.row_week_days_list_item_layout)
            val tvRowWeekDay: TextView = view.findViewById(R.id.tv_row_week_day)
            val weekDay = weekDays[position]
            if (selectedWeekDays.contains(weekDay)) {
                rowWeekDaysListItemLayout.setBackgroundColor(resources.getColor(R.color.white))
                tvRowWeekDay.setTextColor(resources.getColor(R.color.textColor))
                selectedWeekDays = selectedWeekDays.replace("$weekDay;", "", false)
            } else {
                rowWeekDaysListItemLayout.setBackgroundColor(resources.getColor(R.color.mainBackgroundColor))
                tvRowWeekDay.setTextColor(resources.getColor(R.color.white))
                selectedWeekDays += "$weekDay;"
            }
        }
    }


}
