package kz.nextstep.tazalykpartners.ui.addEditPin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kz.nextstep.tazalykpartners.R

class WeekDaysListAdapter(private val weekDays: MutableList<String>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_week_days_list_item_layout, parent, false)
            val rowWeekDaysListItemLayout: ViewGroup = itemView.findViewById(R.id.row_week_days_list_item_layout)
            val tvRowWeekDay: TextView  = itemView.findViewById(R.id.tv_row_week_day)
            val viewHolder = WeekDaysListViewHolder(rowWeekDaysListItemLayout, tvRowWeekDay)
            itemView.tag = viewHolder
        }
        val viewHolder = itemView!!.tag as WeekDaysListViewHolder
        val weekDay = weekDays[position]

        viewHolder.tvRowWeekDay.text = weekDay

        return itemView
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return weekDays.size
    }

    class WeekDaysListViewHolder(
        row_week_days_list_item_layout: ViewGroup,
        tv_row_week_day: TextView
    ) {
        var rowWeekDaysListItemLayout: ViewGroup = row_week_days_list_item_layout
        var tvRowWeekDay: TextView = tv_row_week_day
    }
}