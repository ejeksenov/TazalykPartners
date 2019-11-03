package kz.nextstep.tazalykpartners.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kz.nextstep.tazalykpartners.R

class CustomWasteTypeInfoDialog(private val mContext: Context, private val typeItem: String): Dialog(mContext) {

    private lateinit var ivFilterByTypeItemInfoClose: ImageView
    private lateinit var ivFilterByTypeItemInfoIcon: ImageView
    private lateinit var tvFilterByTypeItemInfoName: TextView
    private lateinit var tvFilterByTypeItemInfoDetailedInfo: TextView
    private lateinit var btnFilterByTypeItemInfoClear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_filter_by_type_grid_item_info)

        ivFilterByTypeItemInfoClose = findViewById(R.id.iv_filter_by_type_item_info_close)
        ivFilterByTypeItemInfoIcon = findViewById(R.id.iv_filter_by_type_item_info_icon)
        tvFilterByTypeItemInfoName = findViewById(R.id.tv_filter_by_type_item_info_name)
        tvFilterByTypeItemInfoDetailedInfo = findViewById(R.id.tv_filter_by_type_item_info_detailed_info)
        btnFilterByTypeItemInfoClear = findViewById(R.id.btn_filter_by_type_item_info_clear)

        val typeItemArr = typeItem.split(",")
        if (typeItemArr.size >= 5) {
            val drawableResId = mContext.resources.getIdentifier(typeItemArr[3], "drawable", mContext.packageName)
            val descriptionResId = mContext.resources.getIdentifier(typeItemArr[4], "string", mContext.packageName)

            tvFilterByTypeItemInfoName.text = typeItemArr[1].capitalize()
            tvFilterByTypeItemInfoDetailedInfo.text = mContext.getString(descriptionResId)
            ivFilterByTypeItemInfoIcon.setImageResource(drawableResId)
        }


        btnFilterByTypeItemInfoClear.setOnClickListener {
            dismiss()
        }

        ivFilterByTypeItemInfoClose.setOnClickListener {
            dismiss()
        }
    }
}