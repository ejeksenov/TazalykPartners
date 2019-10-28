package kz.nextstep.tazalykpartners.ui.filterByDate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.data.DateTypeItem

class FilterByDateAdapter(private val filterDateList: MutableList<DateTypeItem>) :
    RecyclerView.Adapter<FilterByDateAdapter.FilterByDateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterByDateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_filter_by_date_item_layout, parent, false)
        return FilterByDateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filterDateList.size
    }

    override fun onBindViewHolder(holder: FilterByDateViewHolder, position: Int) {
        val dateTypeItem = filterDateList[position]
        val dateText = dateTypeItem.dateType + dateTypeItem.selectedDate
        holder.tvRowFilterByDateItem.text = dateText
        if (dateTypeItem.isSelected)
            holder.tvRowFilterByDateItem.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_check_circle_green,
                0
            )
        else {
            holder.tvRowFilterByDateItem.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_circle_white,
                0
            )
            if (position == filterDateList.size - 1)
                holder.tvRowFilterByDateItem.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_chevron_right_gray,
                    0
                )
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    var onItemClick: ((Int) -> Unit)? = null

    class FilterByDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRowFilterByDateItem: TextView = itemView.findViewById(R.id.tv_row_filter_by_date_item)
    }

}