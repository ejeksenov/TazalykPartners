package kz.nextstep.tazalykpartners.ui.pinDetailedInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.WorkTime

class PinWorkTimeAdapter : RecyclerView.Adapter<PinWorkTimeAdapter.PinWorkTimeViewHolder>() {

    lateinit var workTimeList: MutableList<WorkTime>
    var currentDayOfTheWeek = 0
    var isPointOpen = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinWorkTimeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_pin_working_time_schedule_item_layout, parent, false)
        return PinWorkTimeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (::workTimeList.isInitialized) workTimeList.size - 1 else 0
    }

    fun updateWorkTimeList(workTimeList: MutableList<WorkTime>, currentDayOfTheWeek: Int, isPointOpen: Boolean) {
        this.workTimeList = workTimeList
        this.currentDayOfTheWeek = currentDayOfTheWeek
        this.isPointOpen = isPointOpen
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PinWorkTimeViewHolder, position: Int) {
        val mContext = holder.itemView.context
        val workTime = workTimeList[position]
        val dayOfWeekArr = mContext.resources.getStringArray(R.array.days_of_week)

        if (position < dayOfWeekArr.size)
            holder.tv_row_pin_working_time_schedule_item_day_of_week.text = dayOfWeekArr[position]

        var workingTime = workTime.workingTime
        if (workingTime != null) {
            workingTime = if (workingTime == "--") workingTime else workingTime.replace("-", "\n")
            holder.tv_row_pin_working_time_schedule_item.text = workingTime
        }

        var lunchTime = workTime.lunchTime
        if (lunchTime != null) {
            lunchTime = if (lunchTime == "--") lunchTime else lunchTime.replace("-", "\n")
            holder.tv_row_pin_working_time_schedule_item_lunch_time.text = lunchTime
        }

        if (currentDayOfTheWeek == position) {
            val itemLayoutBackgroundColor =
                if (isPointOpen) mContext.resources.getColor(R.color.mainBackgroundColor) else mContext.resources.getColor(
                    R.color.red
                )
            holder.layout_row_pin_working_time_schedule_item.setBackgroundColor(itemLayoutBackgroundColor)
            holder.tv_row_pin_working_time_schedule_item_day_of_week.setTextColor(mContext.resources.getColor(R.color.white))
            holder.tv_row_pin_working_time_schedule_item.setTextColor(mContext.resources.getColor(R.color.white))
            holder.tv_row_pin_working_time_schedule_item_lunch_time.setTextColor(mContext.resources.getColor(R.color.white))
            holder.tv_row_pin_working_time_schedule_item_lunch_time.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_lens_white_4dp,
                0,
                0
            )
        } else {
            holder.layout_row_pin_working_time_schedule_item.setBackgroundColor(mContext.resources.getColor(R.color.white))
            holder.tv_row_pin_working_time_schedule_item_day_of_week.setTextColor(mContext.resources.getColor(R.color.grayColor))
            holder.tv_row_pin_working_time_schedule_item.setTextColor(mContext.resources.getColor(R.color.grayColor))
            holder.tv_row_pin_working_time_schedule_item_lunch_time.setTextColor(mContext.resources.getColor(R.color.grayColor))
            holder.tv_row_pin_working_time_schedule_item_lunch_time.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_lens_gray_4dp,
                0,
                0
            )
        }

    }

    class PinWorkTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout_row_pin_working_time_schedule_item: ViewGroup =
            itemView.findViewById(R.id.layout_row_pin_working_time_schedule_item)
        var tv_row_pin_working_time_schedule_item_day_of_week: TextView =
            itemView.findViewById(R.id.tv_row_pin_working_time_schedule_item_day_of_week)
        var tv_row_pin_working_time_schedule_item: TextView =
            itemView.findViewById(R.id.tv_row_pin_working_time_schedule_item)
        var tv_row_pin_working_time_schedule_item_lunch_time: TextView =
            itemView.findViewById(R.id.tv_row_pin_working_time_schedule_item_lunch_time)
    }
}