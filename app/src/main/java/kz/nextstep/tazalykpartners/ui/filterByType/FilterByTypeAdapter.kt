package kz.nextstep.tazalykpartners.ui.filterByType

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kz.nextstep.tazalykpartners.R
import kz.nextstep.tazalykpartners.utils.CustomWasteTypeInfoDialog

class FilterByTypeAdapter(
    private val typeItemList: MutableList<String>,
    private val selectedTypeItemList: MutableList<String>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.row_filter_by_type_item_layout, parent, false)
            val layoutFilterByTypeItem: ViewGroup = itemView.findViewById(R.id.layout_filter_by_type_item)
            val ivFilterByTypeItemInfo: ImageView = itemView.findViewById(R.id.iv_filter_by_type_item_info)
            val ivFilterByTypeItemIcon: ImageView = itemView.findViewById(R.id.iv_filter_by_type_item_icon)
            val tvFilterByTypeItemName: TextView = itemView.findViewById(R.id.tv_filter_by_type_item_name)

            val viewHolder = FilterByTypeViewHolder(
                layoutFilterByTypeItem,
                ivFilterByTypeItemInfo,
                ivFilterByTypeItemIcon,
                tvFilterByTypeItemName
            )
            itemView.tag = viewHolder
        }

        val viewHolder = itemView!!.tag as FilterByTypeViewHolder
        val typeItemStr = typeItemList[position]
        val mContext = parent.context
        val selectColor: Int
        if (selectedTypeItemList.contains(typeItemStr)) {
            selectColor = mContext.resources.getColor(R.color.mainBackgroundColor)
            viewHolder.layoutFilterByTypeItem.background =
                mContext.resources.getDrawable(R.drawable.background_rounded_cornered_selected_filter_grid_layout)
        } else {
            selectColor = mContext.resources.getColor(R.color.not_selected_grid_item_color)
            viewHolder.layoutFilterByTypeItem.background =
                mContext.resources.getDrawable(R.drawable.round_cornered_filter_grid_item)
        }
        viewHolder.ivFilterByTypeItemIcon.setColorFilter(selectColor)
        viewHolder.ivFilterByTypeItemInfo.setColorFilter(selectColor)
        viewHolder.tvFilterByTypeItemName.setTextColor(selectColor)

        val typeItemArr = typeItemStr.split(",")
        if (typeItemArr.size >= 4) {
            viewHolder.tvFilterByTypeItemName.text = typeItemArr[1].capitalize()
            val drawableResId = mContext.resources.getIdentifier(typeItemArr[3], "drawable", mContext.packageName)
            viewHolder.ivFilterByTypeItemIcon.setImageResource(drawableResId)
            viewHolder.ivFilterByTypeItemInfo.setOnClickListener {
                val customWasteTypeInfoDialog = CustomWasteTypeInfoDialog(mContext, typeItemStr)
                customWasteTypeInfoDialog.show()
            }
        }
        return itemView
    }


    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return typeItemList.size
    }

    class FilterByTypeViewHolder(
        layout_filter_by_type_item: ViewGroup,
        iv_filter_by_type_item_info: ImageView,
        iv_filter_by_type_item_icon: ImageView,
        tv_filter_by_type_item_name: TextView
    ) {
        var layoutFilterByTypeItem: ViewGroup = layout_filter_by_type_item
        var ivFilterByTypeItemInfo: ImageView = iv_filter_by_type_item_info
        var ivFilterByTypeItemIcon: ImageView = iv_filter_by_type_item_icon
        var tvFilterByTypeItemName: TextView = tv_filter_by_type_item_name
    }
}